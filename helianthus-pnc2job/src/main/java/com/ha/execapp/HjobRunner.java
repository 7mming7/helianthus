package com.ha.execapp;

import com.ha.base.CommonJobProperties;
import com.ha.event.*;
import com.ha.exception.ExecutorManagerException;
import com.ha.exception.HjobTypeManagerException;
import com.ha.executor.ExecutableFlow;
import com.ha.executor.ExecutableNode;
import com.ha.executor.ExecuteStatus;
import com.ha.graph.execute.ExecuteLoaderServiceImpl;
import com.ha.graph.execute.IExecuteLoaderService;
import com.ha.hjob.Hjob;
import com.ha.hjob.hjobType.HjobTypeManager;
import com.ha.util.SpringUtils;
import com.ha.utils.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * User: shuiqing
 * DateTime: 17/7/11 下午4:31
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HjobRunner extends EventHandler implements Runnable{

    private Logger logger = LoggerFactory.getLogger(HjobRunner.class);

    /**
     * 由于Thread非spring启动时实例化，而是根据具体的逻辑动态实例化，所以需要通过此方式从spring的context中获取相应的bean.
     */
    private IExecuteLoaderService iExecuteLoaderService =
            SpringUtils.getBean(IExecuteLoaderService.class);

    private ExecutableNode node;
    private Hjob job;
    private String jobId;

    private final Object syncObject = new Object();

    private final HjobTypeManager hjobTypeManager;

    // Used by the job to watch and block against another flow
    private Integer pipelineLevel = null;
    private FlowWatcher watcher = null;
    private Set<String> pipelineJobs = new HashSet<String>();

    private boolean killed = false;

    private BlockingStatus currentBlockStatus = null;

    private Props props;

    public HjobRunner(ExecutableNode node, HjobTypeManager hjobTypeManager) {
        this.node = node;
        this.props = node.getInputProps();
        this.jobId = node.getId();
        this.hjobTypeManager = hjobTypeManager;
    }

    public ExecutableNode getNode() {
        return node;
    }

    private void findAllStartingNodes(ExecutableFlow flow,
                                      Set<String> pipelineJobs) {
        for (String startingNode : flow.getStartNodes()) {
            ExecutableNode node = flow.getExecutableNode(startingNode);
            if (node instanceof ExecutableFlow) {
                findAllStartingNodes((ExecutableFlow) node, pipelineJobs);
            } else {
                pipelineJobs.add(node.getNestedId());
            }
        }
    }

    /**
     * Used to handle non-ready and special status's (i.e. KILLED). Returns true
     * if they handled anything.
     *
     * @return
     */
    private boolean handleNonReadyStatus() {
        ExecuteStatus nodeStatus = node.getStatus();
        boolean quickFinish = false;
        long time = System.currentTimeMillis();

        if (ExecuteStatus.isStatusFinished(nodeStatus)) {
            quickFinish = true;
        } else if (nodeStatus == ExecuteStatus.DISABLED) {
            nodeStatus = changeStatus(ExecuteStatus.SKIPPED, time);
            quickFinish = true;
        } else if (this.isKilled()) {
            nodeStatus = changeStatus(ExecuteStatus.KILLED, time);
            quickFinish = true;
        }

        if (quickFinish) {
            fireEvent(Event.create(this, Event.Type.JOB_STARTED, new EventData(nodeStatus, node.getNestedId())));
            fireEvent(Event.create(this, Event.Type.JOB_FINISHED, new EventData(nodeStatus, node.getNestedId())));
            return true;
        }

        return false;
    }

    private ExecuteStatus changeStatus(ExecuteStatus executeStatus) {
        changeStatus(executeStatus, System.currentTimeMillis());
        return executeStatus;
    }

    private ExecuteStatus changeStatus(ExecuteStatus executeStatus, long time) {
        node.setStatus(executeStatus);
        return executeStatus;
    }

    private void fireEvent(Event event) {
        fireEvent(event, true);
    }

    private void fireEvent(Event event, boolean updateTime) {
        this.fireEventListeners(event);
    }

    public void kill() {
        synchronized (syncObject) {
            if (ExecuteStatus.isStatusFinished(node.getStatus())) {
                return;
            }
            logError("Kill has been called.");
            this.killed = true;

            BlockingStatus status = currentBlockStatus;
            if (status != null) {
                status.unblock();
            }

            // Cancel code here
            if (job == null) {
                logError("Job hasn't started yet.");
                // Just in case we're waiting on the delay
                synchronized (this) {
                    this.notify();
                }
                return;
            }

            try {
                job.cancel();
            } catch (Exception e) {
                logError(e.getMessage());
                logError("Failed trying to cancel job. Maybe it hasn't started running yet or just finished.");
            }

            this.changeStatus(ExecuteStatus.KILLED);
        }
    }

    public boolean isKilled() {
        return killed;
    }

    public ExecuteStatus getExecuteStatus() {
        return node.getStatus();
    }

    private void logError(String message) {
        if (logger != null) {
            logger.error(message);
        }
    }

    private void logError(String message, Throwable t) {
        if (logger != null) {
            logger.error(message, t);
        }
    }

    private void logInfo(String message) {
        if (logger != null) {
            logger.info(message);
        }
    }

    /**
     * The main run thread.
     */
    @Override
    public void run() {
        Thread.currentThread().setName(
                "JobRunner-" + this.jobId);

        // If the job is cancelled, disabled, killed. No log is created in this case
        if (handleNonReadyStatus()) {
            return;
        }

        boolean errorFound = false;

        ExecuteStatus finalStatus = node.getStatus();
        if (!errorFound && !isKilled()) {
            fireEvent(Event.create(this, Event.Type.JOB_STARTED, new EventData(node)));

            ExecuteStatus prepareStatus = prepareJob();
            if (prepareStatus != null) {
                // Writes status to the db
                /*writeStatus();*/
                fireEvent(Event.create(this, Event.Type.JOB_STATUS_CHANGED,
                        new EventData(prepareStatus, node.getNestedId())));
                finalStatus = runJob();
            } else {
                finalStatus = changeStatus(ExecuteStatus.FAILED);
                logError("Job run failed preparing the job.");
            }
        }
    }

    private void writeStatus() {
        try {
            iExecuteLoaderService.updateNode(node);
        } catch (ExecutorManagerException e) {
            logger.error("Could not update job properties in db for "
                    + this.jobId, e);
        }
    }

    private ExecuteStatus prepareJob() throws RuntimeException {
        // Check pre conditions
        if (props == null || this.isKilled()) {
            logError("Failing job. The job properties don't exist");
            return null;
        }

        ExecuteStatus finalStatus;
        synchronized (syncObject) {
            if (node.getStatus() == ExecuteStatus.FAILED || this.isKilled()) {
                return null;
            }

            // If it's an embedded flow, we'll add the nested flow info to the job
            // conf
            if (node.getExecutableFlow() != node.getParentFlow()) {
                String subFlow = node.getPrintableId(":");
                props.put(CommonJobProperties.NESTED_FLOW_PATH, subFlow);
            }

            props.put(CommonJobProperties.JOB_ID, this.jobId);
            finalStatus = changeStatus(ExecuteStatus.RUNNING);

            try {
                job = hjobTypeManager.buildJobExecutor(this.jobId, props);
            } catch (HjobTypeManagerException e) {
                logger.error("Failed to build job type", e);
                return null;
            }
        }

        return finalStatus;
    }

    private ExecuteStatus runJob() {
        ExecuteStatus finalStatus = node.getStatus();
        try {
            job.run();
        } catch (Throwable e) {
            logError("Job run failed!", e);
            logError(e.getMessage() + " cause: " + e.getCause());
        }

        // If the job is still running, set the status to Success.
        if (!ExecuteStatus.isStatusFinished(finalStatus)) {
            finalStatus = changeStatus(ExecuteStatus.SUCCEEDED);
        }
        return finalStatus;
    }

}

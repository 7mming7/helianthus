package com.ha.execapp;

import com.ha.event.Event;
import com.ha.event.EventData;
import com.ha.event.EventHandler;
import com.ha.event.FlowWatcher;
import com.ha.exception.ExecutorManagerException;
import com.ha.executor.ExecutableFlow;
import com.ha.executor.ExecutableNode;
import com.ha.executor.ExecuteStatus;
import com.ha.executor.ExecutionOptions;
import com.ha.graph.execute.IExecuteLoaderService;
import com.ha.hjob.hjobType.HjobTypeManager;
import com.ha.util.SpringUtils;
import com.ha.utils.Props;
import com.ha.utils.PropsUtils;
import com.ha.utils.SwapQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * Class that handles the running of a ExecutableFlow DAG
 * User: shuiqing
 * DateTime: 17/7/18 下午2:55
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HflowRunner extends EventHandler implements Runnable {

    private Logger logger = LoggerFactory.getLogger(HflowRunner.class);

    // We check update every 5 minutes, just in case things get stuck. But for the
    // most part, we'll be idling.
    private static final long CHECK_WAIT_MS = 5 * 60 * 1000;

    private final ExecutableFlow flow;
    private Thread flowRunnerThread;
    private int numJobThreads = 10;
    private ExecutionOptions.FailureAction failureAction;

    // Sync object for queuing
    private final Object mainSyncObj = new Object();

    // Properties map
    private Props importProps;
    private Map<String, Props> sharedProps = new HashMap<String, Props>();
    private final HjobTypeManager hjobTypeManager;

    private ExecutorService executorService;

    // Watches external flows for execution.
    private FlowWatcher watcher = null;

    private boolean flowPaused = false;
    private boolean flowFailed = false;
    private boolean flowFinished = false;
    private boolean flowKilled = false;

    // The following is state that will trigger a retry of all failed jobs
    private boolean retryFailedJobs = false;

    private Set<HjobRunner> activeJobRunners = Collections
            .newSetFromMap(new ConcurrentHashMap<HjobRunner, Boolean>());

    // Thread safe swap queue for finishedExecutions.
    private SwapQueue<ExecutableNode> finishedNodes;

    private IExecuteLoaderService iExecuteLoaderService =
            SpringUtils.getBean(IExecuteLoaderService.class);

    /**
     * Constructor. This will create its own ExecutorService for thread pools
     *
     * @param flow
     * @param hjobTypeManager
     * @throws ExecutorManagerException
     */
    public HflowRunner(ExecutableFlow flow, HjobTypeManager hjobTypeManager, Props importProps)
            throws ExecutorManagerException {
        this(flow, hjobTypeManager, null, importProps);
    }

    /**
     * Constructor. If executorService is null, then it will create it's own for
     * thread pools.
     *
     * @param flow
     * @param hjobTypeManager
     * @param executorService
     * @throws ExecutorManagerException
     */
    public HflowRunner(ExecutableFlow flow, HjobTypeManager hjobTypeManager,
                      ExecutorService executorService, Props importProps) throws ExecutorManagerException {
        this.flow = flow;
        this.hjobTypeManager = hjobTypeManager;

        this.executorService = executorService;
        this.finishedNodes = new SwapQueue<ExecutableNode>();
        this.importProps = importProps;
    }

    public HflowRunner setFlowWatcher(FlowWatcher watcher) {
        this.watcher = watcher;
        return this;
    }

    public HflowRunner setNumJobThreads(int hjobs) {
        numJobThreads = hjobs;
        return this;
    }

    @Override
    public void run() {
        try {
            if (this.executorService == null) {
                this.executorService = Executors.newFixedThreadPool(numJobThreads);
            }
            flow.setStartTime(System.currentTimeMillis());

            logger.info("Updating initial flow directory.");
            updateFlow();
            logger.info("Fetching job and shared properties.");

            this.fireEventListeners(Event.create(this, Event.Type.FLOW_STARTED, new EventData(this.getExecutableFlow())));
            runFlow();
        } catch (Throwable t) {
            if (logger != null) {
                logger
                        .error(
                                "An error has occurred during the running of the flow. Quiting.",
                                t);
            }
            flow.setStatus(ExecuteStatus.FAILED);
        } finally {
            if (watcher != null) {
                logger.info("Watcher is attached. Stopping watcher.");
                watcher.stopWatcher();
                logger.info("Watcher cancelled status is " + watcher.isWatchCancelled());
            }

            flow.setEndTime(System.currentTimeMillis());

            updateFlow();
            this.fireEventListeners(Event.create(this, Event.Type.FLOW_FINISHED, new EventData(flow)));
        }
    }

    /**
     * Main method that executes the jobs.
     *
     * @throws Exception
     */
    private void runFlow() throws Exception {
        logger.info("Starting flows");
        runReadyJob(this.flow);
        updateFlow();

        //wait flow finish
        while (!flowFinished) {
            synchronized (mainSyncObj) {
                if (flowPaused) {
                    try {
                        mainSyncObj.wait(CHECK_WAIT_MS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    continue;
                } else {
                    if (retryFailedJobs) {
                        retryAllFailures();
                    } else if (!progressGraph()) {
                        try {
                            mainSyncObj.wait(CHECK_WAIT_MS);
                        } catch (InterruptedException e) {
                        }
                    }
                }
            }
        }

        logger.info("Finishing up flow. Awaiting Termination");
        executorService.shutdown();

        updateFlow();
        logger.info("Finished Flow");
    }

    private void propagateStatus(ExecutableFlow base, ExecuteStatus status) {
        if (!ExecuteStatus.isStatusFinished(base.getStatus())) {
            logger.info("Setting " + base.getNestedId() + " to " + status);
            base.setStatus(status);
            if (base.getParentFlow() != null) {
                propagateStatus(base.getParentFlow(), status);
            }
        }
    }

    public void kill() {
        synchronized (mainSyncObj) {
            if(flowKilled) return;
            flow.setStatus(ExecuteStatus.KILLED);
            // If the flow is paused, then we'll also unpause
            flowPaused = false;
            flowKilled = true;

            if (watcher != null) {
                logger.info("Watcher is attached. Stopping watcher.");
                watcher.stopWatcher();
                logger
                        .info("Watcher cancelled status is " + watcher.isWatchCancelled());
            }

            logger.info("Killing " + activeJobRunners.size() + " jobs.");
            for (HjobRunner runner : activeJobRunners) {
                runner.kill();
            }
            updateFlow();
        }
        interrupt();
    }

    private void interrupt() {
        flowRunnerThread.interrupt();
    }

    private boolean progressGraph() throws IOException {
        finishedNodes.swap();

        // The following nodes are finished, so we'll collect a list of outnodes
        // that are candidates for running next.
        HashSet<ExecutableNode> nodesToCheck = new HashSet<ExecutableNode>();
        for (ExecutableNode node : finishedNodes) {
            Set<String> outNodeIds = node.getOutNodes();
            ExecutableFlow parentFlow = node.getParentFlow();

            // If a job is seen as failed, then we set the parent flow to
            // FAILED_FINISHING
            if (node.getStatus() == ExecuteStatus.FAILED) {
                // The job cannot be retried or has run out of retry attempts. We will
                // fail the job and its flow now.
                propagateStatus(node.getParentFlow(), ExecuteStatus.FAILED_FINISHING);
                if (failureAction == ExecutionOptions.FailureAction.CANCEL_ALL) {
                    this.kill();
                }
                this.flowFailed = true;
            }

            if (outNodeIds.isEmpty()) {
                // There's no outnodes means it's the end of a flow, so we finalize
                // and fire an event.
                finalizeFlow(parentFlow);
                finishExecutableNode(parentFlow);

                // If the parent has a parent, then we process
                if (!(parentFlow instanceof ExecutableFlow)) {
                    outNodeIds = parentFlow.getOutNodes();
                    parentFlow = parentFlow.getParentFlow();
                }
            }

            // Add all out nodes from the finished job. We'll check against this set
            // to
            // see if any are candidates for running.
            for (String nodeId : outNodeIds) {
                ExecutableNode outNode = parentFlow.getExecutableNode(nodeId);
                nodesToCheck.add(outNode);
            }
        }

        // Runs candidate jobs. The code will check to see if they are ready to run
        // before
        // Instant kill or skip if necessary.
        boolean jobsRun = false;
        for (ExecutableNode node : nodesToCheck) {
            if (ExecuteStatus.isStatusFinished(node.getStatus())
                    || ExecuteStatus.isStatusRunning(node.getStatus())) {
                // Really shouldn't get in here.
                continue;
            }

            jobsRun |= runReadyJob(node);
        }

        if (jobsRun || finishedNodes.getSize() > 0) {
            updateFlow();
            return true;
        }

        return false;
    }

    private void finalizeFlow(ExecutableFlow flow) {
        String id = flow == this.flow ? "" : flow.getNestedId();

        // If it's not the starting flow, we'll create set of output props
        // for the finished flow.
        boolean succeeded = true;
        Props previousOutput = null;

        for (String end : flow.getEndNodes()) {
            ExecutableNode node = flow.getExecutableNode(end);

            if (node.getStatus() == ExecuteStatus.KILLED
                    || node.getStatus() == ExecuteStatus.FAILED
                    || node.getStatus() == ExecuteStatus.CANCELLED) {
                succeeded = false;
            }

            Props output = node.getOutputProps();
            if (output != null) {
                output = Props.clone(output);
                output.setParent(previousOutput);
                previousOutput = output;
            }
        }

        flow.setOutputProps(previousOutput);
        if (!succeeded && (flow.getStatus() == ExecuteStatus.RUNNING)) {
            flow.setStatus(ExecuteStatus.KILLED);
        }

        flow.setEndTime(System.currentTimeMillis());
        flow.setUpdateTime(System.currentTimeMillis());
        long durationSec = (flow.getEndTime() - flow.getStartTime()) / 1000;
        switch (flow.getStatus()) {
            case FAILED_FINISHING:
                logger.info("Setting flow '" + id + "' status to FAILED in "
                        + durationSec + " seconds");
                flow.setStatus(ExecuteStatus.FAILED);
                break;
            case FAILED:
            case KILLED:
            case CANCELLED:
            case FAILED_SUCCEEDED:
                logger.info("Flow '" + id + "' is set to " + flow.getStatus().toString()
                        + " in " + durationSec + " seconds");
                break;
            default:
                flow.setStatus(ExecuteStatus.SUCCEEDED);
                logger.info("Flow '" + id + "' is set to " + flow.getStatus().toString()
                        + " in " + durationSec + " seconds");
        }

        // If the finalized flow is actually the top level flow, than we finish
        // the main loop.
        if (flow instanceof ExecutableFlow) {
            flowFinished = true;
        }
    }

    private void updateFlow() {
        updateFlow(System.currentTimeMillis());
    }

    private synchronized void updateFlow(long time) {
        flow.setUpdateTime(time);
        try {
            iExecuteLoaderService.updateFlow(flow);
        } catch (ExecutorManagerException e) {
            e.printStackTrace();
        }
    }

    private boolean runReadyJob(ExecutableNode node) throws IOException {
        if (ExecuteStatus.isStatusFinished(node.getStatus())
                || ExecuteStatus.isStatusRunning(node.getStatus())) {
            return false;
        }

        ExecuteStatus nextNodeStatus = getImpliedStatus(node);
        if (nextNodeStatus == null) {
            return false;
        }

        if (nextNodeStatus == ExecuteStatus.CANCELLED) {
            logger.info("Cancelling '" + node.getNestedId()
                    + "' due to prior errors.");
            node.cancelNode(System.currentTimeMillis());
            finishExecutableNode(node);
        } else if (nextNodeStatus == ExecuteStatus.SKIPPED) {
            logger.info("Skipping disabled job '" + node.getId() + "'.");
            node.skipNode(System.currentTimeMillis());
            finishExecutableNode(node);
        } else if (nextNodeStatus == ExecuteStatus.READY) {
            if (node instanceof ExecutableFlow) {
                ExecutableFlow flow = ((ExecutableFlow) node);
                logger.info("Running flow '" + flow.getNestedId() + "'.");
                flow.setStatus(ExecuteStatus.RUNNING);
                flow.setStartTime(System.currentTimeMillis());
                prepareJobProperties(flow);

                for (String startNodeId : ((ExecutableFlow) node).getStartNodes()) {
                    ExecutableNode startNode = flow.getExecutableNode(startNodeId);
                    runReadyJob(startNode);
                }
            } else {
                runExecutableNode(node);
            }
        }
        return true;
    }

    private void runExecutableNode(ExecutableNode node) throws IOException {
        // Collect output props from the job's dependencies.
        prepareJobProperties(node);

        node.setStatus(ExecuteStatus.QUEUED);
        HjobRunner runner = createJobRunner(node);
        logger.info("Submitting job '" + node.getNestedId() + "' to run.");
        try {
            executorService.submit(runner);
            activeJobRunners.add(runner);
        } catch (RejectedExecutionException e) {
            logger.error("Execute node job error!", e);
        }
    }

    //TODO set node properties
    private void prepareJobProperties(ExecutableNode node) throws IOException {
        if (node instanceof ExecutableFlow) {
            return;
        }

        Props props = null;
        // 1. Shared properties (i.e. *.properties) for the jobs only. This takes
        // the
        // least precedence
        /*if (!(node instanceof ExecutableFlow)) {
            String sharedProps = node.getPropsSource();
            if (sharedProps != null) {
                props = this.sharedProps.get(sharedProps);
            }
        }

        // The following is the hiearchical ordering of dependency resolution
        // 2. Parent Flow Properties
        ExecutableFlow parentFlow = node.getParentFlow();
        if (parentFlow != null) {
            Props flowProps = Props.clone(parentFlow.getInputProps());
            flowProps.setEarliestAncestor(props);
            props = flowProps;
        }

        // 3. Output Properties. The call creates a clone, so we can overwrite it.
        Props outputProps = collectOutputProps(node);
        if (outputProps != null) {
            outputProps.setEarliestAncestor(props);
            props = outputProps;
        }

        // 4. The job source.
        Props jobSource = loadJobProps(node);
        if (jobSource != null) {
            jobSource.setParent(props);
            props = jobSource;
        }*/

        node.setInputProps(node.getInputProps());
    }

    private HjobRunner createJobRunner(ExecutableNode node) {
        HjobRunner jobRunner =
                new HjobRunner(node, hjobTypeManager);

        return jobRunner;
    }

    private void retryAllFailures() throws IOException {
        logger.info("Restarting all failed jobs");

        this.retryFailedJobs = false;
        this.flowKilled = false;
        this.flowFailed = false;
        this.flow.setStatus(ExecuteStatus.RUNNING);

        ArrayList<ExecutableNode> retryJobs = new ArrayList<ExecutableNode>();
        resetFailedState(this.flow, retryJobs);

        for (ExecutableNode node : retryJobs) {
            if (node.getStatus() == ExecuteStatus.READY
                    || node.getStatus() == ExecuteStatus.DISABLED) {
                runReadyJob(node);
            } else if (node.getStatus() == ExecuteStatus.SUCCEEDED) {
                for (String outNodeId : node.getOutNodes()) {
                    ExecutableFlow base = node.getParentFlow();
                    runReadyJob(base.getExecutableNode(outNodeId));
                }
            }

            runReadyJob(node);
        }

        updateFlow();
    }

    private void resetFailedState(ExecutableFlow flow,
                                  List<ExecutableNode> nodesToRetry) {
        // bottom up
        LinkedList<ExecutableNode> queue = new LinkedList<ExecutableNode>();
        for (String id : flow.getEndNodes()) {
            ExecutableNode node = flow.getExecutableNode(id);
            queue.add(node);
        }

        long maxStartTime = -1;
        while (!queue.isEmpty()) {
            ExecutableNode node = queue.poll();
            ExecuteStatus oldStatus = node.getStatus();
            maxStartTime = Math.max(node.getStartTime(), maxStartTime);

            long currentTime = System.currentTimeMillis();
            if (node.getStatus() == ExecuteStatus.SUCCEEDED) {
                // This is a candidate parent for restart
                nodesToRetry.add(node);
                continue;
            } else if (node.getStatus() == ExecuteStatus.RUNNING) {
                continue;
            } else if (node.getStatus() == ExecuteStatus.SKIPPED) {
                node.setStatus(ExecuteStatus.DISABLED);
                node.setEndTime(-1);
                node.setStartTime(-1);
                node.setUpdateTime(currentTime);
            } else if (node instanceof ExecutableFlow) {
                ExecutableFlow base = (ExecutableFlow) node;
                switch (base.getStatus()) {
                    case CANCELLED:
                        node.setStatus(ExecuteStatus.READY);
                        node.setEndTime(-1);
                        node.setStartTime(-1);
                        node.setUpdateTime(currentTime);
                        // Break out of the switch. We'll reset the flow just like a normal
                        // node
                        break;
                    case KILLED:
                    case FAILED:
                    case FAILED_FINISHING:
                        resetFailedState(base, nodesToRetry);
                        continue;
                    default:
                        // Continue the while loop. If the job is in a finished state that's
                        // not
                        // a failure, we don't want to reset the job.
                        continue;
                }
            } else if (node.getStatus() == ExecuteStatus.CANCELLED) {
                // Not a flow, but killed
                node.setStatus(ExecuteStatus.READY);
                node.setStartTime(-1);
                node.setEndTime(-1);
                node.setUpdateTime(currentTime);
            } else if (node.getStatus() == ExecuteStatus.FAILED
                    || node.getStatus() == ExecuteStatus.KILLED) {
                nodesToRetry.add(node);
            }

            if (!(node instanceof ExecutableFlow)
                    && node.getStatus() != oldStatus) {
                logger.info("Resetting job '" + node.getNestedId() + "' from "
                        + oldStatus + " to " + node.getStatus());
            }

            for (String inId : node.getInNodes()) {
                ExecutableNode nodeUp = flow.getExecutableNode(inId);
                queue.add(nodeUp);
            }
        }


        // At this point, the following code will reset the flow
        ExecuteStatus oldFlowState = flow.getStatus();
        if (maxStartTime == -1) {
            // Nothing has run inside the flow, so we assume the flow hasn't even
            // started running yet.
            flow.setStatus(ExecuteStatus.READY);
        } else {
            flow.setStatus(ExecuteStatus.RUNNING);

            // Add any READY start nodes. Usually it means the flow started, but the
            // start node has not.
            for (String id : flow.getStartNodes()) {
                ExecutableNode node = flow.getExecutableNode(id);
                if (node.getStatus() == ExecuteStatus.READY
                        || node.getStatus() == ExecuteStatus.DISABLED) {
                    nodesToRetry.add(node);
                }
            }
        }
        flow.setUpdateTime(System.currentTimeMillis());
        flow.setEndTime(-1);
        logger.info("Resetting flow '" + flow.getNestedId() + "' from "
                + oldFlowState + " to " + flow.getStatus());
    }

    /**
     * Determines what the state of the next node should be. Returns null if the
     * node should not be run.
     *
     * @param node
     * @return
     */
    public ExecuteStatus getImpliedStatus(ExecutableNode node) {
        // If it's running or finished with 'SUCCEEDED', than don't even
        // bother starting this job.
        if (ExecuteStatus.isStatusRunning(node.getStatus())
                || node.getStatus() == ExecuteStatus.SUCCEEDED) {
            return null;
        }

        // Go through the node's dependencies. If all of the previous job's
        // statuses is finished and not FAILED or KILLED, than we can safely
        // run this job.
        ExecutableFlow flow = node.getParentFlow();
        boolean shouldKill = false;
        for (String dependency : node.getInNodes()) {
            ExecutableNode dependencyNode = flow.getExecutableNode(dependency);
            ExecuteStatus depStatus = dependencyNode.getStatus();

            if (!ExecuteStatus.isStatusFinished(depStatus)) {
                return null;
            } else if (depStatus == ExecuteStatus.FAILED || depStatus == ExecuteStatus.CANCELLED
                    || depStatus == ExecuteStatus.KILLED) {
                // We propagate failures as KILLED states.
                shouldKill = true;
            }
        }

        // If it's disabled but ready to run, we want to make sure it continues
        // being disabled.
        if (node.getStatus() == ExecuteStatus.DISABLED
                || node.getStatus() == ExecuteStatus.SKIPPED) {
            return ExecuteStatus.SKIPPED;
        }

        // If the flow has failed, and we want to finish only the currently running
        // jobs, we just
        // kill everything else. We also kill, if the flow has been cancelled.
        if (flowFailed
                && failureAction == ExecutionOptions.FailureAction.FINISH_CURRENTLY_RUNNING) {
            return ExecuteStatus.CANCELLED;
        } else if (shouldKill || isKilled()) {
            return ExecuteStatus.CANCELLED;
        }

        // All good to go, ready to run.
        return ExecuteStatus.READY;
    }

    private void finishExecutableNode(ExecutableNode node) {
        finishedNodes.add(node);
        EventData eventData = new EventData(node.getStatus(), node.getNestedId());
        fireEventListeners(Event.create(this, Event.Type.JOB_FINISHED, eventData));
    }

    public boolean isKilled() {
        return flowKilled;
    }

    public ExecutableFlow getExecutableFlow() {
        return flow;
    }
}

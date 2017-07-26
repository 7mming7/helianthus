package com.ha.executor;

import com.ha.utils.TypedMapWrapper;

import java.util.*;

/**
 * Execution options for submitted flows and scheduled flows
 * User: shuiqing
 * DateTime: 17/7/20 下午4:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ExecutionOptions {
    public static final String CONCURRENT_OPTION_SKIP = "skip";
    public static final String CONCURRENT_OPTION_PIPELINE = "pipeline";
    public static final String CONCURRENT_OPTION_IGNORE = "ignore";
    public static final String FLOW_PRIORITY = "flowPriority";
    /* override dispatcher selection and use executor id specified */
    public static final String USE_EXECUTOR = "useExecutor";
    public static final int DEFAULT_FLOW_PRIORITY = 5;

    private static final String FLOW_PARAMETERS = "flowParameters";
    private static final String FAILURE_ACTION = "failureAction";
    private static final String PIPELINE_LEVEL = "pipelineLevel";
    private static final String PIPELINE_EXECID = "pipelineExecId";
    private static final String QUEUE_LEVEL = "queueLevel";
    private static final String CONCURRENT_OPTION = "concurrentOption";
    private static final String DISABLE = "disabled";

    private Integer pipelineLevel = null;
    private Integer pipelineExecId = null;
    private Integer queueLevel = 0;
    private String concurrentOption = CONCURRENT_OPTION_IGNORE;
    private Map<String, String> flowParameters = new HashMap<String, String>();

    public enum FailureAction {
        FINISH_CURRENTLY_RUNNING, CANCEL_ALL, FINISH_ALL_POSSIBLE
    }

    private FailureAction failureAction = FailureAction.FINISH_CURRENTLY_RUNNING;

    private List<Object> initiallyDisabledJobs = new ArrayList<Object>();

    public void addAllFlowParameters(Map<String, String> flowParam) {
        flowParameters.putAll(flowParam);
    }

    public Map<String, String> getFlowParameters() {
        return flowParameters;
    }

    public FailureAction getFailureAction() {
        return failureAction;
    }

    public void setFailureAction(FailureAction action) {
        failureAction = action;
    }

    public void setConcurrentOption(String concurrentOption) {
        this.concurrentOption = concurrentOption;
    }

    public String getConcurrentOption() {
        return concurrentOption;
    }

    public Integer getPipelineLevel() {
        return pipelineLevel;
    }

    public Integer getPipelineExecutionId() {
        return pipelineExecId;
    }

    public void setPipelineLevel(Integer level) {
        pipelineLevel = level;
    }

    public void setPipelineExecutionId(Integer id) {
        this.pipelineExecId = id;
    }

    public Integer getQueueLevel() {
        return queueLevel;
    }

    public List<Object> getDisabledJobs() {
        return new ArrayList<Object>(initiallyDisabledJobs);
    }

    public void setDisabledJobs(List<Object> disabledJobs) {
        initiallyDisabledJobs = disabledJobs;
    }

    public Map<String, Object> toObject() {
        HashMap<String, Object> flowOptionObj = new HashMap<String, Object>();

        flowOptionObj.put(FLOW_PARAMETERS, this.flowParameters);
        flowOptionObj.put(FAILURE_ACTION, failureAction.toString());
        flowOptionObj.put(PIPELINE_LEVEL, pipelineLevel);
        flowOptionObj.put(PIPELINE_EXECID, pipelineExecId);
        flowOptionObj.put(QUEUE_LEVEL, queueLevel);
        flowOptionObj.put(CONCURRENT_OPTION, concurrentOption);
        flowOptionObj.put(DISABLE, initiallyDisabledJobs);
        return flowOptionObj;
    }

    @SuppressWarnings("unchecked")
    public static ExecutionOptions createFromObject(Object obj) {
        if (obj == null || !(obj instanceof Map)) {
            return null;
        }

        Map<String, Object> optionsMap = (Map<String, Object>) obj;
        TypedMapWrapper<String, Object> wrapper =
                new TypedMapWrapper<String, Object>(optionsMap);

        ExecutionOptions options = new ExecutionOptions();
        if (optionsMap.containsKey(FLOW_PARAMETERS)) {
            options.flowParameters = new HashMap<String, String>();
            options.flowParameters.putAll(wrapper
                    .<String, String> getMap(FLOW_PARAMETERS));
        }
        options.concurrentOption =
                wrapper.getString(CONCURRENT_OPTION, options.concurrentOption);

        if (wrapper.containsKey(DISABLE)) {
            options.initiallyDisabledJobs = wrapper.getList(DISABLE);
        }

        // Failure action
        options.failureAction =
                FailureAction.valueOf(wrapper.getString(FAILURE_ACTION,
                        options.failureAction.toString()));
        options.pipelineLevel =
                wrapper.getInt(PIPELINE_LEVEL, options.pipelineLevel);
        options.pipelineExecId =
                wrapper.getInt(PIPELINE_EXECID, options.pipelineExecId);
        options.queueLevel = wrapper.getInt(QUEUE_LEVEL, options.queueLevel);

        return options;
    }
}

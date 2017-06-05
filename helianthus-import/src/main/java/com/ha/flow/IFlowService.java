package com.ha.flow;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午5:45
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IFlowService {

    List<Flow> fetchAllFlows();

    List<Flow> fetchAllActiveFlows();

    List<Flow> fetchAllActiveFlowsByProjectId(Long projectId);
}

package com.ha.job;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午6:01
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IJobService {

    List<Job> fetchAllJobs();

    List<Job> fetchAllActiveJobs();

    List<Job> fetchAllActiveJobsByProjectId(Long projectId);

    List<Job> fetchAllActiveJobsByFlowId(Long flowId);
}

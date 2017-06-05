package com.ha.job;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
@Service
@Getter
@Setter
public class IJobServiceImpl implements IJobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public List<Job> fetchAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public List<Job> fetchAllActiveJobs() {
        return jobRepository.fetchActiveJobs();
    }

    @Override
    public List<Job> fetchAllActiveJobsByProjectId(Long projectId) {
        return jobRepository.fetchActiveJobsByProjectId(projectId);
    }

    @Override
    public List<Job> fetchAllActiveJobsByFlowId(Long flowId) {
        return jobRepository.fetchActiveJobsByFlowId(flowId);
    }
}

package com.ha.flow;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午5:46
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
@Service
public class IFlowServiceImpl implements IFlowService {

    @Autowired
    private FlowRepository flowRepository;

    @Override
    public List<Flow> fetchAllFlows() {
        return flowRepository.findAll();
    }

    @Override
    public List<Flow> fetchAllActiveFlows() {
        return flowRepository.fetchActiveFlows();
    }

    @Override
    public List<Flow> fetchAllActiveFlowsByProjectId(Long projectId) {
        return flowRepository.fetchActiveFlowsByProjectId(projectId);
    }
}

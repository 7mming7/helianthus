package com.ha.flow;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午5:40
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Repository
public interface FlowRepository extends JpaRepository<Flow, String> {

    @Query("select p from Flow p where active = 1")
    List<Flow> fetchActiveFlows();

    @Query("select p from Flow p where active = 1 and projectId = ?1")
    List<Flow> fetchActiveFlowsByProjectId(Long projectId);
}

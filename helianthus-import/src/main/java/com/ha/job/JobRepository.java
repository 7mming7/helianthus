package com.ha.job;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午5:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Repository
public interface JobRepository extends JpaRepository<Job, String> {

    @Query("select p from Job p where active = 1")
    List<Job> fetchActiveJobs();

    @Query("select p from Job p where active = 1 and projectId = ?1")
    List<Job> fetchActiveJobsByProjectId(Long projectId);

    @Query("select p from Job p where active = 1 and flowId = ?1")
    List<Job> fetchActiveJobsByFlowId(Long flowId);
}

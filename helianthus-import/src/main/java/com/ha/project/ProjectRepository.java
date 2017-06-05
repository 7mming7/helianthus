package com.ha.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午5:08
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Repository
public interface ProjectRepository  extends CrudRepository<Project, String>  {

    @Query("select p from Project p where active = 1")
    List<Project> fetchActiveProjects();

    Page<Project> findAll(Pageable pageable);
}

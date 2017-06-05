package com.ha.project;

import com.ha.exception.ProjectManagerException;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/1 下午4:56
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IProjectService {

    Page<Project> fetchAllProjects(int pageIndex, int pageSize) throws ProjectManagerException;

    /**
     * 获取所有active的project
     *
     * @return
     * @throws ProjectManagerException
     */
    List<Project> fetchAllActiveProjects() throws ProjectManagerException;

    /**
     * 根据project id获取project
     *
     * @param id
     * @return
     * @throws ProjectManagerException
     */
    Project fetchProjectById(String id) throws ProjectManagerException;
}

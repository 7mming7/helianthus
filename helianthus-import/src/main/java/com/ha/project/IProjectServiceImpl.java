package com.ha.project;

import com.ha.exception.ProjectManagerException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
@Getter
@Setter
@Service
public class IProjectServiceImpl implements IProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Override
    public Page<Project> fetchAllProjects(int pageIndex, int pageSize) throws ProjectManagerException {
        return projectRepository.findAll(new PageRequest(pageIndex,pageSize));
    }

    public List<Project> fetchAllActiveProjects() throws ProjectManagerException {
        return projectRepository.fetchActiveProjects();
    };

    public Project fetchProjectById(String id) throws ProjectManagerException{
        return projectRepository.findOne(id);
    };
}

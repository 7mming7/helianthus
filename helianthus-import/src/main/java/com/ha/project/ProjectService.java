package com.ha.project;

import com.ha.base.BaseService;
import com.ha.exception.ProjectManagerException;
import com.ha.inject.annotation.BaseComponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/6 上午10:15
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
public class ProjectService extends BaseService<Project, Long> {

    @BaseComponent
    @Autowired
    private ProjectRepository projectRepository;

    public Page<Project> fetchAllProjects(int pageIndex, int pageSize) throws ProjectManagerException {
        return projectRepository.findAll(new PageRequest(pageIndex,pageSize));
    }

    public Object deleteByIds(List<String> ids){
        for(String id:ids){
            delete(Long.parseLong(id));
        }
        return true;
    }
}

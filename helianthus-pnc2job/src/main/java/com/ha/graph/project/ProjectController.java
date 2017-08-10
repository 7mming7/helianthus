package com.ha.graph.project;

import com.ha.entity.search.MatchType;
import com.ha.entity.search.Searchable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: shuiqing
 * DateTime: 17/6/2 下午4:06
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
@Slf4j
@Setter
@Getter
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/projectManagement")
    public ModelAndView projectManagement() {
        return new ModelAndView("project_management");
    }

    @RequestMapping(value = "/queryPageProjects", method = RequestMethod.GET)
    @ResponseBody
    public Object queryPageProjects(int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        Page<Project> data = projectService.fetchAllProjects(pageIndex,pageSize);
        result.put("rows", data.getContent());
        result.put("total", data.getTotalElements());
        return result;
    }

    @RequestMapping(value = "/addAndUpdateProject", method = RequestMethod.POST)
    @ResponseBody
    public Object addProject(Project project) {
        return projectService.saveAndFlush(project);
    }

    @RequestMapping(value = "/deleteProjects", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteProjects(@RequestBody List<String> deleteIds) {
        Assert.notNull(deleteIds);
        Searchable searchable = Searchable.newSearchable().addSearchFilter("id", MatchType.IN, deleteIds);
        projectService.deleteInBatch(projectService.findAllWithNoPageNoSort(searchable));
        return true;
    }
}
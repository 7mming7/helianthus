package com.ha.graph.flow;

import com.ha.entity.search.MatchType;
import com.ha.entity.search.Searchable;
import com.ha.graph.project.Project;
import com.ha.graph.project.ProjectService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: shuiqing
 * DateTime: 17/6/21 下午3:56
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
@Slf4j
@Getter
@Setter
public class FlowController {

    @Autowired
    private FlowService flowService;

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/flowManagement")
    public ModelAndView flowManagement() {
        return new ModelAndView("flow_management");
    }

    @RequestMapping(value = "/queryPageFlows", method = RequestMethod.GET)
    @ResponseBody
    public Object queryPageFlows(int pageIndex, int pageSize) {
        List<FlowDto> flowDtoList = new ArrayList<FlowDto>();
        Map<String, Object> result = new HashMap<String, Object>();
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(pageIndex,pageSize);
        Page<Flow> data = flowService.findAll(searchable);
        for(Flow flow:data.getContent()){
            flowDtoList.add(new FlowDto(flow));
        }
        result.put("rows", flowDtoList);
        result.put("total", data.getTotalElements());
        return result;
    }

    @RequestMapping(value = "/queryProjects", method = RequestMethod.GET)
    @ResponseBody
    public Object queryProjects() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Project> projectList = projectService.findAll();
        result.put("projectList",projectList);
        result.put("total",projectList.size());
        return result;
    }

    @RequestMapping(value = "/queryFlows", method = RequestMethod.GET)
    @ResponseBody
    public Object queryFlows() {
        Map<String, Object> result = new HashMap<String, Object>();
        List<Flow> flowList = flowService.findAll();
        result.put("flowList",flowList);
        result.put("total",flowList.size());
        return result;
    }

    @RequestMapping(value = "/addAndUpdateFlow", method = RequestMethod.POST)
    @ResponseBody
    public Object addFlow(String projectId,Flow flow) {
        Project project = projectService.findOne(Long.parseLong(projectId));
        flow.setProject(project);
        return flowService.saveAndFlush(flow);
    }

    @RequestMapping(value = "/deleteFlows", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteFlows(@RequestBody List<String> deleteIds) {
        Assert.notNull(deleteIds);
        Searchable searchable = Searchable.newSearchable().addSearchFilter("id", MatchType.IN, deleteIds);
        flowService.deleteInBatch(flowService.findAllWithNoPageNoSort(searchable));
        return true;
    }
}

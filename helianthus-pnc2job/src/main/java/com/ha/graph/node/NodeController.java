package com.ha.graph.node;

import com.ha.base.result.Result;
import com.ha.entity.search.Searchable;
import com.ha.execapp.HjobRunner;
import com.ha.executor.ExecutableNode;
import com.ha.executor.ExecuteStatus;
import com.ha.graph.flow.Flow;
import com.ha.graph.flow.FlowService;
import com.ha.graph.project.Project;
import com.ha.graph.project.ProjectService;
import com.ha.hjob.hjobType.HjobTypeManager;
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
 * DateTime: 17/6/7 下午2:28
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
@Getter
@Setter
@Slf4j
public class NodeController {

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private HjobTypeManager hjobTypeManager;

    @RequestMapping("/nodeManagement")
    public ModelAndView nodeManagement() {
        return new ModelAndView("node_management");
    }

    @RequestMapping(value = "/queryPageNodes", method = RequestMethod.GET)
    @ResponseBody
    public Object queryPageNodes(int pageIndex, int pageSize) {
        List<NodeDto> nodeDtoList = new ArrayList<NodeDto>();
        Map<String, Object> result = new HashMap<String, Object>();
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(pageIndex,pageSize);
        Page<Node> data = nodeService.findAll(searchable);
        for(Node node:data){
            nodeDtoList.add(new NodeDto(node));
        }
        result.put("rows", nodeDtoList);
        result.put("total", data.getTotalElements());
        return result;
    }

    @RequestMapping(value = "/addAndUpdateNode", method = RequestMethod.POST)
    @ResponseBody
    public Object addNode(String projectId,String flowId,Node node) {
        Project project = projectService.findOne(Long.parseLong(projectId));
        Flow flow = flowService.findOne(Long.parseLong(flowId));
        node.setProject(project);
        node.setFlow(flow);
        return nodeService.saveAndFlush(node);
    }

    @RequestMapping(value = "/deleteNodes", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteFlows(@RequestBody List<String> deleteIds) {
        Assert.notNull(deleteIds);
        return nodeService.deleteByIds(deleteIds);
    }

    @RequestMapping(value = "/executeSingleNode", method = RequestMethod.POST)
    @ResponseBody
    public Object executeSingleNode(@RequestBody Map<String,String> nodeMap) {
        Node node = nodeService.findOne(Long.parseLong(nodeMap.get("nodeId")));
        log.error("Start execute node:" + node.toString());

        ExecutableNode executableNode = new ExecutableNode(node);

        HjobRunner hjobRunner = new HjobRunner(executableNode, hjobTypeManager);
        hjobRunner.run();

        Result result = new Result();
        result.setSuccess(true);
        result.setMsg("Execute node:" + node.getName() + " Successed!");
        return result;
    }
}

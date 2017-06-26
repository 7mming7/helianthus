package com.ha.node;

import com.ha.entity.search.Searchable;
import com.ha.flow.Flow;
import com.ha.flow.FlowService;
import com.ha.project.Project;
import com.ha.project.ProjectService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
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
}

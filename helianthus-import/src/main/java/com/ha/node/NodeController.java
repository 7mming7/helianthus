package com.ha.node;

import com.ha.entity.search.Searchable;
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

import java.util.HashMap;
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

    @RequestMapping("/nodeManagement")
    public ModelAndView nodeManagement() {
        return new ModelAndView("node_management");
    }

    @RequestMapping(value = "/queryPageNodes", method = RequestMethod.GET)
    @ResponseBody
    public Object queryPageNodes(int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(pageIndex,pageSize);
        Page<Node> data = nodeService.findAll(searchable);
        result.put("rows", data.getContent());
        result.put("total", data.getTotalElements());
        return result;
    }

    @RequestMapping(value = "/addAndUpdateNode", method = RequestMethod.POST)
    @ResponseBody
    public Object addNode(Node node) {
        return nodeService.saveAndFlush(node);
    }
}

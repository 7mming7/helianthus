package com.ha.project;

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
    private IProjectService iProjectService;

    @RequestMapping("/projectManagement")
    public ModelAndView projectManagement() {
        return new ModelAndView("project_management");
    }

    @RequestMapping(value = "/queryPageProjects", method = RequestMethod.GET)
    @ResponseBody
    public Object queryPageProjects(int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        Page<Project> data = iProjectService.fetchAllProjects(pageIndex,pageSize);
        result.put("rows", data.getContent());
        result.put("total", data.getTotalElements());
        return result;
    }
}
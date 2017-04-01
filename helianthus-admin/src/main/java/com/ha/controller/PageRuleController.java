package com.ha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 页面规则配置
 * User: shuiqing
 * DateTime: 16/8/29 下午10:45
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class PageRuleController {

    private static Logger LOG = LoggerFactory.getLogger(PageRuleController.class);

    @RequestMapping("/pageRuleManagement")
    public ModelAndView pageRuleManagement() {
        return new ModelAndView("rule_management");
    }

    @RequestMapping(value = "/addPageInfo", method = RequestMethod.GET)
    public ModelAndView addPageInfo() {
        return new ModelAndView("add_page_info");
    }
}

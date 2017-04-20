package com.ha.base.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 首页controller
 * User: shuiqing
 * DateTime: 16/8/18 下午1:55
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class HomeController {

    private static Logger LOG = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/index")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
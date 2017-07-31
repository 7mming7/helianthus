package com.ha.system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 系统管理controller
 * User: shuiqing
 * DateTime: 17/3/29 下午3:47
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class SystemController {

    private static Logger LOG = LoggerFactory.getLogger(SystemController.class);

    @RequestMapping("/dashbord")
    public ModelAndView login() {
        return new ModelAndView("index");
    }

}

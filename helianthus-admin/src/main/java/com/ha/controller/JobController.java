package com.ha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * 调度任务控制器
 * User: shuiqing
 * DateTime: 16/8/23 下午4:54
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class JobController {

    @RequestMapping("/itemQueueManagement")
    public ModelAndView itemQueueManagement() {
        ModelAndView modelAndView = new ModelAndView("itemqueue_management");
        modelAndView.addObject("queue", "waiting");
        return modelAndView;
    }
}

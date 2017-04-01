package com.ha.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 计划任务controller
 * User: shuiqing
 * DateTime: 17/3/29 下午3:42
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class SchedulingJobController {

    private static Logger LOG = LoggerFactory.getLogger(SchedulingJobController.class);

    @RequestMapping("/viewJobsConifg")
    public ModelAndView viewJobsConifg() {
        return new ModelAndView("view_jobs_config");
    }
}

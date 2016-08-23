package com.ha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 配置相关
 * User: shuiqing
 * DateTime: 16/8/23 上午11:22
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class ConfigController {

    @RequestMapping("/accountManagement")
    public ModelAndView accountManagement() {
        return new ModelAndView("account_management");
    }
}

package com.ha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 上传controller
 * User: shuiqing
 * DateTime: 16/8/19 上午10:30
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Controller
public class UploadController {

    @RequestMapping("/upload")
     public ModelAndView upload() {
        return new ModelAndView("upload");
    }

    @RequestMapping("/uploadFile")
    public ModelAndView uploadFile(String key,String key2) {
        System.out.println("key:" + key + ";key2:" + key2);
        System.out.println("uuuuuuuuu");
        return new ModelAndView("index");
    }
}

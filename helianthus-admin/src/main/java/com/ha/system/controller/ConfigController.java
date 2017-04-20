package com.ha.system.controller;

import com.ha.system.model.Parameter;
import com.ha.system.service.IParameterService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static Logger LOG = LoggerFactory.getLogger(ConfigController.class);

    @Autowired
    private IParameterService iParameterService;

    public IParameterService getiParameterService() {
        return iParameterService;
    }

    public void setiParameterService(IParameterService iParameterService) {
        this.iParameterService = iParameterService;
    }

    @RequestMapping("/parameterManagement")
    public ModelAndView parameterManagement() {
        return new ModelAndView("parameter_management");
    }

    @RequestMapping(value = "/queryPageParameters", method = RequestMethod.GET)
    @ResponseBody
    public Object queryPageParameters(int pageIndex, int pageSize) {
        Map<String, Object> result = new HashMap<String, Object>();
        Page<Parameter> data = iParameterService.queryParameters(pageIndex, pageSize);
        result.put("rows", data.getContent());
        result.put("total", data.getTotalElements());
        return result;
    }

    @RequestMapping(value = "/addAndUpdateParameter", method = RequestMethod.POST)
    @ResponseBody
    public Object addAndUpdateParameter(Parameter parameter) {
        if (StringUtils.isBlank(parameter.getAttribute())) {
            return false;
        }
        System.out.println(parameter.toString());
        return iParameterService.addOrUpdateParameter(parameter);
    }
}

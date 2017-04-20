package com.ha.system.service.impl;

import com.ha.system.model.Parameter;
import com.ha.system.repository.ParameterRespository;
import com.ha.system.service.IParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * 系统参数service
 * User: shuiqing
 * DateTime: 17/3/29 下午4:02
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Transactional
public class IParameterServiceImpl implements IParameterService{

    @Autowired
    private ParameterRespository parameterRespository;

    public ParameterRespository getParameterRespository() {
        return parameterRespository;
    }

    public void setParameterRespository(ParameterRespository parameterRespository) {
        this.parameterRespository = parameterRespository;
    }

    @Override
    public Page<Parameter> queryParameters(int pageIndex, int pageSize) {
        return parameterRespository.findAll(new PageRequest(pageIndex,pageSize));
    }

    @Override
    public boolean updateParameter(Parameter parameter) {
        return parameterRespository.save(parameter) != null;
    }

    @Override
    public boolean addOrUpdateParameter(Parameter parameter) {
        return parameterRespository.save(parameter) != null;
    }
}

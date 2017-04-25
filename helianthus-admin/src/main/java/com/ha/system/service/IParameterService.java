package com.ha.system.service;

import com.ha.system.domain.Parameter;
import org.springframework.data.domain.Page;

/**
 * User: shuiqing
 * DateTime: 17/3/29 下午4:01
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IParameterService {

    public Page<Parameter> queryParameters(int pageIndex, int pageSize);

    public boolean updateParameter(Parameter parameter);

    public boolean addOrUpdateParameter(Parameter parameter);
}

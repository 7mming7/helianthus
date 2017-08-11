package com.ha.system.service;

import com.ha.base.IBaseService;
import com.ha.system.domain.Dictionary;
import org.springframework.data.domain.Page;

/**
 * User: shuiqing
 * DateTime: 17/8/4 下午3:15
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public interface IDictionaryService extends IBaseService<Dictionary, Long>{

    Dictionary fetchDicByCondition(String tableName, String attribute, String value);
}

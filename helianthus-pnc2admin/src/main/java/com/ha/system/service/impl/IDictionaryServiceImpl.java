package com.ha.system.service.impl;

import com.ha.base.BaseService;
import com.ha.inject.annotation.BaseComponent;
import com.ha.system.domain.Dictionary;
import com.ha.system.repository.DictionaryRespository;
import com.ha.system.service.IDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: shuiqing
 * DateTime: 17/8/4 下午3:16
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
public class IDictionaryServiceImpl extends BaseService<Dictionary, Long> implements IDictionaryService {

    @Autowired
    @BaseComponent
    private DictionaryRespository dictionaryRespository;
}
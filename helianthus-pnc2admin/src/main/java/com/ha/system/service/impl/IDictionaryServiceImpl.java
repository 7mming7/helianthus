package com.ha.system.service.impl;

import com.ha.base.BaseService;
import com.ha.entity.search.MatchType;
import com.ha.entity.search.Searchable;
import com.ha.inject.annotation.BaseComponent;
import com.ha.system.domain.Dictionary;
import com.ha.system.repository.DictionaryRespository;
import com.ha.system.service.IDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Dictionary fetchDicByCondition(String tableName, String attribute, String value) {
        Searchable searchable = Searchable.newSearchable()
                .addSearchFilter("tableName", MatchType.EQ, tableName)
                .addSearchFilter("attribute", MatchType.EQ, attribute)
                .addSearchFilter("value", MatchType.EQ, value);
        Page<Dictionary> dictionaryPage = dictionaryRespository.findAll(searchable);
        if(dictionaryPage.hasContent()){
            return dictionaryPage.getContent().get(0);
        }

        return null;
    }
}
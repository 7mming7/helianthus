package com.ha.system.controller;

import com.ha.entity.search.Searchable;
import com.ha.graph.node.Node;
import com.ha.graph.node.NodeDto;
import com.ha.system.domain.Dictionary;
import com.ha.system.domain.Parameter;
import com.ha.system.dto.DictionaryDto;
import com.ha.system.service.IDictionaryService;
import lombok.Getter;
import lombok.Setter;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: shuiqing
 * DateTime: 17/8/4 下午3:25
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
@Controller
public class DictionaryController {

    private static Logger LOG = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private IDictionaryService iDictionaryService;

    @RequestMapping("/dictionaryManagement")
    public ModelAndView dictionaryManagement() {
        return new ModelAndView("dictionary_management");
    }

    @RequestMapping(value = "/queryPageDictionarys", method = RequestMethod.GET)
    @ResponseBody
    public Object queryPageDictionarys(int pageIndex, int pageSize) {
        List<DictionaryDto> dictionaryDtoList = new ArrayList<DictionaryDto>();
        Map<String, Object> result = new HashMap<String, Object>();
        Searchable searchable = Searchable.newSearchable();
        searchable.setPage(pageIndex,pageSize);
        Page<Dictionary> data = iDictionaryService.findAll(searchable);
        for(Dictionary dic:data){
            dictionaryDtoList.add(new DictionaryDto(dic));
        }
        result.put("rows", dictionaryDtoList);
        result.put("total", data.getTotalElements());
        return result;
    }

    @RequestMapping(value = "/addAndUpdateDictionary", method = RequestMethod.POST)
    @ResponseBody
    public Object addAndUpdateDictionary(Dictionary dictionary) {
        return iDictionaryService.saveAndFlush(dictionary);
    }

}

package com.ha.graph.node;

import com.ha.base.BaseService;
import com.ha.inject.annotation.BaseComponent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/6/7 下午2:27
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Service
@Getter
@Setter
public class NodeService extends BaseService<Node, Long> {

    @BaseComponent
    @Autowired
    private NodeRepository nodeRepository;

    public Object deleteByIds(List<String> ids){
        for(String id:ids){
            delete(Long.parseLong(id));
        }
        return true;
    }

}

package com.ha.node;

import com.ha.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 调度执行任务
 * User: shuiqing
 * DateTime: 17/6/1 下午4:59
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_Node")
@Getter
@Setter
public class Node extends AbstractEntity<Long> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Boolean active = true;

    private Long projectId;

    private Long flowId;

    private String jobType;

    //Node 依赖 ->> 以#分割
    private String depends;
}

package com.ha.graph.node;

import com.ha.entity.AbstractEntity;
import com.ha.graph.flow.Flow;
import com.ha.graph.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
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

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JoinColumn(name="projectId")
    @org.hibernate.annotations.ForeignKey(name="fk_pn_projectId")
    @NotFound(action= NotFoundAction.IGNORE)
    private Project project;

    @ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
    @JoinColumn(name="flowId")
    @org.hibernate.annotations.ForeignKey(name="fk_fn_flowId")
    @NotFound(action= NotFoundAction.IGNORE)
    private Flow flow;

    private String jobType;

    //node level
    private Integer level;

    //Node 依赖 ->> 以#分割
    private String depends;
}

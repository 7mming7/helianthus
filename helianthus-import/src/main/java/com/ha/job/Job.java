package com.ha.job;

import com.ha.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
@Table(name = "t_Job")
@Getter
@Setter
public class Job extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String description;

    private Boolean active = true;

    private Long projectId;

    private Long flowId;

    private Long startTime;

    private Long endTime;

    private String jobType;

    //job 依赖 ->> 以#分割
    private String depends;
}

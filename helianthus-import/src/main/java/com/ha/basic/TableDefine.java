package com.ha.basic;

import com.ha.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 表定义
 * User: shuiqing
 * DateTime: 17/5/23 下午4:09
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_TableDefine")
@Getter
@Setter
public class TableDefine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    @Column(name = "name", columnDefinition = "varchar(100)")
    private String name;

    private String description;

    private JobStatus jobStatus = JobStatus.READY;

    //执行到的日期
    private String execDate;
}

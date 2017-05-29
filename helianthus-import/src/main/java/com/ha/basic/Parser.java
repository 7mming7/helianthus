package com.ha.basic;

import com.ha.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 解析器
 * User: shuiqing
 * DateTime: 17/5/22 下午2:14
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_Parser")
@Getter
@Setter
public class Parser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    @Column(name = "name", columnDefinition = "varchar(100)")
    private String name;

    @NotNull
    private String className;
}
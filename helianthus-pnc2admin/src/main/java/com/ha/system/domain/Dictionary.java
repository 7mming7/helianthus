package com.ha.system.domain;

import com.ha.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 字典对象
 * User: shuiqing
 * DateTime: 17/8/4 下午2:51
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Setter
@Getter
@Entity
@Table(name = "t_Dictionary")
public class Dictionary extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 数据表名
     */
    @Column(name="tableName", nullable=false, length=24)
    private String objType;

    @Column(nullable=false, length=24)
    private String attribute;

    @Column(nullable=false)
    private int value;

    @Column(nullable=false, length=32)
    private String description;

    //1:enable 2：disable
    private int status;

    private Long parentId;

    private String name;

    private String remark;
}

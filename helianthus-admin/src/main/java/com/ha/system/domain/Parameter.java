package com.ha.system.domain;

import com.ha.base.BaseEntity;

import javax.persistence.*;

/**
 * 系统参数
 * User: shuiqing
 * DateTime: 17/3/29 下午3:55
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_parameter")
public class Parameter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    //参数常量名称
    private String attribute;
    //参数描述
    private String description;
    //参数类型
    private String ptype;
    //参数值
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "id:" + this.getId() + ","
                + "attribute:" + this.getAttribute() + ","
                + "description:" + this.getDescription() + ","
                + "value:" + this.getValue() + ","
                + "ptype:" + this.getPtype();
    }
}

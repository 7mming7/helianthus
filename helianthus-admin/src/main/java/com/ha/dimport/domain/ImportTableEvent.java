package com.ha.dimport.domain;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 表导入数据的基类
 * User: shuiqing
 * DateTime: 17/4/27 下午1:36
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@MappedSuperclass
public abstract class ImportTableEvent implements Serializable {

    private String userid;

    private String content;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

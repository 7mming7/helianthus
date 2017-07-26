package com.ha.dimport.domain;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 微信记录
 * User: shuiqing
 * DateTime: 17/4/28 上午10:19
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@MappedSuperclass
public abstract class BillWechatRecordBase implements Serializable {

    private String id_no;

    private String openid;

    private String content;

    public String getId_no() {
        return id_no;
    }

    public void setId_no(String id_no) {
        this.id_no = id_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

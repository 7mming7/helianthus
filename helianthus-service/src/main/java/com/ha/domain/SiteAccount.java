package com.ha.domain;

import com.sun.istack.internal.NotNull;

import javax.persistence.*;

/**
 * 账户信息
 * User: shuiqing
 * DateTime: 16/8/23 下午10:05
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Entity
@Table(name = "t_site_account")
public class SiteAccount extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @NotNull
    @Column(name = "userName", columnDefinition = "varchar(100)")
    private String userName;

    @NotNull
    @Column(name = "password", columnDefinition = "varchar(100)")
    private String passWord;

    @NotNull
    private String domain;

    @NotNull
    @Column(name = "available",insertable = false,columnDefinition = "char default '1'")
    private String  available;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}

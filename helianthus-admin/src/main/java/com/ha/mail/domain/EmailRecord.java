package com.ha.mail.domain;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Calendar;

/**
 * 邮件抽象基础
 * User: shuiqing
 * DateTime: 17/4/24 下午2:28
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@MappedSuperclass
public abstract class EmailRecord implements Serializable{

    //收件人
    private String reciver;
    //主题
    private String subject;
    //正文内容
    private String content;
    //正文类型
    private ContentType contentType;
    //执行时间
    private Calendar execDate;
    //执行结果
    private boolean success;
    //返回消息
    private String msg;

    public EmailRecord(String reciver, String subject, String content, ContentType contentType) {
        this.reciver = reciver;
        this.subject = subject;
        this.content = content;
        this.contentType = contentType;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public Calendar getExecDate() {
        return execDate;
    }

    public void setExecDate(Calendar execDate) {
        this.execDate = execDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

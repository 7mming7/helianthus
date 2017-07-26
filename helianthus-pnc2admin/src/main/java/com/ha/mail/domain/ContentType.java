package com.ha.mail.domain;

/**
 * 邮件内容类型
 * User: shuiqing
 * DateTime: 17/4/24 下午3:31
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum ContentType {

    TEXT(1, "文本类型"),
    HTML(2, "HTML");

    private int value;
    private String name;

    ContentType(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ContentType valueOf(int value){
        switch(value){
            case 2:
                return ContentType.HTML;
            case 1:
                return ContentType.TEXT;
            default:
                return null;
        }
    }
}

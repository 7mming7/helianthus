package com.ha.base;

import org.apache.commons.lang3.StringUtils;

/**
 * Hive 表字段定义
 * User: shuiqing
 * DateTime: 17/4/7 下午4:37
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveField implements Field{

    private String name;
    private String type;
    private String comment;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    public String define() {
        StringBuffer sb = new StringBuffer();
        sb.append("\t").append(getName()).append(" ").append(type);
        if(!StringUtils.isBlank(comment)){
            sb.append(" ").append("COMMENT").append(" '").append(comment).append("'");
        }
        return sb.toString();
    }
}

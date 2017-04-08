package com.ha.base;

import com.ha.util.TableUtils;

/**
 * 字段定义
 * User: shuiqing
 * DateTime: 17/4/7 下午3:49
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class BaseField {

    private String name;
    private String type;
    private String desc;
    private boolean partition; // 是否为分区字段
    private boolean unique; // 唯一字段

    public BaseField(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public BaseField(String name, String type, String desc) {
        super();
        this.name = name;
        this.type = type;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isPartition() {
        return partition;
    }

    public void setPartition(boolean partition) {
        this.partition = partition;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * 得到分区名
     * @return
     */
    public String getPartName(){
        if(this.partition){
            return TableUtils.PARTITION_PREFIX + this.getName();
        }
        return this.getName();
    }

    @Override
    public String toString() {
        return "BaseField [name=" + name + ", type=" + type + ", desc=" + desc
                + "]";
    }
}
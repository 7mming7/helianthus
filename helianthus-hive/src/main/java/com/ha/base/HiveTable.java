package com.ha.base;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Hive 表定义
 * User: shuiqing
 * DateTime: 17/4/7 下午5:18
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveTable implements Table{

    private String name;

    private boolean external;

    private String location;

    private String terminated = "\001"; // 字段分割符

    private String comment;             // 描述

    private boolean compressed = false; // 是否压缩

    private String compressedType;

    private List<Field> fields = new ArrayList<Field>();
    private List<Field> partitionFields = new ArrayList<Field>();


    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isExternal() {
        return external;
    }

    @Override
    public void setExternal(boolean external) {
        this.external = external;
    }

    @Override
    public void addField(Field field) {
        this.fields.add(field);
    }

    @Override
    public List<Field> getFields() {
        return this.fields;
    }

    @Override
    public void addPartitionField(Field field) {
        this.partitionFields.add(field);
    }

    @Override
    public List<Field> getPartitionFields() {
        return this.partitionFields;
    }

    @Override
    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String getLocation() {
        return location;
    }

    @Override
    public String getTerminated() {
        return terminated;
    }

    @Override
    public void setTerminated(String terminated) {
        this.terminated = terminated;
    }

    @Override
    public String getComment() {
        return this.comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     *  STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'
     *	OUTPUTFORMAT 'org.apache.hadoop.hive.ql.io.HiveIgnoreKeyTextOutputFormat';
     * @return
     */
    @Override
    public String define() {
        StringBuffer sb = new StringBuffer();
        sb.append("create");
        if(isExternal()){
            sb.append(" external");
        }
        sb.append(" table ").append(this.name).append("(").append("\n"); // 表名
        for(Field field : fields){ // 字段
            sb.append(field.define()).append(",").append("\n");
        }
        sb.setLength(sb.length() - 2);
        sb.append("\n)").append("\n"); // 字段结束
        if(!StringUtils.isBlank(this.comment)){
            sb.append("COMMENT").append(" ").append("'").append(this.comment).append("'").append("\n");
        }
        if(!this.partitionFields.isEmpty()){ // 分区定义
            sb.append("PARTITIONED BY(");
            for(Field field : fields){
                sb.append(field.define()).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append(")").append("\n");
        }
        sb.append("ROW FORMAT DELIMITED").append("\n");
        if(!StringUtils.isBlank(this.terminated)){
            sb.append("FIELDS TERMINATED BY ").append("'").append(this.terminated).append("'").append("\n"); // 字段分割符
        }

        if(this.compressed && "lzo".equalsIgnoreCase(this.getCompressedType())){
            sb.append("STORED AS INPUTFORMAT 'com.hadoop.mapred.DeprecatedLzoTextInputFormat'").append("\n"); // 存储
        }else{
            sb.append("STORED AS TEXTFILE").append("\n"); // 存储
        }

        if(!StringUtils.isBlank(this.location)){
            sb.append("location ").append("'").append(this.location).append("'");
        }
        sb.append(";\n");
        return sb.toString();
    }

    @Override
    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    @Override
    public void setCompressedType(String type) {
        this.compressedType = type;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public String getCompressedType() {
        return compressedType;
    }
}
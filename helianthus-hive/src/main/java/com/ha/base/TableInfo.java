package com.ha.base;

import java.util.ArrayList;
import java.util.List;

/**
 * hive 表创建信息
 * User: shuiqing
 * DateTime: 17/4/7 下午5:30
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class TableInfo {

    /**
     * 创建了多少表
     */
    private List<String> createTables = new ArrayList<String>();

    /**
     * 删除了多少表
     */
    private List<String> deleteTables = new ArrayList<String>();

    /**
     * 创建失败了多少张表
     */
    private List<String> errorCreates = new ArrayList<String>();

    /**
     * 删除失败了多少张表
     */
    private List<String> errorDeletes = new ArrayList<String>();

    public List<String> getCreateTables() {
        return createTables;
    }

    /**
     * 创建成功之后加入到此列表中
     * @param tableName
     */
    public void addCreateTables(String tableName){
        this.createTables.add(tableName);
    }

    public List<String> getDeleteTables() {
        return deleteTables;
    }

    /**
     * 删除成功之后加入到此列表中
     * @param tableName
     */
    public void addDeleteTables(String tableName) {
        this.deleteTables.add(tableName);
    }

    public List<String> getErrorCreates() {
        return errorCreates;
    }

    /**
     * 创建失败之后加入到此列表中
     * @param tableName
     */
    public void addErrorCreates(String tableName) {
        this.errorCreates.add(tableName);
    }

    public List<String> getErrorDeletes() {
        return errorDeletes;
    }

    /**
     * 删除失败之后加入到此列表中
     * @param tableName
     */
    public void addErrorDeletes(String tableName) {
        this.errorDeletes.add(tableName);
    }

    /**
     *
     * @return
     */
    public int create(){
        return this.createTables.size();
    }

    /**
     * 删除的数量
     * @return
     */
    public int delete(){
        return this.deleteTables.size();
    }

    /**
     * 创建失败
     * @return
     */
    public int errorCreate(){
        return this.errorCreates.size();
    }

    /**
     * 错误的删除
     * @return
     */
    public int errorDelete(){
        return this.errorDeletes.size();
    }

    /**
     * 返回结果信息
     * @return
     */
    public String toInfo(){
        StringBuffer sb = new StringBuffer();
        sb.append("共");
        if(!this.createTables.isEmpty()){
            sb.append("创建 ").append(this.create()).append(" 张表,");
        }
        if(!this.deleteTables.isEmpty()){
            sb.append("删除 ").append(this.delete()).append(" 张表,");
        }
        if(!this.errorCreates.isEmpty()){
            sb.append("创建失败 ").append(this.errorCreate()).append(" 张表:[");
            for(String ec : errorCreates){
                sb.append(ec).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append("],");
        }
        if(!this.errorDeletes.isEmpty()){
            sb.append("删除失败 ").append(this.errorDelete()).append(" 张表:[");
            for(String ed : errorDeletes){
                sb.append(ed).append(",");
            }
            sb.setLength(sb.length() - 1);
            sb.append("],");
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    @Override
    public String toString() {
        return this.toInfo();
    }

    /**
     * 关联其它
     * @param tableInfo
     */
    public void join(TableInfo tableInfo){
        this.createTables.addAll(tableInfo.createTables);
        this.deleteTables.addAll(tableInfo.deleteTables);
        this.errorCreates.addAll(tableInfo.errorCreates);
        this.errorDeletes.addAll(tableInfo.errorDeletes);
    }
}

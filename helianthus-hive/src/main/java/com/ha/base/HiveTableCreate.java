package com.ha.base;

import com.ha.util.TableUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;

/**
 * Hive 表创建
 * User: shuiqing
 * DateTime: 17/4/7 下午5:43
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class HiveTableCreate extends AbstractTableCreate{

    private static final String DATABASE = "CIB_IDA";
    private static final Properties tableConfig = new Properties();

    static{
        try {
            tableConfig.load(TableUtils.getResourceAsStream("/tables/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TableInfo createAll(OutputStream out) throws IOException, URISyntaxException {
        return createAllOrByName(out,null);
    }

    @Override
    public TableInfo deleteAll(OutputStream out) throws IOException, URISyntaxException {
        return deleteAllOrByName(out,null);
    }

    @Override
    public TableInfo create(OutputStream out,String table) throws IOException, URISyntaxException {
        return createAllOrByName(out,table);
    }

    @Override
    public TableInfo delete(OutputStream out,String table) throws IOException, URISyntaxException {
        return deleteAllOrByName(out,table);
    }

    /**
     * 写头部内容
     * @param out
     * @throws IOException
     */
    private static final void writeHeader(OutputStream out,String tn)throws IOException {
        if(StringUtils.isBlank(tn)){
            out.write(("-- 创建" + DATABASE + "数据库\n").getBytes());
            out.write(("create database " + DATABASE + ";\n\n").getBytes());
        }
        out.write(("-- 使用" + DATABASE + "数据库\n").getBytes());
        out.write(("use " + DATABASE + ";\n\n").getBytes());
    }

    /**
     * 创建所有的表或者根据表名创建表
     * @param out
     * @param tn
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private TableInfo createAllOrByName(OutputStream out,String tn) throws IOException, URISyntaxException {

        // 创建database
        writeHeader(out, tn);

        out.write("-- 建表语句\n".getBytes());
        TableInfo tableInfo = new TableInfo();
        File[] files = TableUtils.listTablesFiles();
        for(File file : files){
            Table table = new HiveTable();
            String tableName = TableUtils.getTableNameFromFile(file.getName());
            if(!StringUtils.isBlank(tn)){ // 当表名不为空时，根据表名创建指定的表
                if(!tn.equalsIgnoreCase(tableName)){
                    continue;
                }
            }
            table.setName(tableName);// 表名称
            table.setExternal(true);// 是外部表
            table.setLocation(tableConfig.getProperty("hive.table.location","/user/hive/warehouse/cib.db") + "/" + tableName); // 存储路径
            table.setTerminated(tableConfig.getProperty("hive.table.terminated","\001")); // 分隔符
            table.setComment(tableConfig.getProperty("hive.table." + tableName + ".comment")); // 表的描述

            List<BaseField> bfs = TableUtils.loadBaseField(file);
            for(BaseField bf : bfs){
                HiveField hf = new HiveField();
                if(bf.isPartition()){ // 如果是分区，则得到分区
                    hf.setName(bf.getPartName());
                }else{
                    hf.setName(bf.getName());
                }
                hf.setType("string");
                hf.setComment(bf.getDesc());

                table.addField(hf); // 不管是否分区都会加入到字段中，保证与原来表的一直
                if(bf.isPartition()){
                    table.addPartitionField(hf);
                }
            }
            if(!StringUtils.isBlank(table.getComment())){ // 如果表的描述不为空，则输出表的描述
                out.write(("-- " + table.getComment() + "\n").getBytes());
            }
            String info = table.define();
            out.write(info.getBytes());
            out.write("\n".getBytes());
            tableInfo.addCreateTables(tableName); // 设置已经创建的表
        }
        return tableInfo;
    }

    /**
     * 删除所有的表或者根据表名删除表
     * @param out
     * @param tn
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    private TableInfo deleteAllOrByName(OutputStream out,String tn) throws IOException, URISyntaxException {
        TableInfo tableInfo = new TableInfo();
        File[] files = TableUtils.listTablesFiles();

        out.write(("-- 使用" + DATABASE + "数据库\n").getBytes());
        out.write(("use " + DATABASE + ";\n\n").getBytes());

        for(File file : files){
            Table table = new HiveTable();
            String tableName = TableUtils.getTableNameFromFile(file.getName());
            table.setName(tableName);
            if(!StringUtils.isBlank(tn)){ // 当表名不为空时，根据表名创建指定的表
                if(!tn.equalsIgnoreCase(tableName)){
                    continue;
                }
            }
            table.setComment(tableConfig.getProperty("hive.table." + tableName + ".comment")); // 表的描述

            if(!StringUtils.isBlank(table.getComment())){ // 如果表的描述不为空，则输出表的描述
                out.write(("-- " + table.getComment() + "\n").getBytes());
            }

            tableInfo.addDeleteTables(tableName);
        }
        return tableInfo;
    }
}

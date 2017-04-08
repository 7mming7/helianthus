package com.ha.util;

import com.ha.base.BaseField;
import com.ha.exception.TableConfNotFoundException;
import com.ha.parser.FieldWidthParser;
import com.ha.file.MultiFileNameFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hive 表处理工具类
 * User: shuiqing
 * DateTime: 17/4/7 下午3:50
 * Email: helianthus301@163.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class TableUtils {


    private static Map<String,LinkedHashMap<String, Integer>> mapping;
    private static final String encoding = "utf-8";

    private static final String TABLE_CONF_DIR = "/tables/";
    private static final String DEFINE_SUFFIX = ".cfg";
    //字段name上的分割区分是否分区和唯一主键
    private static final String SPLIT = "===";
    // 分区字段的前缀，分区字段以 "P_" 为前缀
    public static final String PARTITION_PREFIX = "P_";

    private static final List<FieldWidthParser> parsers = new ArrayList<FieldWidthParser>();

    private static final FilenameFilter cfgFileFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(DEFINE_SUFFIX); // 只需要所有以'.cfg'结尾的文件
        }
    };

    /**
     * 从文件名中提取表名（文件后缀不需要关心）
     * 文件名格式如下：
     *   AAAAAAAAAA_I_20170407.txt
     */
    private static final Pattern tableName = Pattern.compile("_[f|i]_\\d{8}$",Pattern.CASE_INSENSITIVE);

    /**
     * 匹配格式如下
     */
    private static final Pattern fullDefine = Pattern.compile("([a-z|0-9|_|-|=]+)\\s+([a-z|0-9]+\\(?[0-9|,]*\\)?)\\s*//\\s*(.+)", Pattern.CASE_INSENSITIVE);

    /**
     * 没有描述的匹配
     */
    private static final Pattern noDescDefine = Pattern.compile("([a-z|0-9|_|-|=]+)\\s+([a-z|0-9]+\\(?[0-9|,]*\\)?)\\s*", Pattern.CASE_INSENSITIVE);

    /**
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static final List<BaseField> loadBaseField(File file) throws IOException{
        return loadBaseField(new FileInputStream(file));
    }

    /**
     * 加载基本的项根据表名
     * @param tableName
     * @return
     * @throws IOException
     */
    public static final List<BaseField> loadBaseField(String tableName) throws IOException{
        URL url = getTableConfig(tableName);
        return loadBaseField(url.openStream());
    }

    /**
     * 得到表的配制地址
     * @param tableName
     * @return
     */
    private static URL getTableConfig(String tableName){
        return getResource(TABLE_CONF_DIR + tableName + DEFINE_SUFFIX);
    }

    /**
     * 定义类型如下：
     *   name char(13) // 名称
     * @param in
     * @return
     * @throws IOException
     */
    public static final List<BaseField> loadBaseField(InputStream in) throws IOException{
        List<BaseField> results = new ArrayList<BaseField>();
        List<String> lines = IOUtils.readLines(in,encoding);

        for(String line : lines){
            if(skipCommentOrNull(line)){
                continue;
            }
            Matcher m =fullDefine.matcher(line);
            String desc = "";
            if(!m.matches()){
                m = noDescDefine.matcher(line);
                if(!m.matches()){
                    throw new IOException("The file " + in + " value " + line + " not support!");
                }
            }else{
                desc = m.group(3); // 解析字段描述
            }
            String name = m.group(1).toUpperCase(); // 解析字段名,全部字段都转换为大写
            String type = m.group(2).toUpperCase(); // 解析字段类型

            boolean part = false;
            boolean unique = false;
            // 如果包含切分，当切分前是PART，则为分区字段，当为UNIQUE，为唯一
            if(name.contains(SPLIT)){
                String[] vs = StringUtils.split(name, SPLIT);
                name = vs[1];
                if("PART".equalsIgnoreCase(vs[0])){
                    part = true;
                }
                if("UNIQUE".equalsIgnoreCase(vs[0])){
                    unique = true;
                }
            }
            BaseField field = new BaseField(name, type, desc);
            field.setPartition(part);
            field.setUnique(unique);
            results.add(field);
        }
        return results;
    }


    /**
     * 加载指定文件的对应关系
     * 每行的数据样式如下：
     *    AAA_ID=CHAR(4)
     *
     * @param in
     * @return
     * @throws IOException
     */
    private static LinkedHashMap<String, Integer> loadFileFixedWidthFields(InputStream in) throws IOException{
        LinkedHashMap<String, Integer> mapping = new LinkedHashMap<String, Integer>();
        List<BaseField> fields = loadBaseField(in);
        for(BaseField bf : fields){
            // 所有的字段都大写，容易比较
            mapping.put(bf.getName().toUpperCase(), parseValue(bf));
        }
        return mapping;
    }

    /**
     * 解析value值转换为长度
     * @param bf
     * @return
     */
    private static int parseValue(BaseField bf) throws UnsupportedOperationException{
        for(FieldWidthParser parser : parsers){
            if(parser.supportWidth(bf)){
                return parser.parseWidth(bf);
            }
        }
        throw new UnsupportedOperationException(bf + " unsupported");
    }

    /**
     * 如果line是空行，或者已 '#' 或者 '//'开头 都会跳过
     * 其中 '#','//' 都是为注解
     * '#' 只能是一行的开始
     * '//' 只能是一行的结尾
     * @param line
     * @return
     */
    private static boolean skipCommentOrNull(String line){
        return StringUtils.isBlank(line) || line.startsWith("#") || line.startsWith("//");
    }

    /**
     * 根据文件名，得到对应的表名
     * @param fileName 文件名
     * @return 返回 表名
     */
    public static final String getTableNameFromFile(String fileName){
        String baseName = fileName;
        while(baseName.contains(".")){
            baseName = FilenameUtils.getBaseName(fileName);
        }
        return tableName.matcher(baseName).replaceFirst("");
    }

    public static final LinkedHashMap<String, Integer> getFixedWidthFields(String tableName) throws IOException, URISyntaxException{
        if(mapping == null){
            mapping = new HashMap<String,LinkedHashMap<String, Integer>>();
        }
        LinkedHashMap<String, Integer> result = mapping.get(tableName);
        if(result == null){
            URL url = getTableConfig(tableName);
            if(url != null){
                LinkedHashMap<String,Integer> fixedWidthFields = loadFileFixedWidthFields(url.openStream());
                mapping.put(getBaseName(url).toUpperCase(), fixedWidthFields);
                result = fixedWidthFields;
            }else{
                throw new TableConfNotFoundException(tableName);
            }
        }
        return result;
    }

    private static final String getBaseName(URL url){
        String u = url.toString();
        int index = u.indexOf("!/");
        if(index != -1){
            u = u.substring(0, index);
        }
        return FilenameUtils.getBaseName(u);
    }

    /**
     * 根据文件名称获取字段定义
     * @param fileName
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public static final LinkedHashMap<String, Integer> getFixedWidthFieldsByFileName(String fileName) throws IOException, URISyntaxException{
        return getFixedWidthFields(getTableNameFromFile(fileName));
    }

    /**
     *
     * @param resource
     * @return
     */
    public static final InputStream getResourceAsStream(String resource){
        return TableUtils.class.getResourceAsStream(resource);
    }

    /**
     *
     * 得到所有指定的资源
     * @param resource
     * @return
     */
    public static final URL getResource(String resource){
        return TableUtils.class.getResource(resource);
    }

    /**
     * 列出所有的table文件
     *
     * @return
     * @throws FileNotFoundException
     * @throws URISyntaxException
     */
    public static final File[] listTablesFiles() throws FileNotFoundException,URISyntaxException{
        URL url = TableUtils.class.getClassLoader().getResource(TABLE_CONF_DIR);
        File tablesDir = new File(url.toURI());
        if(tablesDir.exists()){
            File[] files = tablesDir.listFiles(MultiFileNameFilter.get(cfgFileFilter).addHiddenFileFilter());
            return files;
        }else{
            throw new FileNotFoundException(tablesDir.getAbsolutePath());
        }
    }
}

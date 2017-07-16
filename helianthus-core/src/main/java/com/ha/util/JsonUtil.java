package com.ha.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.ser.std.NullSerializer;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * json和对象转换工具
 * User: shuiqing
 * DateTime: 17/3/27 下午12:00
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class JsonUtil {

    private static Logger log = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.getSerializerProvider().setNullKeySerializer(NullSerializer.instance);
    }

    public static String toJSON(Object obj, boolean prettyPrint) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            if (prettyPrint) {
                ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                return writer.writeValueAsString(obj);
            }
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void toJSON(Object obj, OutputStream stream) {
        toJSON(obj, stream, false);
    }

    public static void toJSON(Object obj, OutputStream stream, boolean prettyPrint) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            if (prettyPrint) {
                ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
                writer.writeValue(stream, obj);
                return;
            }
            mapper.writeValue(stream, obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void toJSON(Object obj, File file) throws IOException {
        toJSON(obj, file, false);
    }

    public static void toJSON(Object obj, File file, boolean prettyPrint)
            throws IOException {
        BufferedOutputStream stream =
                new BufferedOutputStream(new FileOutputStream(file));
        try {
            toJSON(obj, stream, prettyPrint);
        } finally {
            stream.close();
        }
    }

    public static Object parseJSONFromStringQuiet(String json) {
        try {
            return parseJSONFromString(json);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object parseJSONFromString(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createJsonParser(json);
        JsonNode node = mapper.readTree(parser);

        return toObjectFromJSONNode(node);
    }

    public static Object parseJSONFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createJsonParser(file);
        JsonNode node = mapper.readTree(parser);

        return toObjectFromJSONNode(node);
    }

    public static Object parseJSONFromReader(Reader reader) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory factory = new JsonFactory();
        JsonParser parser = factory.createJsonParser(reader);
        JsonNode node = mapper.readTree(parser);

        return toObjectFromJSONNode(node);
    }

    private static Object toObjectFromJSONNode(JsonNode node) {
        if (node.isObject()) {
            HashMap<String, Object> obj = new HashMap<String, Object>();
            Iterator<String> iter = node.getFieldNames();
            while (iter.hasNext()) {
                String fieldName = iter.next();
                JsonNode subNode = node.get(fieldName);
                Object subObj = toObjectFromJSONNode(subNode);
                obj.put(fieldName, subObj);
            }

            return obj;
        } else if (node.isArray()) {
            ArrayList<Object> array = new ArrayList<Object>();
            Iterator<JsonNode> iter = node.getElements();
            while (iter.hasNext()) {
                JsonNode element = iter.next();
                Object subObject = toObjectFromJSONNode(element);
                array.add(subObject);
            }
            return array;
        } else if (node.isTextual()) {
            return node.asText();
        } else if (node.isNumber()) {
            if (node.isInt()) {
                return node.asInt();
            } else if (node.isLong()) {
                return node.asLong();
            } else if (node.isDouble()) {
                return node.asDouble();
            } else {
                System.err.println("ERROR What is this!? " + node.getNumberType());
                return null;
            }
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else {
            return null;
        }
    }

    public static long getLongFromObject(Object obj) {
        if (obj instanceof Integer) {
            return Long.valueOf((Integer) obj);
        }

        return (Long) obj;
    }

    /*
     * Writes json to a stream without using any external dependencies.
     *
     * This is useful for plugins or extensions that want to write properties to a
     * writer without having to import the jackson, or json libraries. The
     * properties are expected to be a map of String keys and String values.
     *
     * The other json writing methods are more robust and will handle more cases.
     */
    public static void writePropsNoJarDependency(Map<String, String> properties,
                                                 Writer writer) throws IOException {
        writer.write("{\n");
        int size = properties.size();

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            // tab the space
            writer.write('\t');
            // Write key
            writer.write(quoteAndClean(entry.getKey()));
            writer.write(':');
            writer.write(quoteAndClean(entry.getValue()));

            size -= 1;
            // Add comma only if it's not the last one
            if (size > 0) {
                writer.write(',');
            }
            writer.write('\n');
        }
        writer.write("}");
    }

    private static String quoteAndClean(String str) {
        if (str == null || str.isEmpty()) {
            return "\"\"";
        }

        StringBuffer buffer = new StringBuffer(str.length());
        buffer.append('"');
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);

            switch (ch) {
                case '\b':
                    buffer.append("\\b");
                    break;
                case '\t':
                    buffer.append("\\t");
                    break;
                case '\n':
                    buffer.append("\\n");
                    break;
                case '\f':
                    buffer.append("\\f");
                    break;
                case '\r':
                    buffer.append("\\r");
                    break;
                case '"':
                case '\\':
                case '/':
                    buffer.append('\\');
                    buffer.append(ch);
                    break;
                default:
                    if (isCharSpecialUnicode(ch)) {
                        buffer.append("\\u");
                        String hexCode = Integer.toHexString(ch);
                        int lengthHexCode = hexCode.length();
                        if (lengthHexCode < 4) {
                            buffer.append("0000".substring(0, 4 - lengthHexCode));
                        }
                        buffer.append(hexCode);
                    } else {
                        buffer.append(ch);
                    }
            }
        }
        buffer.append('"');
        return buffer.toString();
    }

    private static boolean isCharSpecialUnicode(char ch) {
        if (ch < ' ') {
            return true;
        } else if (ch >= '\u0080' && ch < '\u00a0') {
            return true;
        } else if (ch >= '\u2000' && ch < '\u2100') {
            return true;
        }

        return false;
    }

    private static ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * 转换对象为JSON
     * @param obj 待转换对象
     * @return JSON字符串
     */
    public static String toJSON(Object obj) {
        try {
            String value = getObjectMapper().writeValueAsString(obj);
            return value;
        } catch (Exception e) {
            log.error("msg=Fail at toJSON: ", e);
        }
        return "";
    }

    /**
     *  从JSON转换为对象
     * @param <T> 转换的Java类型
     * @param jsonStr JSON字符串
     * @param type 指定类型
     * @return 转换后的对象
     */
    public static <T> T fromJSON(String jsonStr, Class<T> type) {
        T result = null;
        try {
            result = getObjectMapper().readValue(jsonStr, type);
        } catch (Exception e) {
            log.error("msg=Fail at fromJSON: ", e);
        }
        return result;
    }

    /**
     * 从JSON转换为对象
     * @param jsonStr JSON字符串
     * @param javaType 指定类型
     * @return 转换后的对象
     */
    public static <T> T fromJSON(String jsonStr, JavaType javaType) {
        T result = null;
        try {
            result = getObjectMapper().readValue(jsonStr, javaType);
        } catch (Exception e) {
            log.error("msg=Fail at fromJSON: ", e);
        }
        return result;
    }

    /**
     * 功能：对象转为json字符串
     * @param obj
     * @return
     */
    public static String toJson(Object obj){
        return JSONObject.toJSONString(obj);
    }

    /**
     * 功能：json转为对象
     * @param json	json字符串
     * @param cls	类描述
     * @return
     */
    public static <T> T toObject(String json, Class<T> cls){
        T obj = JSONObject.parseObject(json, cls);
        return obj;
    }

    /**
     * 功能：json转为list集合对象
     * @param json
     * @param cls
     * @return
     */
    public static <T> List<T> toObjectList(String json, Class<T> cls){
        List<T> resultList = Lists.newArrayList();
        List list = toObject(json, List.class);
        for(int i = 0; i < list.size(); i++){
            JSONObject obj = (JSONObject)list.get(i);
            resultList.add(JSON.parseObject(obj.toJSONString(), cls));
        }
        return resultList;
    }

    /**
     * 功能：json转为Map集合对象
     * @param json
     * @param cls
     * @return
     */
    public static <T> Map<String, T> toObjectMap(String json, Class<T> cls){
        Map<String, T> resultMap = Maps.newHashMap();
        Map<String, Object> map = toObject(json, Map.class);
        for(String key: map.keySet()){
            JSONObject obj = (JSONObject)map.get(key);
            resultMap.put(key, JSON.parseObject(obj.toJSONString(), cls));
        }
        return resultMap;
    }

    /**
     * 功能：String转为Json
     * @param obj
     * @return
     */
    public static String parsetoJson(Object ...obj)
    {
        Map<String ,Object> mapdata = Maps.newHashMap();
        for(int i=0;i<obj.length;i++){
            mapdata.put(obj[i].getClass().getName(), obj[i]);
        }
        return JsonUtil.toJson(mapdata).toString();
    }
    /**
     * 功能：json转为Map集合对象（String -> long）
     * @param json
     * @return
     */
    public static Map<String, Long> toMapStringLong(String json){
        Map<String, Long> resultMap = Maps.newHashMap();
        Map<String, Long> tmpMap = JsonUtil.toObject(json, Map.class);
        for(String key: tmpMap.keySet()){
            Object v1 = tmpMap.get(key);
            if(v1 instanceof Long){
                resultMap.put(key, (Long) v1);
            } else if(v1 instanceof Integer){
                Integer tmp = (Integer) v1;
                resultMap.put(key, tmp.longValue());
            }
        }
        return resultMap;
    }

    /**
     * 功能：json转为Map集合对象（long -> long）
     * @param json
     * @return
     */
    public static Map<Long, Long> toMapLongLong(String json){
        Map<Long, Long> resultMap = Maps.newHashMap();
        Map<String, Long> tmpMap = JsonUtil.toObject(json, Map.class);
        for(String key: tmpMap.keySet()){
            Object v1 = tmpMap.get(key);
            if(v1 instanceof Long){
                resultMap.put(Long.parseLong(key), (Long) v1);
            } else if(v1 instanceof Integer){
                Integer tmp = (Integer) v1;
                resultMap.put(Long.parseLong(key), tmp.longValue());
            }
        }
        return resultMap;
    }


    public static Map<String, Double> toMapStringDouble(String json){
        Map<String, Double> resultMap = Maps.newHashMap();
        Map<String, Object> tmpMap = JsonUtil.toObject(json, Map.class);
        for(String key: tmpMap.keySet()){
            Object v1 = tmpMap.get(key);
            if(v1 instanceof Long){
                resultMap.put(key, ((Long) v1).doubleValue());
            } else if(v1 instanceof Integer){
                Integer tmp = (Integer) v1;
                resultMap.put(key, tmp.doubleValue());
            }  else if(v1 instanceof Double){
                Double tmp = (Double) v1;
                resultMap.put(key, tmp);
            }
        }
        return resultMap;
    }

    /**
     * 功能：从json中获取<key，value>对数据
     * @param jsonStr
     * @return
     */
    public static Map<String, Object> parseJSON2Map(String jsonStr){
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        JSONObject json = JSONObject.parseObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k);
            if(v == null) continue;
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<Object> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    Object json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }

    /**
     * 功能：从json中获取<key，value>对数据，只解析第一层数据
     * @param jsonStr
     * @return
     */
    public static Map<String, String> parseJSON2MapStr(String jsonStr){
        Map<String, String> map = new HashMap<String, String>();
        //最外层解析
        JSONObject json = JSONObject.parseObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k);
            map.put(k.toString(), v.toString());
        }
        return map;
    }

    public static void main(String[] args) {
        String tmp = "\"searchCondition:{\"company\":\"中国联通代理商\",\"companyText\":\"中国联通\",\"niscohis\":\"0\",\"nisseniordb\":\"0\"}\"";
        System.out.println(tmp.substring(1,tmp.length() -1));
        System.out.println(tmp.indexOf("\\\""));
        System.out.println(tmp);
    }
}

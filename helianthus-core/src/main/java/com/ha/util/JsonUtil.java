package com.ha.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

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
            resultList.add(obj.parseObject(obj.toJSONString(), cls));
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
            resultMap.put(key, obj.parseObject(obj.toJSONString(), cls));
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

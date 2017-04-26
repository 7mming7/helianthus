package com.ha.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 图片缓存工具类
 * User: shuiqing
 * DateTime: 17/4/26 下午3:28
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ImageCacheHelper {

    private static Logger LOG = LoggerFactory.getLogger(ImageCacheHelper.class);

    //图片缓存存储对象，使用ConcurrentHashMap作为存储对象
    private static ConcurrentHashMap<String,byte[]> imageCache = new ConcurrentHashMap<String, byte[]>();

    public static void setImageCache(String cid, byte[] data){
        imageCache.put(cid,data);
    }

    public static byte[] getImageCache(String cid){
        return imageCache.get(cid);
    }
}

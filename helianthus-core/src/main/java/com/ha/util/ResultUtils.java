package com.ha.util;

import com.ha.domain.ListResult;
import com.ha.domain.SingleResult;

import java.util.List;

/**
 * User: shuiqing
 * DateTime: 17/5/5 下午5:03
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ResultUtils {

    /**
     * 创建错误返回结果
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> SingleResult<T> createErrorResult (String msg) {
        SingleResult<T> result = new SingleResult<T>();
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static <T> SingleResult<T> createSuccessResult (String msg, T data) {
        SingleResult<T> result = new SingleResult<T>();
        result.setSuccess(true);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static <T> SingleResult<T> createSuccessResult (T data) {
        SingleResult<T> result = new SingleResult<T>();
        result.setSuccess(true);
        result.setMsg("");
        result.setData(data);
        return result;
    }

    /**
     * 创建错误返回结果
     * @param msg
     * @param <T>
     * @return
     */
    public static <T> ListResult<T> createErrorListResult (String msg) {
        ListResult<T> result = new ListResult<T>();
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    public static <T> ListResult<T> createSuccessListResult (String msg, List<T> dataList) {
        ListResult<T> result = new ListResult<T>();
        result.setSuccess(true);
        result.setMsg(msg);
        result.setDataList(dataList);
        return result;
    }

    public static <T> ListResult<T> createSuccessListResult (List<T> dataList) {
        ListResult<T> result = new ListResult<T>();
        result.setSuccess(true);
        result.setMsg("");
        result.setDataList(dataList);
        return result;
    }
}

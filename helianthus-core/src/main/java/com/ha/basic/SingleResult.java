package com.ha.basic;

/**
 * 返回单一数据
 * User: shuiqing
 * DateTime: 17/5/5 下午3:42
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class SingleResult<T> extends Result {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

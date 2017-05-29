package com.ha.basic;

import java.util.List;

/**
 * 返回集合数据
 * User: shuiqing
 * DateTime: 17/5/5 下午3:46
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class ListResult<T> extends Result {
    private List<T> dataList;

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}

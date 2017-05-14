package com.ha.domain;

/**
 * 返回json的基类
 * User: shuiqing
 * DateTime: 17/5/5 下午3:41
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class Result {

    /**
     * 请求是否成功
     */
    private boolean success;

    /**
     * 错误码
     */
    private Integer errorCode;

    /**
     * 返回的消息
     */
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

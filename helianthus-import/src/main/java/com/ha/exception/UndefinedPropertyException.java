package com.ha.exception;

/**
 * User: shuiqing
 * DateTime: 17/6/27 下午4:13
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
/**
 * Indicates that a required property is missing from the Props
 */
public class UndefinedPropertyException extends RuntimeException {

    private static final long serialVersionUID = 1;

    public UndefinedPropertyException(String message) {
        super(message);
    }
}

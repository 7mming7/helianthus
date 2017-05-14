package com.ha.event.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * 作业执行事件Throwable.
 * User: shuiqing
 * DateTime: 17/5/11 下午4:23
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@ToString(of = "plainText")
public final class JobExecutionEventThrowable {

    private final Throwable throwable;

    private String plainText;
}
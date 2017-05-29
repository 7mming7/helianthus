package com.ha.basic;

import lombok.Getter;
import lombok.Setter;

/**
 * 脚本类型任务
 * User: shuiqing
 * DateTime: 17/5/22 下午4:07
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class ScriptJob implements ElasticJob {

    private String commandPath;
}
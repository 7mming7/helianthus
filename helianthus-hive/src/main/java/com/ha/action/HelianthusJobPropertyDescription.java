package com.ha.action;

/**
 * Description of parameter passed to this class via the Helianthus property to
 * which the annotation is attached.
 * User: shuiqing
 * DateTime: 17/8/14 下午5:20
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */

import java.lang.annotation.Documented;

@Documented
public @interface HelianthusJobPropertyDescription {
    // @TODO: Actually add the value in since it doesn't show up in the
    // javadoc... siargh.
    String value();
}

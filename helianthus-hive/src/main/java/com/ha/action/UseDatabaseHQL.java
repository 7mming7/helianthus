package com.ha.action;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:28
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class UseDatabaseHQL implements HQL {
    private final String database;

    UseDatabaseHQL(String database) {
        this.database = database;
    }

    @Override
    public String toHQL() {
        return "USE " + database + ";";
    }
}

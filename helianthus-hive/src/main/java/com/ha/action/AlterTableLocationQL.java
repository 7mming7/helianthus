package com.ha.action;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:05
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class AlterTableLocationQL implements HQL {

    private final String table;
    private final String newLocation;

    public AlterTableLocationQL(String table, String newLocation) {
        // @TODO: Null checks
        this.table = table;
        this.newLocation = newLocation;
    }

    @Override
    public String toHQL() {
        return "ALTER TABLE " + table + " SET LOCATION '" + newLocation + "';";
    }
}

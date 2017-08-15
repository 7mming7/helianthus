package com.ha.action;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:07
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class DropPartitionHQL implements HQL {
    private final String table;
    private final String partition;
    private final String value;
    private final boolean ifExists;

    DropPartitionHQL(String table, String partition, String value,
                     boolean ifExists) {
        // @TODO: Null checks
        this.table = table;
        this.partition = partition;
        this.value = value;
        this.ifExists = ifExists;
    }

    @Override
    public String toHQL() {
        String exists = ifExists ? "IF EXISTS " : "";

        return "ALTER TABLE " + table + " DROP " + exists + "PARTITION ("
                + partition + "='" + value + "');";
    }
}

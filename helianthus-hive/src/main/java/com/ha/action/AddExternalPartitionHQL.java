package com.ha.action;

/**
 * User: shuiqing
 * DateTime: 17/8/14 下午5:03
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public class AddExternalPartitionHQL implements HQL {

    // ALTER TABLE table_name ADD PARTITION (partCol = 'value1') location 'loc1';
    private final String table;
    private final String partition;
    private final String value;
    private final String location;
    private final boolean ifNotExists;

    public AddExternalPartitionHQL(String table, String partition, String value,
                                   String location, boolean ifNotExists) {
        // @TODO: Null checks
        this.table = table;
        this.partition = partition;
        this.value = value;
        this.location = location;
        this.ifNotExists = ifNotExists;
    }

    @Override
    public String toHQL() {
        String ifNot = ifNotExists ? "IF NOT EXISTS " : "";

        return "ALTER TABLE " + table + " ADD " + ifNot + "PARTITION (" + partition
                + " = '" + value + "') LOCATION '" + location + "';";
    }
}

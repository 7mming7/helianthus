package com.ha.mail.domain;

/**
 * User: shuiqing
 * DateTime: 17/4/24 下午3:39
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum BillType {

    YEAR(1, "年度账单"),
    MONTH(2, "月度账单");

    private int value;
    private String name;

    private BillType(int value, String name){
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static BillType valueOf(int value){
        switch(value){
            case 2:
                return BillType.MONTH;
            case 1:
                return BillType.YEAR;
            default:
                return null;
        }
    }
}

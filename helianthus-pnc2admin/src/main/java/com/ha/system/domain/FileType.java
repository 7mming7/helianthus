package com.ha.system.domain;

/**
 * 文件类型
 * User: shuiqing
 * DateTime: 17/4/25 下午1:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
public enum FileType {

    IMAGE(1),
    VIDEO(2);

    private int value;

    FileType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static FileType valueOf(int value){
        switch(value){
            case 2:
                return FileType.VIDEO;
            case 1:
                return FileType.IMAGE;
            default:
                return null;
        }
    }
}

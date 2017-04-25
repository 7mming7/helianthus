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

    IMAGE(1, "照片"),
    VIDEO(2, "视频");

    private int value;
    private String name;

    private FileType(int value, String name){
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

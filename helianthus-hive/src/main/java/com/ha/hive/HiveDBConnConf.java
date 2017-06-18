package com.ha.hive;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * hive 服务连接配置
 * User: shuiqing
 * DateTime: 17/6/15 下午4:10
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Getter
@Setter
public class HiveDBConnConf {

    public static final String KEY_DRIVER = "driver";
    public static final String KEY_URL = "url";
    public static final String KEY_USER = "user";
    public static final String KEY_PASS = "pass";

    private String driver;
    private String url;
    private String user;
    private String pass;

    public HiveDBConnConf() {
    }

    public HiveDBConnConf(String prefix, PropertiesConfiguration pc) {
        driver = pc.getString(prefix + KEY_DRIVER);
        url = pc.getString(prefix + KEY_URL);
        user = pc.getString(prefix + KEY_USER);
        pass = pc.getString(prefix + KEY_PASS);
    }

    public HiveDBConnConf(String driver, String url, String user, String pass) {
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    public String toString() {
        return String.format("%s,%s,%s,%s", driver, url, user, pass);
    }
}

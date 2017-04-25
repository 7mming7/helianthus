package com.ha.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 邮件配置
 * User: shuiqing
 * DateTime: 17/4/24 下午3:57
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Configuration
public class EmailConfig {

    @Value("${email.transportStrategy}")
    private String transportStrategy;
    @Value("${email.smtpHost}")
    private String smtpHost;
    @Value("${email.smtpPort}")
    private Integer smtpPort;
    @Value("${email.smtpUsername}")
    private String smtpUsername;
    @Value("${email.smtpPassword}")
    private String smtpPassword;
    @Value("${email.defaultPoolsize}")
    private Integer defaultPoolsize;
    @Value("${email.transportModeLoggingOnly}")
    private Boolean transportModeLoggingOnly;

    @Bean(name = "mailer")
    public Mailer configureMailer() {
        ServerConfig serverConfigSMTP = new ServerConfig(smtpHost, smtpPort, smtpUsername, smtpPassword);

        Mailer mailer = new Mailer(serverConfigSMTP, TransportStrategy.valueOf(transportStrategy));
        mailer.setThreadPoolSize(defaultPoolsize);
        mailer.setTransportModeLoggingOnly(transportModeLoggingOnly);

        return mailer;
    }
}

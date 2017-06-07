package com.ha.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManagerFactory;

/**
 * User: shuiqing
 * DateTime: 17/6/7 上午10:26
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Configuration
public class MethodInvokeConfig {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Bean(name = "methodInvokingFactoryBean")
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean mfBean = new MethodInvokingFactoryBean();
        mfBean.setStaticMethod("com.ha.base.repository.RepositoryHelper.setEntityManagerFactory");
        mfBean.setArguments(new Object[] {entityManagerFactory});
        return mfBean;
    }
}
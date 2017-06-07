package com.ha.config;

import com.ha.base.repository.support.SimpleBaseRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

/**
 * JPA 配置
 * User: shuiqing
 * DateTime: 17/6/6 下午11:36
 * Email: annuus.sq@gmail.com
 * GitHub: https://github.com/shuiqing301
 * Blog: http://shuiqing301.github.io/
 * _
 * |_)._ _
 * | o| (_
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.ha",
        repositoryFactoryBeanClass = SimpleBaseRepositoryFactoryBean.class,
        entityManagerFactoryRef="entityManagerFactory",
        transactionManagerRef="transactionManager")
@EnableSpringDataWebSupport
@EnableTransactionManagement
public class RepositoryConfig {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "transactionManager")
    PlatformTransactionManager transactionManagerPrimary(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }

    @Bean(name = "entityManager")
    public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
        return entityManagerFactory(builder).getObject().createEntityManager();
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    @ConditionalOnMissingBean({ LocalContainerEntityManagerFactoryBean.class,
            EntityManagerFactory.class })
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(dataSource)
                .packages("com.ha") //设置实体类所在位置
                .persistenceUnit("persistenceUnit").build();
    }
}

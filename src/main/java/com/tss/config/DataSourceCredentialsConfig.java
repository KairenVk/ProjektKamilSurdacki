package com.tss.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;


import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.tss.repositories.credentials",
        entityManagerFactoryRef = "credentialsEntityManagerFactory",
        transactionManagerRef = "credentialsTransactionManager")
public class DataSourceCredentialsConfig {


    @Bean
    @ConfigurationProperties("spring.datasource.auth")
    public DataSourceProperties credentialsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource.auth.configuration")
    public DataSource credentialsDataSource() {
        return credentialsDataSourceProperties().initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "credentialsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean credentialsEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(credentialsDataSource())
                .packages("com.tss.entities.credentials")
                .build();
    }

    @Bean
    public PlatformTransactionManager credentialsTransactionManager(
            final @Qualifier("credentialsEntityManagerFactory") LocalContainerEntityManagerFactoryBean credentialsEntityManagerFactory) {
        return new JpaTransactionManager(credentialsEntityManagerFactory.getObject());
    }
}

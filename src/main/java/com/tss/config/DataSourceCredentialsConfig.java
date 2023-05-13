package com.tss.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.tss.repositories.credentials",
        entityManagerFactoryRef = "credentialsEntityManagerFactory",
        transactionManagerRef = "credentialsTransactionManager")
public class DataSourceCredentialsConfig {

    @Autowired
    private Environment env;

    @Bean
    @ConfigurationProperties("spring.datasource.auth")
    public DataSourceProperties credentialsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Qualifier("dataSourceAuth")
    @ConfigurationProperties("spring.authdatasource")
    public DataSource dataSourceAuth() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.auth.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.auth.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.auth.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.auth.password"));
        return dataSource;
    }

    @Bean(name = "credentialsEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean credentialsEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSourceAuth())
                .packages("com.tss.entities.credentials")
                .build();
    }

    @Bean(name="credentialsTransactionManager")
    public PlatformTransactionManager credentialsTransactionManager(
            final @Qualifier("credentialsEntityManagerFactory") LocalContainerEntityManagerFactoryBean credentialsEntityManagerFactory) {
        return new JpaTransactionManager(credentialsEntityManagerFactory.getObject());
    }
}

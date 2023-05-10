package com.tss.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.tss.repositories.data"},
        entityManagerFactoryRef = "dataEntityManagerFactory",
        transactionManagerRef= "dataTransactionManager")
public class DataSourceDataConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.data")
    public DataSourceProperties dataDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.data.configuration")
    public DataSource userDataSource() {
        return dataDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();
    }

    @Primary
    @Bean(name = "dataEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean dataEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(userDataSource())
                .packages("com.tss.entities.data")
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager dataTransactionManager(
            final @Qualifier("dataEntityManagerFactory") LocalContainerEntityManagerFactoryBean dataEntityManagerFactory) {
        return new JpaTransactionManager(dataEntityManagerFactory.getObject());
    }
}

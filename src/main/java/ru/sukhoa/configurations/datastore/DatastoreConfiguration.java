package ru.sukhoa.configurations.datastore;


import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.sukhoa.domain.Node;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableNeo4jRepositories(basePackages = "ru.sukhoa.DAO.Neo4j", transactionManagerRef = "neoTransactionalManager")
@EnableJpaRepositories(basePackages = "ru.sukhoa.DAO.Postgres", entityManagerFactoryRef = "postgresEntityManager",
        transactionManagerRef = "postgresTransactionalManager")
@EnableTransactionManagement
public class DatastoreConfiguration {

    @Primary
    @Bean(name = "postgresDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSource getPostgresDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("postgresEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(getPostgresDatasource())
                .packages(Node.class)
                .persistenceUnit("postgresPU")
                .build();
    }

    @Bean("postgresTransactionalManager")
    public PlatformTransactionManager postgresTransactionalManager(
            @Qualifier("postgresEntityManager") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "neoj4DataSource")
    @ConfigurationProperties(prefix = "spring.data.neo4j")
    public DataSource getNeo4jDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public org.neo4j.ogm.config.Configuration configuration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config
                .driverConfiguration()
                .setURI("http://neo4j:123@localhost:7474")
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver");
        return config;
    }

    @Bean
    public SessionFactory sessionFactory() {
        return new SessionFactory(configuration(), "ru.sukhoa.domain");
    }

    @Bean("neoTransactionalManager")
    public Neo4jTransactionManager transactionManager() {
        return new Neo4jTransactionManager(sessionFactory());
    }

}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.testConfig;

import java.util.Properties;
import javax.sql.DataSource;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.ac.ebi.ep.testkit.TestKit;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("uk.ac.ebi.ep.data.repositories")
public class EnzymeMockDataConfig extends TestKit {
   
    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EnzymeMockDataConfig.class);
    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }


    @Bean
    public DataSource mockDataSource(){
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        return builder.build();


    }



    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        Properties properties = new Properties();
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty(" hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("spring.jpa.hibernate.ddl-auto", "update");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(false);

        LocalContainerEntityManagerFactoryBean factory
                = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaProperties(properties);
        factory.setPackagesToScan("uk.ac.ebi.ep.data.domain");
        factory.setDataSource(mockDataSource());

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }
}

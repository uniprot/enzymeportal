package uk.ac.ebi.ep.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author joseph
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"uk.ac.ebi.ep.data.repositories", "uk.ac.ebi.ep.model.repositories"})
@PropertySources({
    @PropertySource(value = "classpath:log4j.properties", ignoreResourceNotFound = true),
    @PropertySource("classpath:ep-web-client.properties"),
    @PropertySource("classpath:chembl-adapter.properties")

})
public class DataConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        //em.setPersistenceXmlLocation("classpath*:META-INF/persistence.xml");

        em.setDataSource(dataSource);

        em.setPackagesToScan("uk.ac.ebi.ep.data.domain", "uk.ac.ebi.ep.model");
        Properties properties = new Properties();
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");

        properties.setProperty("hibernate.cache.use_second_level_cache", "false");
        properties.setProperty("hibernate.cache.auto_evict_collection_cache", "true");

        properties.setProperty("hibernate.batch_fetch_style", "DYNAMIC");
        properties.setProperty("hibernate.max_fetch_depth", "1");
        properties.setProperty("hibernate.default_batch_fetch_size", "30");

        HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
        vendor.setShowSql(false);
        vendor.setDatabase(Database.ORACLE);
        em.setJpaProperties(properties);
        em.setJpaVendorAdapter(vendor);

        return em;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        //return hibernateTransactionManager();
        return new JpaTransactionManager(entityManagerFactory().getObject());

    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }


    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();

        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("uk.ac.ebi.ep.data.domain", "uk.ac.ebi.ep.model");
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");

        properties.setProperty("hibernate.cache.use_second_level_cache", "false");
        properties.setProperty("hibernate.cache.auto_evict_collection_cache", "true");
        return properties;
    }
}

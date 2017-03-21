/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.dataconfig;

import java.util.Properties;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.HibernateExceptionTranslator;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
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
@EnableJpaRepositories("uk.ac.ebi.ep.data.repositories")
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

        em.setPackagesToScan("uk.ac.ebi.ep.data.domain");

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

    private SessionFactory sessionFactory() {
        EntityManager em = entityManagerFactory().getObject().createEntityManager();
        Session session = em.unwrap(Session.class);

        return session.getSessionFactory();
    }

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    //@Bean
    public HibernateTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager manager = new HibernateTransactionManager(sessionFactory());
        manager.setAllowResultAccessAfterCompletion(true);

        return manager;

    }
}

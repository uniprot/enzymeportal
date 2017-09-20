package uk.ac.ebi.ep.model.testConfig;

import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.hibernate.SessionFactory;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import uk.ac.ebi.ep.testkit.TestKit;

/**
 * configuration for integration test
 *
 * @author joseph
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("uk.ac.ebi.ep.model.repositories")
@PropertySource({"classpath:data.sql", "classpath:schema.sql"})
public class SpringDataMockConfig extends TestKit {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SpringDataMockConfig.class);

    @Bean
    public HibernateExceptionTranslator hibernateExceptionTranslator() {
        return new HibernateExceptionTranslator();
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        builder.setName("ep");
        //return builder.build();
        //return builder.addDefaultScripts().build();
        return builder
                .addScript("classpath:schema.sql")
                .addScript("classpath:data.sql")
                .build();

    }

    @Bean
    public DataSource defaultDataSource() throws SQLException {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:enzymePortal");
        dataSource.setUsername("sa");//not important to secure
        dataSource.setPassword("");//default - not important to secure

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        Properties properties = new Properties();
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty(" hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        //properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.ejb.naming_strategy", "org.hibernate.cfg.ImprovedNamingStrategy");
        properties.setProperty("spring.jpa.hibernate.ddl-auto", "update");
        //properties.setProperty("spring.jpa.hibernate.ddl-auto", "create-drop");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.H2);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setShowSql(false);

        LocalContainerEntityManagerFactoryBean factory
                = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaProperties(properties);
        factory.setPackagesToScan("uk.ac.ebi.ep.model");
        factory.setDataSource(dataSource());

        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txManager;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(
                new String[]{"uk.ac.ebi.ep.model"});
        sessionFactory.setHibernateProperties(h2Properties());

        return sessionFactory;
    }

//        @Bean
//    public SessionFactory sessionFactory() {
//        EntityManager em = entityManagerFactory().getObject().createEntityManager();
//        Session session = em.unwrap(Session.class);
//
//        return session.getSessionFactory();
//    }
    //@Bean(name = "hibernateTransactionManager")
    //@Autowired
    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {

        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        //txManager.setAllowResultAccessAfterCompletion(true);

        return txManager;
    }

    private Properties h2Properties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty(" hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.setProperty("spring.jpa.hibernate.ddl-auto", "update");

        return properties;
    }
}

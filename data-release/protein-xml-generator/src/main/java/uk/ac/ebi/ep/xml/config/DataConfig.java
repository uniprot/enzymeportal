package uk.ac.ebi.ep.xml.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by joseph on 2/26/18.
 */
@Slf4j
@Configuration
@EnableTransactionManagement
public class DataConfig {

    private static final String PACKAGES_TO_SCAN = "uk.ac.ebi.ep.xml.entities";

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(DataSourceProperties properties) {
        return (HikariDataSource) properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class).build();

    }

    @Bean
    @Autowired
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder, DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages(PACKAGES_TO_SCAN)
                .build();

    }

    @Bean
    @ConfigurationProperties("spring.jpa")
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    //    @Bean(name = "xmlTransactionManager")
//    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
//        return new JpaTransactionManager(emf);
//        //return new DataSourceTransactionManager(dataSource(dataSourceProperties()));
//    }
//    @Bean
//    public HibernateExceptionTranslator hibernateExceptionTranslator() {
//        return new HibernateExceptionTranslator();
//    }  
    // @Bean
    //@ConfigurationProperties(prefix = "spring.datasource.hikari")
//    public DataSource dataSourceNotUsed(DataSourceProperties properties) {
////        return (HikariDataSource) properties.initializeDataSourceBuilder()
////                .type(HikariDataSource.class).build();
//
//        DataSource ds = (HikariDataSource) properties.initializeDataSourceBuilder()
//                .type(HikariDataSource.class).build();
//
//        // use pretty formatted query with multiline enabled
//        PrettyQueryEntryCreator creator = new PrettyQueryEntryCreator();
//        creator.setMultiline(true);
//
//        SystemOutQueryLoggingListener listener = new SystemOutQueryLoggingListener();
//        listener.setQueryLogEntryCreator(creator);
//
//        return ProxyDataSourceBuilder
//                .create(ds)
//                .name("MyDS")
//                .listener(listener)
//                .proxyResultSet() // enable resultset proxy
//                .afterMethod(executionContext -> {
//                    // print out JDBC API calls to console
//                    Method method = executionContext.getMethod();
//                    Class<?> targetClass = executionContext.getTarget().getClass();
//                    //log.error("JDBC: " + targetClass.getSimpleName() + "#" + method.getName());
//                })
//                .afterQuery((execInfo, queryInfoList) -> {
//                    //log.error("Query took " + execInfo.getElapsedTime() + "msec With BATCH "+ execInfo.getBatchSize());
//                    if(execInfo.getBatchSize() > 0){
//                       log.error("Query took " + execInfo.getElapsedTime() + "msec With BATCH "+ execInfo.getBatchSize());  
//                    }
//                })
//                .build();
//
//    }
    // use hibernate to format queries
//    private static class PrettyQueryEntryCreator extends DefaultQueryLogEntryCreator {
//
//        private Formatter formatter = FormatStyle.BASIC.getFormatter();
//
//        @Override
//        protected String formatQuery(String query) {
//            return this.formatter.format(query);
//        }
//    }
//    @Bean(destroyMethod="close")
//    @Autowired
//    public DataSource dataSource(XmlDatasourceProperties properties) {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setUsername(properties.getUsername());
//        dataSource.setPassword(properties.getPassword());
//        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");
//        dataSource.setUrl(properties.getUrl());
//        return dataSource;
//    }
    //hibernate
//    @Bean
//    @Autowired
//    public LocalSessionFactoryBean sessionFactory(DataSource dataSource) {
//        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
//        sessionFactory.setDataSource(dataSource);
//        sessionFactory.setPackagesToScan(PACKAGES_TO_SCAN);
//        //sessionFactory.setHibernateProperties(jpaProperties());
//
//        return sessionFactory;
//    }
//
//    @Bean
//    @Autowired
//    public HibernateTransactionManager hibernateTransactionManager(SessionFactory sessionFactory) {
//
//        HibernateTransactionManager txManager
//                = new HibernateTransactionManager();
//        txManager.setSessionFactory(sessionFactory);
//
//        return txManager;
//    }
}

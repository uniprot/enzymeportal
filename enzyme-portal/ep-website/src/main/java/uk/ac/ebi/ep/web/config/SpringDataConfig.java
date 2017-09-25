/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web.config;

/**
 *
 * @author joseph
 */
//@Configuration
//@EnableTransactionManagement
//@EnableJpaRepositories("uk.ac.ebi.ep.data.repositories")
public class SpringDataConfig{// extends WebMvcConfigurerAdapter {

//    @Autowired
//    private Environment env;
//
//    // Declare a datasource that has pooling capabilities
//    @Bean
//    public DataSource dataSource() {
//        try {
//            // ComboPooledDataSource ds = new ComboPooledDataSource();
//            OracleDataSource ds = new OracleConnectionPoolDataSource();
//            //OracleConnectionPoolDataSource ds = new OracleConnectionPoolDataSource();
//
//           
//            ds.setURL(env.getProperty("app.jdbc.url"));
//            ds.setUser(env.getRequiredProperty("app.jdbc.username"));
//            ds.setPassword(env.getRequiredProperty("app.jdbc.password"));
//            
//             ds.setConnectionCachingEnabled(true);
//             ds.setConnectionCacheName("ep-connection-cache-01");
//             ds.setImplicitCachingEnabled(true);
//
//            Properties prop = new Properties();
//
//            prop.setProperty("MinLimit", "5");     // the cache size is 5 at least 
//            prop.setProperty("MaxLimit", "100");
//            prop.setProperty("InitialLimit", "3"); // create 3 connections at startup
//            
//            
////            prop.setProperty("InactivityTimeout", "1800");    //  seconds
////            prop.setProperty("AbandonedConnectionTimeout", "900");  //  seconds
////            prop.setProperty("MaxStatementsLimit", "10");
////            prop.setProperty("PropertyCheckInterval", "60"); // seconds
//            ds.setConnectionCacheProperties(prop);
//
//
//            return ds;
//        } catch (IllegalStateException | SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        //em.setPersistenceXmlLocation("classpath*:META-INF/persistence.xml");
//
//        //em.setPersistenceUnitName("ep_PU");
//        em.setDataSource(dataSource());
//        em.setPackagesToScan("uk.ac.ebi.ep.data.domain");
//
//        Properties properties = new Properties();
//        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
//        properties.setProperty("hibernate.connection.driver_class", "oracle.jdbc.OracleDriver");
//
//          HibernateJpaVendorAdapter vendor = new HibernateJpaVendorAdapter();
//        vendor.setShowSql(false);
//        vendor.setDatabase(Database.ORACLE);
//        em.setJpaProperties(properties);
//        em.setJpaVendorAdapter(vendor);
//      
//
//        return em;
//    }
//
//    @Bean
//    public JpaTransactionManager transactionManager() {
//        JpaTransactionManager transactionManager = new JpaTransactionManager();
//        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//        return transactionManager;
//    }
//
////       @Bean
////    public PlatformTransactionManager transactionManager() {
////        PlatformTransactionManager manager = new JpaTransactionManager(entityManagerFactory().getObject());   
////        //return new JpaTransactionManager();
////        return manager;
////    }
//    @Bean
//    public HibernateExceptionTranslator hibernateExceptionTranslator() {
//        return new HibernateExceptionTranslator();
//    }

}

package uk.ac.ebi.ep.data.dataconfig;

import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author joseph
 */
@Configuration
@Prod
@PropertySource({"classpath:ep-db-uzprel.properties"})
public class ProdDataConfig implements EnzymePortalDataConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProdDataConfig.class);
    @Autowired
    private Environment env;

    @Bean
    @Override
    public DataSource dataSource() {
        String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));

        String username = env.getRequiredProperty("ep.db.username");
        String password = env.getRequiredProperty("ep.db.password");
        DriverManagerDataSource ds = new DriverManagerDataSource(url, username, password);

        ds.setDriverClassName("oracle.jdbc.OracleDriver");
        
         LOGGER.warn("The connection url "+ url);//DELETE LATER

        return ds;
    }
    
//       // @Bean
//    //public DataSource oracleDataSource() {
// @Bean
//    @Override
//    public DataSource dataSourceXX() {
//        // set cache properties 
//        Properties prop = new Properties();
//
//        prop.setProperty("MinLimit", "5");     // the cache size is 5 at least 
//        prop.setProperty("MaxLimit", "1000");
//        prop.setProperty("InitialLimit", "3"); // create 3 connections at startup
//
//        OracleDataSource ds = null;
//        try {
//
//            ds = new OracleConnectionPoolDataSource();
//
//            String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
//                    env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));
//
//            ds.setURL(url);
//            ds.setUser(env.getRequiredProperty("ep.db.username"));
//            ds.setPassword(env.getRequiredProperty("ep.db.password"));
//
//            //ds.setConnectionCacheName("ep-connection-cache-01");
//            //ds.setImplicitCachingEnabled(true);
//
//            ds.setConnectionProperties(prop);
//            System.out.println("prod called "+ url);
//
//            return ds;
//        } catch (IllegalStateException | SQLException e) {
//            LOGGER.error(e.getMessage(), e);
//        }
//        return ds;
//    }
}

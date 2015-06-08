package uk.ac.ebi.ep.data.dataconfig;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import oracle.jdbc.pool.OracleConnectionPoolDataSource;
import oracle.jdbc.pool.OracleDataSource;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolProperties;
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
@Dev
@PropertySource({"classpath:ep-db-uzpdev.properties"})
public class DevDataConfig extends AbstractConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevDataConfig.class);
    @Autowired
    private Environment env;

    @Bean
    @Override
    //public DataSource dataSource() {
              public DataSource driverManagerDataSource() {
        String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));

        String username = env.getRequiredProperty("ep.db.username");
        String password = env.getRequiredProperty("ep.db.password");

        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName("oracle.jdbc.OracleDriver");
        p.setUsername(username);
        p.setPassword(password);

        //TODO - fine-tune connection poooling here
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(false);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30000);
        p.setTimeBetweenEvictionRunsMillis(30000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10000);
        p.setRemoveAbandonedTimeout(60);
        p.setMinEvictableIdleTimeMillis(30000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
        datasource.setPoolProperties(p);

        return datasource;

    }

    @Bean
    @Override
    public DataSource poolDataSource() {
        String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));

        String username = env.getRequiredProperty("ep.db.username");
        String password = env.getRequiredProperty("ep.db.password");

        PoolDataSource ds = PoolDataSourceFactory.getPoolDataSource();

        try {
            ds.setURL(url);
            ds.setUser(username);
            ds.setPassword(password);
            ds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");

            ds.setInitialPoolSize(5);
            ds.setMinPoolSize(1);
            ds.setMaxPoolSize(1000);
            ds.setMaxIdleTime(0);

            //in secs
            ds.setConnectionWaitTimeout(5);
            ds.setInactiveConnectionTimeout(0);
            ds.setTimeToLiveConnectionTimeout(0);
            ds.setAbandonedConnectionTimeout(0);
            ds.setTimeoutCheckInterval(30);

            //
            ds.setValidateConnectionOnBorrow(false);
            ds.setFastConnectionFailoverEnabled(false);
            ds.setMaxStatements(Integer.MAX_VALUE);
//            
//            ds.setMaxConnectionReuseCount(0);
//            ds.setMaxConnectionReuseTime(0);

            return ds;
        } catch (IllegalStateException | SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ds;
    }

    @Bean
    @Override
    public DataSource comboPooledDataSource() {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {

           
            String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                    env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));

            String username = env.getRequiredProperty("ep.db.username");
            String password = env.getRequiredProperty("ep.db.password");

            //ds.setDriverClass(env.getRequiredProperty("app.jdbc.driverClassName"));
            ds.setJdbcUrl(url);

            ds.setUser(username);
            ds.setPassword(password);

            //properties
            ds.setInitialPoolSize(100);
            ds.setMinPoolSize(2);
            ds.setAcquireIncrement(100);
            ds.setMaxPoolSize(1000);
            ds.setMaxStatements(Integer.MAX_VALUE);
            ds.setNumHelperThreads(50);

            ds.setTestConnectionOnCheckout(true);

            return ds;
        } catch (IllegalStateException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ds;
    }

    @Bean
    @Override
    public DataSource oracleDataSource() {

        // set cache properties 
        Properties prop = new Properties();

        prop.setProperty("MinLimit", "5");     // the cache size is 5 at least 
        prop.setProperty("MaxLimit", "1000");
        prop.setProperty("InitialLimit", "3"); // create 3 connections at startup

        OracleDataSource ds = null;
        try {

            ds = new OracleConnectionPoolDataSource();

            String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                    env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));

            ds.setURL(url);
            ds.setUser(env.getRequiredProperty("ep.db.username"));
            ds.setPassword(env.getRequiredProperty("ep.db.password"));

            ds.setConnectionCacheName("ep-connection-cache-01");
            ds.setImplicitCachingEnabled(true);

            ds.setConnectionProperties(prop);

            return ds;
        } catch (IllegalStateException | SQLException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return ds;
    }

    @Bean
    @Override
            public DataSource dataSource() {
    //public DataSource driverManagerDataSource() {
        String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));

        String username = env.getRequiredProperty("ep.db.username");
        String password = env.getRequiredProperty("ep.db.password");
        return new DriverManagerDataSource(url, username, password);
    }

}

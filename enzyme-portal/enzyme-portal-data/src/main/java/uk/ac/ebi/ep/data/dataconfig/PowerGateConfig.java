package uk.ac.ebi.ep.data.dataconfig;

import java.sql.SQLException;
import javax.sql.DataSource;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 *
 * @author joseph
 */
@Configuration
@PowerGate
@PropertySource({"classpath:ep-db-uzppub-pg.properties"})
public class PowerGateConfig implements EnzymePortalDataConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(PowerGateConfig.class);
    @Autowired
    private Environment env;

    @Bean
    @Override
    public DataSource dataSource() {
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

    

}

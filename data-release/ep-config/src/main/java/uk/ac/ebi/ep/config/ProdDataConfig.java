package uk.ac.ebi.ep.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
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
@Prod
@PropertySource({"classpath:ep-db-uzprel.properties"})
public class ProdDataConfig implements EnzymePortalDataConfig {

    @Autowired
    private Environment env;

    @Bean
    @Override
    public DataSource dataSource() {
    	String serviceName = env.getRequiredProperty("ep.db.serviceName");
    	String instance = env.getRequiredProperty("ep.db.instance");
    	String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), instance);
    	if((serviceName !=null) && !serviceName.isEmpty()){
    		url = String.format("jdbc:oracle:thin:@//%s:%s/%s",
                    env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), serviceName);
    	}
        

        String user = env.getRequiredProperty("ep.db.username");
        String password = env.getRequiredProperty("ep.db.password");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("oracle.jdbc.OracleDriver");
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(1);
        //config.setLeakDetectionThreshold(900000);
        config.setMaxLifetime(345600000);
        config.setPoolName("ep-parser-pool");
        config.setConnectionTimeout(50_000_000); //~14hrs
      
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }

}

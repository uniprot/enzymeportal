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
        String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
                env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));

        String user = env.getRequiredProperty("ep.db.username");
        String password = env.getRequiredProperty("ep.db.password");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("oracle.jdbc.OracleDriver");

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        
        //new pool config
        config.setPoolName("ep-parser-pool-"+ System.currentTimeMillis());
        config.setMaximumPoolSize(20);
        //config.setMinimumIdle(1);
        //config.addDataSourceProperty("useServerPrepStmts", true);
        //end new pool config

        HikariDataSource ds = new HikariDataSource(config);
        return ds;
    }


}

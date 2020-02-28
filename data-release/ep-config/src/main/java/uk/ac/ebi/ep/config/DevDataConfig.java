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
@Dev
@PropertySource({"classpath:ep-db-uzpdev.properties"})
public class DevDataConfig extends AbstractConfig {

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

        return new HikariDataSource(config);
    }

    @Override
    public DataSource driverManagerDataSource() {
//        String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
//                env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));
//
//        String username = env.getRequiredProperty("ep.db.username");
//        String password = env.getRequiredProperty("ep.db.password");
//        DriverManagerDataSource ds = new DriverManagerDataSource(url, username, password);
//
//        ds.setDriverClassName("oracle.jdbc.OracleDriver");
//
//        return ds;
         throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   
    }

    @Override
    protected DataSource comboPooledDataSource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   
    }

    @Override
    protected DataSource oracleDataSource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected DataSource poolDataSource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

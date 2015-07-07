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
@Dev
@PropertySource({"classpath:ep-db-uzpdev.properties"})
public class DevDataConfig extends AbstractConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DevDataConfig.class);
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
        //LOGGER.warn("The connection url "+ url);//DELETE LATER

        return ds;
    }

    @Override
    protected DataSource comboPooledDataSource() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected DataSource driverManagerDataSource() {
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

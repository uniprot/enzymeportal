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
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Configuration
@EZPPUBHXFB
@PropertySource({"classpath:ep-db-ezppub-hxfb.properties"})
public class EZPPUBHXFBConfig implements EnzymePortalDataConfig {

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
}

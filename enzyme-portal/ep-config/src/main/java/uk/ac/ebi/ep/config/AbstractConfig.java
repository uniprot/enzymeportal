
package uk.ac.ebi.ep.config;

import javax.sql.DataSource;

/**
 *
 * @author joseph
 */
public abstract class AbstractConfig implements EnzymePortalDataConfig {

    protected abstract DataSource driverManagerDataSource();

    protected abstract DataSource oracleDataSource();

    protected abstract DataSource comboPooledDataSource();

    protected abstract DataSource poolDataSource();

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.dataconfig;

import javax.sql.DataSource;

/**
 *
 * @author joseph
 */
public abstract class AbstractConfig implements EnzymePortalDataConfig {

    

    protected abstract DataSource epDataSource();
}

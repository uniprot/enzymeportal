/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.testkit;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author joseph
 */
public class TestKit {

    protected String getFile(String filename) {
        try {
            InputStream in = this.getClass().getClassLoader()
                    .getResourceAsStream(filename);

            return IOUtils.toString(in);
        } catch (IOException ex) {
            // LOGGER.error(ex.getMessage(), ex);
        }

        return null;
    }

}

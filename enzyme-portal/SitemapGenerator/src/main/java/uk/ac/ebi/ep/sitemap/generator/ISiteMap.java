/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap.generator;

import uk.ac.ebi.ep.sitemap.generator.SiteMapException;
import java.util.Collection;

/**
 *the abstract method of this interface is to be implemented in order to generate a siteMap.
 * @author joseph
 * @param <T>
 */
public interface ISiteMap<T> {

    /**
     * the abstract method that needs to be implemented to generate siteMap from an input
     * @param inputData collection of data
     * @param output the output of the siteMap which includes the directory
     * @param fileName name of the generated file
     * @param testMode
     * @throws SiteMapException if siteMap cannot be generated
     */
    void generateSitemap(Collection<?> inputData, T output, String fileName,boolean testMode) throws SiteMapException;
}

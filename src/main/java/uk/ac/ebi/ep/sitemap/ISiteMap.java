/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap;

import java.util.Collection;

/**
 *the abstract method of this interface is to be implemented in order to generate a siteMap.
 * @author joseph
 */
public interface ISiteMap<T> {

    /**
     * the abstract method that needs to be implemented to generate siteMap from an input
     * @param inputData collection of data
     * @param output the output type of the siteMap
     * @throws SiteMapException if siteMap cannot be generated
     */
    void generateSitemap(Collection<?> inputData, T output) throws SiteMapException;
}

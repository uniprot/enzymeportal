/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap.advanced;

import java.net.URL;
import java.util.Date;

/**
 *
 * @author joseph
 */
public interface ISitemapUrl {

    public abstract Date getLastMod();

    public abstract URL getUrl();
}

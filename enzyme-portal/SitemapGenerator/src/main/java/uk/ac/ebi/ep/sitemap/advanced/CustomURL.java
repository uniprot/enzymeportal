/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.sitemap.advanced;

import java.net.URL;

/**
 *
 * @author joseph
 */
public final class CustomURL  {
    private URL url;
    private String prefix;
    private String suffix;
    
    public CustomURL(URL url, String prefix, String suffix){
        this.url = url;
        
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public URL getUrl() {
        return url;
    }


    
    
}

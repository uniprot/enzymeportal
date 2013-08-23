/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import uk.ac.ebi.ep.mBean.FilesConfig;

/**
 * This Servlet reads and serves the generated SiteMap
 *
 * @author joseph
 */
//
public class EnzymePortalSitemapServlet extends HttpServlet {

    public Logger LOGGER = Logger.getLogger(EnzymePortalSitemapServlet.class);
    private FilesConfig sitemapConfig;
    
    public FilesConfig getSitemapConfig() {
        return sitemapConfig;
    }
    
    public void setSitemapConfig(FilesConfig sitemapConfig) {
        this.sitemapConfig = sitemapConfig;
    }
    

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //response.setContentType("application/gzip");
        response.setContentType("application/xml");
        LOGGER.info("Getting URL connection to sitemap...");
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());//.getWebApplicationContext(getServletContext());
        // ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        //FilesConfig sitemapConfig = (FilesConfig) context.getBean("sitemapConfig");
        sitemapConfig = (FilesConfig) context.getBean(FilesConfig.class);

       
        URL url = new URL(sitemapConfig.getSitemapIndex());

                    LOGGER.info("Connecting to sitemap...");
        URLConnection con = url.openConnection(java.net.Proxy.NO_PROXY);
                    
                    LOGGER.info("Connected to sitemap...");
        InputStream inputStream = null;
        try {
                    inputStream = con.getInputStream();
                    int r = -1;
                    LOGGER.debug("Starting to read sitemap...");
                    while ((r = inputStream.read()) != -1) {
                        response.getOutputStream().write(r);
                    }

                    response.getOutputStream().flush();
                    response.flushBuffer();
                    LOGGER.debug("... Read and served");
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                }
                }
}

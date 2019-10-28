
package uk.ac.ebi.ep.web.sitemap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author joseph
 */
@Slf4j
public class SiteMapServlet extends HttpServlet {

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
        String sitemaps = request.getParameter("sitemaps");
        response.setContentType("text/xml");
        log.info("Getting URL connection to sitemap...");
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        sitemapConfig = (FilesConfig) context.getBean(FilesConfig.class);

        String output = String.format("%s/%s", sitemapConfig.getSitemapUrl(), sitemaps);

        URL url = new URL(output);

        log.info("Connecting to sitemap...");
        URLConnection con = url.openConnection(java.net.Proxy.NO_PROXY);

        log.info("Connected to sitemap...");

        InputStream inputStream = con.getInputStream();

        int r = -1;
        log.debug("Starting to read sitemap...");
        while ((r = inputStream.read()) != -1) {
            response.getOutputStream().write(r);
        }

        try {
            response.getOutputStream().flush();
            response.flushBuffer();

            log.debug("... Read and served");

        } finally {

            inputStream.close();

        }
    }

}

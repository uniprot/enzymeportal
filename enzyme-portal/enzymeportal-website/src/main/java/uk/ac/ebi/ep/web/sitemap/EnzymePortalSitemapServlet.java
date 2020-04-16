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
 * This Servlet reads and serves the generated SiteMap
 *
 * @author joseph
 */
@Slf4j
public class EnzymePortalSitemapServlet extends HttpServlet {

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/xml");
        log.info("Getting URL connection to sitemap...");
        WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());

        FilesConfig sitemapConfig = context.getBean(FilesConfig.class);

        URL url = new URL(sitemapConfig.getSitemapIndex());

        log.info("Connecting to sitemap...");
        URLConnection con = url.openConnection(java.net.Proxy.NO_PROXY);

        log.info("Connected to sitemap...");
        try (InputStream inputStream = con.getInputStream()) {
            int r = -1;
            log.debug("Starting to read sitemap...");
            while ((r = inputStream.read()) != -1) {
                response.getOutputStream().write(r);
            }

            response.getOutputStream().flush();
            response.flushBuffer();
            log.debug("... Read and served");
        }
    }
}

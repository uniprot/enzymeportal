package uk.ac.ebi.ep.web.sitemap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Servlet acting as proxy to retrieve files from the file system, out of the
 * web container.
 * <br/>
 * It requires a context parameter <b><code>FileProxyServlet.fs.base</code></b>
 * set to the path to the directory in the file system used as base for the file
 * requests. This directory should be readable for the unix user running the
 * server, and from the machine where it runs.
 *
 * @author joseph
 * @since 1.0.25
 */
@Slf4j
public class FileProxyServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        ApplicationContext context = WebApplicationContextUtils
                .getWebApplicationContext(getServletContext());
        FilesConfig config = new FilesConfig();
        if (context != null) {
            config = context.getBean(FilesConfig.class);
        }

        File f = new File(config.getBaseDirectory(),
                req.getPathInfo().substring(1));
        if (f.exists() && f.canRead()) {
            String extension = req.getPathInfo()
                    .substring(req.getPathInfo().lastIndexOf('.') + 1);
            String contentType = "text/plain;charset=UTF-8";
            if ("xml".equals(extension)) {
                contentType = "text/xml;charset=UTF-8";
            }
            // Add here any other content types when (if) we serve them.
            resp.setContentType(contentType);

            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f))) {
                byte[] buffer = new byte[256];
                int r = -1;
                while ((r = bis.read(buffer)) != -1) {
                    resp.getOutputStream().write(buffer, 0, r);
                }
                resp.flushBuffer();
            }

        } else {
            log.error("File not found: " + f);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}

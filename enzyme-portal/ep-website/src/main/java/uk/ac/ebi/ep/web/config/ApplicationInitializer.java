/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Properties;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author joseph
 */
public class ApplicationInitializer implements WebApplicationInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInitializer.class);

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
//Load application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContext.class);

        String profile = computeProfile(rootContext);
      
        rootContext.getEnvironment().setActiveProfiles(profile);

        //Context loader listener
        servletContext.addListener(new ContextLoaderListener(rootContext));

        //Dispatcher servlet
        ServletRegistration.Dynamic dispatcher
                = servletContext.addServlet("ep-website-dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setAsyncSupported(true);
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        dispatcher.addMapping("*.html");
        dispatcher.addMapping("*.json");

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR);

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncoding", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");

    }

    private String computeProfile(AnnotationConfigWebApplicationContext rootContext) {
        Resource resource = rootContext.getResource("classpath:ep.properties");

        Properties property = new Properties();

        InputStream is;
        try {
            is = resource.getInputStream();

            property.load(is);

        } catch (IOException ex) {

            LOGGER.error("Missing property file in this tomcat server /lib/ep.properties.", ex);
        }
        String profile = property.getProperty("app");

        return profile;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.config;

import java.io.File;
import java.io.FileInputStream;
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

    private static final String URL_REWRITE_FILTER_NAME = "urlRewrite";
    private static final String URL_REWRITE_FILTER_MAPPING = "/*";
    private static final String URL_REWRITE_FILTER_PARAM_LOGLEVEL = "logLevel";
    private static final String URL_REWRITE_FILTER_LOGLEVEL_SLF4J = "slf4j";
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationInitializer.class);

//    @Autowired
//    private Environment env;
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
//Load application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContext.class);
         //rootContext.register(DataConfig.class);
        // rootContext.getEnvironment().setDefaultProfiles("uzpdev","uzppro");
        //rootContext.getEnvironment().setActiveProfiles("uzprel");

         String profile = computeProfile(rootContext);
         

        rootContext.getEnvironment().setActiveProfiles(profile);
        //rootContext.getEnvironment().setActiveProfiles("uzpdev");
        rootContext.setDisplayName("ep-website");

        //Context loader listener
        servletContext.addListener(new ContextLoaderListener(rootContext));

        //Dispatcher servlet
        ServletRegistration.Dynamic dispatcher
                = servletContext.addServlet("ep-website-dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR);

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncoding", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");

    }

    @Deprecated
    private String readEnvironment() {
        String env = "no-env";
        try {

            ///nfs/public/rw/webadmin/tomcat/homes/apache-tomcat-7.0.55/lib
            //Use Any Environmental Variable , here i have used CATALINA_HOME
            String propertyHome = System.getenv("CATALINA_HOME");

             if (null == propertyHome) {

                //This is a system property that is  passed
                // using the -D option in the Tomcat startup script
                propertyHome = System.getProperty("PROPERTY_HOME");

            }

            String filePath = propertyHome + "/lib/";

            Properties property = new Properties();
            File propsFile = new File(filePath, "ep.properties");

            InputStream is = new FileInputStream(propsFile);

            property.load(is);

            env = property.getProperty("app");

        } catch (IOException e) {
            LOGGER.error("Missing property file in this tomcat server.", e);
        }

        return env;
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

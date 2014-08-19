/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.config;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 *
 * @author joseph
 */

public class ApplicationInitializer   implements WebApplicationInitializer {

       private static final String URL_REWRITE_FILTER_NAME = "urlRewrite";
    private static final String URL_REWRITE_FILTER_MAPPING = "/*";
    private static final String URL_REWRITE_FILTER_PARAM_LOGLEVEL = "logLevel";
    private static final String URL_REWRITE_FILTER_LOGLEVEL_SLF4J = "slf4j";
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        //Load application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationContext.class);
        // rootContext.register(DataConfig.class);
       // rootContext.getEnvironment().setDefaultProfiles("uzpdev","uzprel");
        rootContext.getEnvironment().setActiveProfiles("uzpdev");
       // rootContext.refresh();
      
        rootContext.setDisplayName("ep-website");



        //Context loader listener
        servletContext.addListener(new ContextLoaderListener(rootContext));
        
//        FilterRegistration.Dynamic urlReWrite = servletContext.addFilter(URL_REWRITE_FILTER_NAME, new UrlRewriteFilter());
//        urlReWrite.setInitParameter(URL_REWRITE_FILTER_PARAM_LOGLEVEL, URL_REWRITE_FILTER_LOGLEVEL_SLF4J);
//        EnumSet<DispatcherType> urlReWriteDispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);
//        urlReWrite.addMappingForUrlPatterns(urlReWriteDispatcherTypes, true, URL_REWRITE_FILTER_MAPPING);

        //Dispatcher servlet
        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("ep-website-dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
          EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR);
        
          CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        FilterRegistration.Dynamic characterEncoding = servletContext.addFilter("characterEncoding", characterEncodingFilter);
        characterEncoding.addMappingForUrlPatterns(dispatcherTypes, true, "/*");
        
        
        
           
                
    }
        


    
}

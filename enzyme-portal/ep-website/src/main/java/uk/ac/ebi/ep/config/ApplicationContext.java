/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.config;

import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import uk.ac.ebi.ep.data.dataconfig.DataConfig;
import uk.ac.ebi.ep.data.dataconfig.DevDataConfig;
import uk.ac.ebi.ep.data.dataconfig.ProdDataConfig;

/**
 *
 * @author joseph
 */
@Configuration
@ComponentScan(basePackages = {"uk.ac.ebi.ep",
        "uk.ac.ebi.ep.data.dataconfig"})

//@ComponentScan(basePackages = {"uk.ac.ebi.ep"})

//@ComponentScan(basePackages = "uk.ac.ebi.ep")
@EnableWebMvc
@EnableSpringDataWebSupport
//@Import({DataConfig.class,DevDataConfig.class,ProdDataConfig.class})
@Import({DevDataConfig.class,ProdDataConfig.class,DataConfig.class})
@ImportResource("classpath:trace-context.xml")
//@PropertySource("classpath:spring.properties")
public class ApplicationContext extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment env;
    
//        @Bean
//   
//    public DataSource dataSource() {
//        try {
//
//            System.out.println("ENVIRONMENT "+ env);
//            OracleDataSource ds = new OracleConnectionPoolDataSource();
//            //OracleConnectionPoolDataSource ds = new OracleConnectionPoolDataSource();
//
//            String url = String.format("jdbc:oracle:thin:@%s:%s:%s",
//                    env.getRequiredProperty("ep.db.host"), env.getRequiredProperty("ep.db.port"), env.getRequiredProperty("ep.db.instance"));
//
//            System.out.println("URL for connection "+ url);
//            ds.setURL(url);
//            ds.setUser(env.getRequiredProperty("ep.db.username"));
//            ds.setPassword(env.getRequiredProperty("ep.db.password"));
//
//            ds.setConnectionCacheName("ep-connection-cache-01");
//            ds.setImplicitCachingEnabled(true);
//
//            Properties prop = new Properties();
//
//            prop.setProperty("MinLimit", "5");     // the cache size is 5 at least 
//            prop.setProperty("MaxLimit", "100");
//            prop.setProperty("InitialLimit", "3"); // create 3 connections at startup
//
//            return ds;
//        } catch (IllegalStateException | SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    
//http://docs.spring.io/spring/docs/3.0.0.M3/reference/html/ch04s12.html
    // Maps resources path to webapp/resources
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/");//this is the root src/webapp/here

 
    }


    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {
        
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(new PageRequest(1, 5));
        
        argumentResolvers.add(resolver);

    }
    



    // Only needed if we are using @Value and ${...} when referencing properties
//    @Bean
//    public static PropertySourcesPlaceholderConfigurer properties() {
//        PropertySourcesPlaceholderConfigurer propertySources = new PropertySourcesPlaceholderConfigurer();
//        Resource[] resources = new ClassPathResource[]{
//            new ClassPathResource("spring.properties")};
//        propertySources.setLocations(resources);
//        propertySources.setIgnoreUnresolvablePlaceholders(true);
//        return propertySources;
//    }

    // Provides internationalization of messages
    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource(); //new ResourceBundleMessageSource();
        source.setBasename("classpath:messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }

    @Bean
    public CookieLocaleResolver localeResolver() {
        CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
        cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
        cookieLocaleResolver.setCookieName("locale");
        return cookieLocaleResolver;
    }
   // public CookieLocaleResolver
//
    // Basic jsp view resolver ...

    @Bean
    public InternalResourceViewResolver internalResourceViewResolver() {

        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;

    }

   

    @Bean
    public FormattingConversionService formattingConversionService() {

        // Use the DefaultFormattingConversionService but do not register defaults
        DefaultFormattingConversionService conversionService = new DefaultFormattingConversionService(false);

        // Ensure @NumberFormat is still supported
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());

        // Register date conversion with a specific global format
        DateFormatterRegistrar registrar = new DateFormatterRegistrar();
        registrar.setFormatter(new DateFormatter("EEE, d MMM yyyy"));
        registrar.setFormatter(new DateFormatter("d MMMMM yyyy"));
        registrar.setFormatter(new DateFormatter("dd MMMMM yyyy - HH:mm"));

        registrar.registerFormatters(conversionService);

        return conversionService;
    }


}

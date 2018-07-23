/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web.config;

import java.util.List;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.datetime.DateFormatterRegistrar;
import org.springframework.format.number.NumberFormatAnnotationFormatterFactory;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import uk.ac.ebi.ep.config.DataConfig;
import uk.ac.ebi.ep.config.DevDataConfig;
import uk.ac.ebi.ep.config.EZPPUBConfig;
import uk.ac.ebi.ep.config.EZPPUBHHConfig;
import uk.ac.ebi.ep.config.EZPPUBHXFBConfig;
import uk.ac.ebi.ep.config.HHConfig;
import uk.ac.ebi.ep.config.HXFBConfig;
import uk.ac.ebi.ep.config.ProdDataConfig;
import uk.ac.ebi.ep.converters.StringToEnzymeEntry;
import uk.ac.ebi.ep.ebeye.config.EbeyeConfig;

/**
 *
 * @author joseph
 */
@Configuration
@ComponentScan(basePackages = {"uk.ac.ebi.ep.config", "uk.ac.ebi.ep.controller", "uk.ac.ebi.ep.base", "uk.ac.ebi.ep.ebeye.config", "uk.ac.ebi.ep.data.service", "uk.ac.ebi.ep.literatureservice.config"})
@EnableWebMvc
@EnableSpringDataWebSupport
@Import({EbeyeConfig.class, EnzymePortalConfig.class, DevDataConfig.class, ProdDataConfig.class, EZPPUBConfig.class, EZPPUBHHConfig.class, EZPPUBHXFBConfig.class, HXFBConfig.class, HHConfig.class, DataConfig.class})
//@Import({EbeyeConfig.class, EnzymePortalConfig.class, DevDataConfig.class, ProdDataConfig.class,EZPPUBConfig.class, PowerGateConfig.class, OliverYardConfig.class, HXFBConfig.class, HHConfig.class, DataConfig.class})
@ImportResource("classpath:trace-context.xml")
@PropertySource("classpath:ep.properties")
public class ApplicationContext extends WebMvcConfigurerAdapter {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //registry.addMapping("/**");
        registry.addMapping("/**").allowedOrigins("http://localhost:8080");

//        registry.addMapping("/api/**")
//                .allowedOrigins("http://domain2.com")
//                .allowedMethods("PUT", "DELETE")
//                .allowedHeaders("header1", "header2", "header3")
//                .exposedHeaders("header1", "header2")
//                .allowCredentials(false).maxAge(3600);
    }

    /**
     * Maps resources path to webapp/resources
     *
     * @param registry ResourceHandlerRegistry
     */
//    @Override
//    public void addResourceHandlers1(ResourceHandlerRegistry registry) {
//
//        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
//        registry.addResourceHandler("/favicon.ico").addResourceLocations("/");
//
//    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**", "/favicon.ico", "/files/**")
                .addResourceLocations("/resources/", "/", "file:/nfs/public/rw/uniprot/enzyme_portal/sitemap/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> argumentResolvers) {

        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(new PageRequest(1, 10));

        argumentResolvers.add(resolver);

    }

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

        //add entry conversion
        conversionService.addConverter(new StringToEnzymeEntry());

        registrar.registerFormatters(conversionService);

        return conversionService;
    }

}

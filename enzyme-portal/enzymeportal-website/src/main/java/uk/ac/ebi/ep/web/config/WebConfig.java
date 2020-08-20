package uk.ac.ebi.ep.web.config;

import java.util.concurrent.TimeUnit;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.util.UrlPathHelper;
import uk.ac.ebi.ep.web.filter.WebXssFilter;

/**
 *
 * @author joseph
 */
@Configuration
@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private static final String SITEMAP_DIR = "file:/nfs/public/rw/uniprot/enzyme_portal/sitemap/";

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView(), new JstlView(), new InternalResourceView());
        registry.jsp("/WEB-INF/jsp/", ".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Long maxAge = 365L;
        TimeUnit unit = TimeUnit.DAYS;
        String resources = "/resources/**";

        String catalinaHome = System.getenv("CATALINA_HOME");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/WEB-INF/resources/", "file:" + catalinaHome + "/tomcat/");

        registry.addResourceHandler(resources)
                .addResourceLocations("/resources", "classpath:/static/")
                .setCachePeriod(31556926);
        registry
                .addResourceHandler(resources)
                .addResourceLocations("/resources/", "/resources/static/")
                .setCacheControl(CacheControl.maxAge(maxAge, unit).cachePublic());

        registry
                .addResourceHandler(resources, "/favicon.ico", "/files/**")
                .addResourceLocations("/resources/", "/", SITEMAP_DIR)
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer
                .setUseSuffixPatternMatch(false)
                .setUrlPathHelper(urlPathHelper);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }

    @Bean
    public FilterRegistrationBean xssFilter() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();

        registrationBean.setFilter(new WebXssFilter());
        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .csrf()
                .disable()
                .headers()
                .httpStrictTransportSecurity()
                .includeSubDomains(true)
                .preload(true)
                .maxAgeInSeconds(31536000)
                .and()
                .referrerPolicy()
                .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER_WHEN_DOWNGRADE)
                .and()
                .featurePolicy("geolocation 'self'")
                .and()
                .xssProtection(xss -> xss.xssProtectionEnabled(true))
                .addHeaderWriter(new XXssProtectionHeaderWriter())
                .cacheControl()
                .and()
                .contentTypeOptions()
                .and()
                .frameOptions()
                .sameOrigin();
    }

}

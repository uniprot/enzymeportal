package uk.ac.ebi.ep.web.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
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

/**
 *
 * @author joseph
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

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
        Long maxAge = 2000L;
        TimeUnit unit = TimeUnit.MILLISECONDS;

        String catalinaHome = System.getenv("CATALINA_HOME");
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/WEB-INF/resources/", "file:" + catalinaHome + "/tomcat/");

        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources", "classpath:/static/")
                .setCachePeriod(31556926);
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/", "/resources/static/")
                .setCacheControl(CacheControl.maxAge(maxAge, unit).cachePublic());

        registry
                .addResourceHandler("/resources/**", "/favicon.ico", "/files/**")
                .addResourceLocations("/resources/", "/", "file:/nfs/public/rw/uniprot/enzyme_portal/sitemap/")
                .setCachePeriod(3600)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);//enable mapping in request;
        configurer
                .setUseSuffixPatternMatch(false)
                .setUrlPathHelper(urlPathHelper);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**");
    }


}

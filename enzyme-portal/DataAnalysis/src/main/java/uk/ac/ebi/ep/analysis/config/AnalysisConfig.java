/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.analysis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import uk.ac.ebi.ep.analysis.service.DataAnalyzer;
import uk.ac.ebi.ep.data.service.EnzymePortalService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Configuration
@PropertySource({"classpath:service.properties", "classpath:service.urlConfig"})
public class AnalysisConfig {

    @Autowired
    private Environment env;

    @Bean
    public ServiceUrl serviceUrl() {
        ServiceUrl url = new ServiceUrl();

        String functionUrl = env.getProperty("service.url.function");
        String cofactorUrl = env.getProperty("service.url.cofactor");
        String activityUrl = env.getProperty("service.url.activity");
        String regulationUrl = env.getProperty("service.url.regulation");
        String biophysioUrl = env.getProperty("service.url.biophysio");

        url.setFunctionUrl(functionUrl);
        url.setActivityUrl(activityUrl);
        url.setBioPhysioUrl(biophysioUrl);
        url.setCofactorUrl(cofactorUrl);
        url.setRegulationUrl(regulationUrl);

        return url;
    }

    @Bean
    public DataAnalyzer dataAnalyzer() {
        return new DataAnalyzer();
    }
    
     @Bean
    public EnzymePortalService enzymePortalService() {
        return new EnzymePortalService();
    }
}

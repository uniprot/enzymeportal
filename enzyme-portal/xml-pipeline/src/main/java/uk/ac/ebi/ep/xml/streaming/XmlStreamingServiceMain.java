/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.streaming;

import javax.xml.bind.JAXBException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import uk.ac.ebi.ep.xml.config.DataConfig;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@EnableAsync
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackageClasses = {ServiceConfig.class, DataConfig.class})
public class XmlStreamingServiceMain {

    public static void main(String[] args) throws JAXBException {

        SpringApplication
                .run(XmlStreamingServiceMain.class, args)
                .getBean(XmlStreamingService.class)
                .generateXmL();
    }

}

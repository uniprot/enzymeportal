package uk.ac.ebi.ep.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(scanBasePackages = {"uk.ac.ebi"})
@EnableCaching
public class EnzymeportalWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnzymeportalWebsiteApplication.class, args);
    }

}

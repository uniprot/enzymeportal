package uk.ac.ebi.ep.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"uk.ac.ebi"})
public class EnzymeportalWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnzymeportalWebsiteApplication.class, args);
    }

}

package uk.ac.ebi.ep.enzymeservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "uk.ac.ebi.ep")
public class EnzymeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EnzymeServiceApplication.class, args);
    }

}

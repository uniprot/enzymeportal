package uk.ac.ebi.ep.restclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "uk.ac.ebi.ep")
public class EpRestclientApplication {

    public static void main(String... args) {
        SpringApplication.run(EpRestclientApplication.class, args);
    }

}

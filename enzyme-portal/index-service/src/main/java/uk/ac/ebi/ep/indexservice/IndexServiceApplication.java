package uk.ac.ebi.ep.indexservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "uk.ac.ebi.ep")
public class IndexServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IndexServiceApplication.class, args);

    }

}

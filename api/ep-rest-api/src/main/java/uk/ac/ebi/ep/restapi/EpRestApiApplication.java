package uk.ac.ebi.ep.restapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication(scanBasePackages = {"uk.ac.ebi"})
public class EpRestApiApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(EpRestApiApplication.class, args);
	}

}

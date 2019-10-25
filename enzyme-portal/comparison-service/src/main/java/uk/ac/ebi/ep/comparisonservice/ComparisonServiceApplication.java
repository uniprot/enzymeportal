package uk.ac.ebi.ep.comparisonservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "uk.ac.ebi.ep")
public class ComparisonServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComparisonServiceApplication.class, args);
	}

}

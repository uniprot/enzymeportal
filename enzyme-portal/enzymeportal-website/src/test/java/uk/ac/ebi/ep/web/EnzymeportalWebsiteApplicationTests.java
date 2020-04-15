package uk.ac.ebi.ep.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("uzpdev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EnzymeportalWebsiteApplicationTests {

    @Test
    public void contextLoads() {
    }

}

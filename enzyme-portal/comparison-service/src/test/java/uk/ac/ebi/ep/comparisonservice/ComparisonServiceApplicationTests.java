package uk.ac.ebi.ep.comparisonservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.comparisonservice.service.ComparisonService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ComparisonServiceApplicationTests {

    @Autowired
    private ComparisonService comparisonService;

    @Test
    public void contextLoads() {
        assertThat(comparisonService).isNotNull();
    }

}

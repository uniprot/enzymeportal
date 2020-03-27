package uk.ac.ebi.ep.brendaservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.ebi.ep.brendaservice.service.BrendaService;

@SpringBootTest
class BrendaServiceApplicationTests {

    @Autowired
    private BrendaService brendaService;

    @Test
    void contextLoads() {
        assertThat(brendaService).isNotNull();
    }

}

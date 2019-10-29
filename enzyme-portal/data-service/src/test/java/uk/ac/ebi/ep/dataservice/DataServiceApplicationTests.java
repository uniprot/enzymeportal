package uk.ac.ebi.ep.dataservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.dataservice.service.DataService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataServiceApplicationTests {

    @Autowired
    private DataService dataService;

    @Test
    public void contextLoads() {
        assertThat(dataService).isNotNull();
    }

}

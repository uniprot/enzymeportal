package uk.ac.ebi.ep.indexservice;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.indexservice.service.IndexService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class IndexServiceApplicationTests {

    @Autowired
    private IndexService indexService;

    @Test
    public void contextLoads() {
        assertThat(indexService).isNotNull();
    }

}

package uk.ac.ebi.ep.web;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.web.service.EntryPageService;

@ActiveProfiles("uzpdev")
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EnzymeportalWebsiteApplicationTests {

    @Autowired
    protected EntryPageService entryPageService;

    @Test
    public void contextLoads() {
    }

    @Test
    void injectedComponentsAreNotNull() {

        assertThat(entryPageService).isNotNull();
    }

}

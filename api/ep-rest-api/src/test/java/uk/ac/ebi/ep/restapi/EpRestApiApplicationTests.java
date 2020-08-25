package uk.ac.ebi.ep.restapi;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.ac.ebi.ep.restapi.hateoas.EnzymeModelAssembler;
import uk.ac.ebi.ep.restapi.hateoas.PaginationModelAssembler;
import uk.ac.ebi.ep.restapi.hateoas.ProteinResourceAssembler;
import uk.ac.ebi.ep.restapi.hateoas.ProteinSummaryModelAssembler;
import uk.ac.ebi.ep.restapi.service.EnzymeApiService;
import uk.ac.ebi.ep.restapi.service.ProteinApiService;
import uk.ac.ebi.ep.restapi.service.ResourceService;

@SpringBootTest
class EpRestApiApplicationTests {

    @Autowired
    private EnzymeModelAssembler enzymeModelAssembler;
    @Autowired
    private EnzymeApiService enzymeApiService;
    @Autowired
    private ProteinSummaryModelAssembler proteinSummaryModelAssembler;
    @Autowired
    private ProteinApiService proteinApiService;
    @Autowired
    private ProteinResourceAssembler proteinResourceAssembler;
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PaginationModelAssembler pageModelAssembler;

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(enzymeModelAssembler).isNotNull();
        assertThat(enzymeApiService).isNotNull();
        assertThat(proteinSummaryModelAssembler).isNotNull();
        assertThat(proteinApiService).isNotNull();
        assertThat(proteinResourceAssembler).isNotNull();
        assertThat(resourceService).isNotNull();
        assertThat(pageModelAssembler).isNotNull();

    }

}

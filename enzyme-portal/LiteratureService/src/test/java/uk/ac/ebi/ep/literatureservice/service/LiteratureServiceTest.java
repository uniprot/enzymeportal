package uk.ac.ebi.ep.literatureservice.service;

import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.literatureservice.config.PmcConfig;
import uk.ac.ebi.ep.literatureservice.dto.CitationLabel;
import uk.ac.ebi.ep.literatureservice.dto.LabelledCitation;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PmcConfig.class})
public class LiteratureServiceTest {

    @Autowired
    private LiteratureService literatureService;

    /**
     * Test of getCitationsByAccession method, of class LiteratureServiceTest.
     */
    @Test
    public void testGetCitationsByAccession() {

        String accession = "P42346";
        int limit = 20;

        List<LabelledCitation> result = literatureService.getCitationsByAccession(accession, limit);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThan(1)));
        assertThat(result, hasSize(lessThanOrEqualTo(limit)));

    }

    /**
     * in response to an issue where disease was found for mouse (O88587) but
     * none for human (P21964)
     * https://www.ebi.ac.uk/europepmc/webservices/rest/search?query=UNIPROT_PUBS:O88587&pageSize=50&resulttype=core
     */
    @Test
    public void testGetCitationsByAccession_validate_Label() {

        String accession = "O88587";

        int limit = 50;

        List<LabelledCitation> result = literatureService.getCitationsByAccession(accession, limit);
        assertNotNull(result);
        assertThat(result, hasSize(greaterThan(1)));
        assertThat(result, hasSize(lessThanOrEqualTo(limit)));
        List<LabelledCitation> hasDisease = result.stream()
                .filter(x -> x.getLabels().contains(CitationLabel.DISEASES))
                .collect(Collectors.toList());

        assertThat(hasDisease, hasSize(greaterThan(1)));

    }

    /**
     * Test of getCitationsBySearchTerm method, of class LiteratureServiceTest.
     */
    @Test
    public void testGetCitationsBySearchTerm() {
        String term = "cGMP-specific 3',5'-cyclic phosphodiesterase";

        int estimatedResultSize = 2;

        EuropePMC result = literatureService.getCitationsBySearchTerm(term);

        assertNotNull(result);
        assertThat(result.getResultList().getResult(), hasSize(greaterThan(1)));
        assertThat(result.getResultList().getResult(), hasSize(greaterThanOrEqualTo(estimatedResultSize)));

        Result r = result.getResultList().getResult().stream().findFirst().orElse(new Result());
        LabelledCitation citation = new LabelledCitation(r, CitationLabel.ENZYME);
        assertNotNull(citation.getCitation());
        assertThat(citation.getLabels(), hasSize(1));
        citation.getLabels().forEach(s -> System.out.println(" dat " + s.getDisplayText()));
    }

}

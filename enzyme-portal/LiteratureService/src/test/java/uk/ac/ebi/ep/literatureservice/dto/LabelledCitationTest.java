package uk.ac.ebi.ep.literatureservice.dto;

import java.util.EnumSet;
import java.util.Set;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.literatureservice.config.PmcConfig;
import uk.ac.ebi.ep.literatureservice.model.EuropePMC;
import uk.ac.ebi.ep.literatureservice.model.Result;
import uk.ac.ebi.ep.literatureservice.service.LiteratureService;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PmcConfig.class})
public class LabelledCitationTest {

    @Autowired
    private LiteratureService literatureService;

    /**
     * Test of getCitation method, of class LabelledCitation.
     */
    @Test
    public void testGetCitation() {
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

        citation.addLabel(CitationLabel.PROTEIN_STRUCTURE);
        assertThat(citation.getLabels(), hasSize(2));
        Set<CitationLabel> labels = EnumSet.of(CitationLabel.DISEASES, CitationLabel.PROTEIN_STRUCTURE);
        citation.addLabels(labels);

        assertThat(citation.getLabels(), hasSize(3));

    }

    /**
     * Test of getLabels method, of class LabelledCitation.
     */
    @Test
    public void testGetLabels() {
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

    }

    /**
     * Test of compareTo method, of class LabelledCitation.
     */
    @Test
    public void testCompareTo() {
        int limit = 5;
        String acc1 = "O76074";
        String acc2 = "O54735";

        EuropePMC pmc1 = literatureService.findCitationsByUniprotAccession(acc1, limit);
        assertNotNull(pmc1);
        Result r1 = pmc1.getResultList().getResult().stream().findFirst().orElse(new Result());
        assertNotNull(r1.getDateOfCreation());
        assertNotNull(r1.getTitle());
        assertNotNull(r1.getSource());
        assertNotNull(r1.getFirstPublicationDate());
        assertNotNull(r1.getId());
        LabelledCitation citation = new LabelledCitation(r1, CitationLabel.ENZYME);
        citation.addLabel(CitationLabel.PROTEIN_STRUCTURE);
        citation.addLabel(CitationLabel.DISEASES);

        EuropePMC pmc2 = literatureService.findCitationsByUniprotAccession(acc2, limit);
        assertNotNull(pmc2);
        Result r2 = pmc2.getResultList().getResult().stream().findFirst().orElse(new Result());

        Set<CitationLabel> labels = EnumSet.of(CitationLabel.ENZYME, CitationLabel.DISEASES, CitationLabel.PROTEIN_STRUCTURE);
        LabelledCitation other = new LabelledCitation(r2, labels);

        int compare = citation.compareTo(other);

        assertEquals(-3, compare);

    }

}

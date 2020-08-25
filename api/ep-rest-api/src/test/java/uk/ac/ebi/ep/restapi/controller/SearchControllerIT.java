package uk.ac.ebi.ep.restapi.controller;

import java.net.URI;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import static org.springframework.hateoas.client.Hop.rel;
import org.springframework.hateoas.client.Traverson;
import org.springframework.hateoas.server.core.TypeReferences;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import uk.ac.ebi.ep.restapi.model.EnzymeModel;
import uk.ac.ebi.ep.restapi.model.ProteinModel;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchControllerIT {

    @LocalServerPort
    private int port;

    private Traverson getTraversonClient(String endpoint) {
        String url = "http://localhost:" + this.port + "/enzymeportal/rest/" + endpoint;
        return new Traverson(URI.create(url), MediaTypes.HAL_JSON);
    }

    private void enzymeModelTest(String endPoint) {
        Traverson traverson = getTraversonClient(endPoint);
        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        PagedModel<EnzymeModel> enzymes = client
                .toObject(new TypeReferences.PagedModelType<EnzymeModel>() {
                });

        assertNotNull(enzymes);
        assertNotNull(enzymes.getContent());
        assertTrue(enzymes.hasLinks());

        assertTrue(enzymes.hasLink(IanaLinkRelations.FIRST));
        assertTrue(enzymes.hasLink(IanaLinkRelations.SELF));
        assertTrue(enzymes.hasLink(IanaLinkRelations.NEXT));
        assertTrue(enzymes.hasLink(IanaLinkRelations.LAST));

        assertNotNull(enzymes.getMetadata());
        assertTrue(enzymes.getMetadata().getNumber() == 0);
        assertTrue(enzymes.getMetadata().getTotalPages() > 1);
        assertTrue(enzymes.getMetadata().getTotalElements() > 10);
        assertTrue(enzymes.getMetadata().getSize() == 10);

        List<String> enzymeNames = client.toObject("$._embedded.enzymes.[*].enzymeName");
        assertThat(enzymeNames).hasSizeGreaterThanOrEqualTo(10);

        List<String> associatedProteinsAccession = client.toObject("$._embedded.enzymes.[*].associatedProteins._embedded.proteins[*].accession");

        assertThat(associatedProteinsAccession).hasSizeGreaterThanOrEqualTo(2);
    }

    /**
     * Test of findEnzymesByKeyword method, of class SearchController.
     */
    @Test
    public void testFindEnzymesByKeyword() {
        log.info("testFindEnzymesByKeyword");
        String query = "pyruvate";
        String endPoint = "search/?page=0&pageSize=10&query=" + query.trim();
        enzymeModelTest(endPoint);

    }

    /**
     * Test of findAssociatedProteins method, of class SearchController.
     */
    @Test
    public void testFindAssociatedProteins() {
        log.info("testFindAssociatedProteins");

        String endPoint = "search/proteins?pageSize=10&query=pyruvate%20kinase&ec=2.7.1.40";

        Traverson traverson = getTraversonClient(endPoint);

        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        TypeReferences.CollectionModelType<ProteinModel> collectionModelType = new TypeReferences.CollectionModelType<ProteinModel>() {
        };

        CollectionModel<ProteinModel> proteinModel = traverson.
                follow(rel("self")).
                toObject(collectionModelType);

        assertTrue(proteinModel.hasLink(IanaLinkRelations.SELF));

        List<String> accessions = client.toObject("$._embedded.proteinSummaries.[*].accession");

        assertThat(accessions).hasSizeGreaterThanOrEqualTo(2);
    }

    /**
     * Test of findEnzymesByCofactor method, of class SearchController.
     */
    @Test
    public void testFindEnzymesByCofactor() {
        log.info("testFindEnzymesByCofactor");

        String endPoint = "search/cofactors/CHEBI:57925?page=0&pageSize=10";
        Traverson traverson = getTraversonClient(endPoint);
        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        PagedModel<EnzymeModel> enzymes = client
                .toObject(new TypeReferences.PagedModelType<EnzymeModel>() {
                });

        assertNotNull(enzymes);
        assertNotNull(enzymes.getContent());
        assertTrue(enzymes.hasLinks());

        assertTrue(enzymes.hasLink(IanaLinkRelations.FIRST));
        assertTrue(enzymes.hasLink(IanaLinkRelations.SELF));
        assertTrue(enzymes.hasLink(IanaLinkRelations.NEXT));
        assertTrue(enzymes.hasLink(IanaLinkRelations.LAST));

        assertNotNull(enzymes.getMetadata());
        assertTrue(enzymes.getMetadata().getNumber() == 0);
        assertTrue(enzymes.getMetadata().getTotalPages() >= 2);
        assertTrue(enzymes.getMetadata().getTotalElements() >= 14);
        assertTrue(enzymes.getMetadata().getSize() == 10);

        List<String> enzymeNames = client.toObject("$._embedded.enzymes.[*].enzymeName");
        assertThat(enzymeNames).hasSizeGreaterThanOrEqualTo(10);

        List<String> associatedProteinsAccession = client.toObject("$._embedded.enzymes.[*].associatedProteins._embedded.proteins[*].accession");

        assertThat(associatedProteinsAccession).hasSizeGreaterThanOrEqualTo(2);
    }

    /**
     * Test of findEnzymesByDisease method, of class SearchController.
     */
    @Test
    public void testFindEnzymesByDisease() {
        log.info("testFindEnzymesByDisease");

        String endPoint = "search/diseases/114480?page=0&pageSize=10";
        Traverson traverson = getTraversonClient(endPoint);
        Traverson.TraversalBuilder client = traverson.follow(IanaLinkRelations.SELF.value());

        PagedModel<EnzymeModel> enzymes = client
                .toObject(new TypeReferences.PagedModelType<EnzymeModel>() {
                });

        assertNotNull(enzymes);
        assertNotNull(enzymes.getContent());
        assertTrue(enzymes.hasLinks());

        assertTrue(enzymes.hasLink(IanaLinkRelations.SELF));

        assertNotNull(enzymes.getMetadata());
        assertTrue(enzymes.getMetadata().getNumber() == 0);
        assertTrue(enzymes.getMetadata().getTotalPages() >= 1);
        assertTrue(enzymes.getMetadata().getTotalElements() >= 5);
        assertTrue(enzymes.getMetadata().getSize() == 10);

        List<String> enzymeNames = client.toObject("$._embedded.enzymes.[*].enzymeName");
        assertThat(enzymeNames).hasSizeGreaterThanOrEqualTo(6);

        List<String> associatedProteinsAccession = client.toObject("$._embedded.enzymes.[*].associatedProteins._embedded.proteins[*].accession");

        assertThat(associatedProteinsAccession).hasSizeGreaterThanOrEqualTo(2);
    }

    /**
     * Test of findEnzymesByMetabolight method, of class SearchController.
     */
    @Test
    public void testFindEnzymesByMetabolight() {
        log.info("testFindEnzymesByMetabolight");

        String endPoint = "search/metabolites/MTBLC57925?page=0&pageSize=10";
        enzymeModelTest(endPoint);
    }

    /**
     * Test of findEnzymesByTaxonomy method, of class SearchController.
     */
    @Test
    public void testFindEnzymesByTaxonomy() {
        log.info("testFindEnzymesByTaxonomy");

        String endPoint = "search/organisms/9606?page=0&pageSize=10";
        enzymeModelTest(endPoint);
    }

    /**
     * Test of findEnzymesByPathways method, of class SearchController.
     */
    @Test
    public void testFindEnzymesByPathways() {
        log.info("testFindEnzymesByPathways");
        String endPoint = "search/pathways/R-211981?page=0&pageSize=10";
        enzymeModelTest(endPoint);
    }

}

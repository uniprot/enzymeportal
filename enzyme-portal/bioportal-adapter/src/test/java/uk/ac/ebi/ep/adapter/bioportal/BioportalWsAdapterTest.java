package uk.ac.ebi.ep.adapter.bioportal;

import java.util.Collection;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Entity;

public class BioportalWsAdapterTest {

	private BioportalWsAdapter bwa;
	
	@Before
	public void setUp() throws Exception {
		bwa = new BioportalWsAdapter();
		bwa.setConfig(new BioportalConfig()); // defaults
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDisease() throws BioportalAdapterException {
		Disease disease = bwa.getDisease("sarcoidosis");
		assertNotNull(disease);
        // Some EFO IDs point now to http://www.orphanet.org/rdfns#pat_id_###
//		assertEquals("EFO_0000690", disease.getId());
//		assertNotNull(disease.getDescription());
//		assertTrue(disease.getDescription().length() > 0);
		
		disease = bwa.getDisease("inflammatory bowel diseases");
		assertNotNull(disease);
		assertEquals("EFO_0003767", disease.getId());
		assertNotNull(disease.getDescription());
		assertTrue(disease.getDescription().length() > 0);
	}
	
	@Test
	public void testSearchConcept() throws Exception {
		Entity disease = bwa.searchConcept(BioportalOntology.EFO,
				"sarcoidosis", Disease.class, false);
		assertNotNull(disease);
        // Some EFO IDs point now to http://www.orphanet.org/rdfns#pat_id_###
//		assertEquals("EFO_0000690", disease.getId());
		assertNull(disease.getDescription());
	}
	
	@Test
	public void testSearchConceptComplete() throws Exception {
		Entity disease = bwa.searchConcept(BioportalOntology.EFO,
				"sarcoidosis", Disease.class, true);
		assertNotNull(disease);
        // Some EFO IDs point now to http://www.orphanet.org/rdfns#pat_id_###
//		assertEquals("EFO_0000690", disease.getId());
//		assertNotNull(disease.getDescription());
//		assertTrue(disease.getDescription().length() > 0);
	}
    
    @Test
    public void testSearchConcept2() throws Exception {
        Collection<Entity> diseases = bwa.searchConcept(
                BioportalOntology.forDiseases(), "Piebaldism",
                Disease.class, false);
        assertNotNull(diseases);
        assertTrue(diseases.size() > 1); // 3 of them from EFO, OMIM and MeSH
        
        diseases = bwa.searchConcept(
                BioportalOntology.forDiseases(), "D016116",
                Disease.class, false);
        assertNotNull(diseases);
        assertFalse(diseases.size() > 1); // only 1 from MeSH
    }

}

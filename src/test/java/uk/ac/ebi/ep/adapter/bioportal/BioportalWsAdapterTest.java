package uk.ac.ebi.ep.adapter.bioportal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.adapter.bioportal.BioportalWsAdapter.BioportalOntology;
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
	public void testGetDiseaseByName() throws BioportalAdapterException {
		Disease disease = bwa.getDiseaseByName("sarcoidosis");
		assertNotNull(disease);
		assertEquals("EFO_0000690", disease.getId());
		assertNotNull(disease.getDescription());
		assertTrue(disease.getDescription().length() > 0);
	}
	
	@Test
	public void testSearchConcept() throws Exception {
		Entity disease = bwa.searchConcept(BioportalOntology.EFO,
				"sarcoidosis", Disease.class, false);
		assertNotNull(disease);
		assertEquals("EFO_0000690", disease.getId());
		assertNull(disease.getDescription());
	}
	
	@Test
	public void testSearchConceptComplete() throws Exception {
		Entity disease = bwa.searchConcept(BioportalOntology.EFO,
				"sarcoidosis", Disease.class, true);
		assertNotNull(disease);
		assertEquals("EFO_0000690", disease.getId());
		assertNotNull(disease.getDescription());
		assertTrue(disease.getDescription().length() > 0);
	}

}

package uk.ac.ebi.ep.adapter.bioportal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.enzyme.model.Disease;

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
	}

}

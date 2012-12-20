package uk.ac.ebi.ep.adapter.reactome;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.adapter.reactome.ReactomeWsCallable.ReactomeClass;
import uk.ac.ebi.ep.enzyme.model.Pathway;

public class ReactomeWsCallableTest {

	private ReactomeWsCallable rwc;
	
	@Before
	public void setUp() throws Exception {
		ReactomeConfig config = new ReactomeConfig();
		config.setUseProxy(true);
		config.setWsBaseUrl(ReactomeWsCallable.WS_BASE_URL);
		rwc = new ReactomeWsCallable(config, null);
	}

	@Test
	public void testGetDescription() throws ReactomeServiceException {
		String desc;
		desc = rwc.getDescription(ReactomeClass.Reaction, "REACT_21342");
		System.out.println(desc);
		//assertNotNull(desc);
		desc = rwc.getDescription(ReactomeClass.Reaction, "REACT_75928");
		System.out.println(desc);
		//assertNotNull(desc);
		desc = rwc.getDescription(ReactomeClass.Pathway, "REACT_15295");
		System.out.println(desc);
		//assertNotNull(desc);
	}
	
	@Test
	public void testGetPathway() throws Exception {
		Pathway pathway;
		pathway = rwc.getPathway("REACT_15295");
		assertNotNull(pathway.getDescription());
		
		pathway = rwc.getPathway("REACT_604");
		assertNotNull(pathway.getDescription());
		assertNotNull(pathway.getImage());
		
		pathway = rwc.getPathway("REACT_15518");
		assertNull(pathway.getDescription());
	}

}

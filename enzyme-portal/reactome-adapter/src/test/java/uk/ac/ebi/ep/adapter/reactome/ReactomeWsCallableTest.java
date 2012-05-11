package uk.ac.ebi.ep.adapter.reactome;


import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.adapter.reactome.ReactomeWsCallable.ReactomeClass;

public class ReactomeWsCallableTest {

	private ReactomeWsCallable rwc;
	
	@Before
	public void setUp() throws Exception {
		ReactomeConfig config = new ReactomeConfig();
		config.setUseProxy(true);
		config.setWsBaseUrl(ReactomeWsCallable.WS_BASE_URL);
		rwc = new ReactomeWsCallable(config);
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

}

package uk.ac.ebi.ep.adapter.uniprot;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UniprotWsSummaryCallableTest {

	private UniprotWsSummaryCallable callable;
	
	@Before
	public void setUp() throws Exception {
		// We are just testing an utility method.
		callable = new UniprotWsSummaryCallable(null, null, null, null, null);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testParsePdbCodes() {
		String pdbCodes = null;
		String pdbMethods = "";
		List<String> result = callable.parsePdbCodes(pdbCodes, pdbMethods);
		assertNull(result);
		
		pdbCodes = "";
		result = callable.parsePdbCodes(pdbCodes, pdbMethods);
		assertNull(result);
		
		pdbCodes = "1LRB;2OUN;2OUP;2OUQ;2OUR;2OUS;2OUU;2OUV;2OUY;2WEY;2Y0J;" +
				"2ZMF;3SN7;3SNI;3SNL;3UI7;";
		pdbMethods = null;
		result = callable.parsePdbCodes(pdbCodes, pdbMethods);
		assertNull(result);
		
		pdbMethods = "Model (1); X-ray crystallography (15)";
		result = callable.parsePdbCodes(pdbCodes, pdbMethods);
		assertNotNull(result);
		assertEquals(15, result.size());
		assertEquals("2oun", result.get(0));
		
		pdbMethods = "Model (2); X-ray crystallography (14)";
		result = callable.parsePdbCodes(pdbCodes, pdbMethods);
		assertNotNull(result);
		assertEquals(14, result.size());
		assertEquals("2oup", result.get(0));
		
		pdbMethods = "Model (16)";
		result = callable.parsePdbCodes(pdbCodes, pdbMethods);
		assertNull(result);
		
		pdbMethods = "X-ray crystallography (16)";
		result = callable.parsePdbCodes(pdbCodes, pdbMethods);
		assertNotNull(result);
		assertEquals(16, result.size());
		assertEquals("1lrb", result.get(0));
	}

}

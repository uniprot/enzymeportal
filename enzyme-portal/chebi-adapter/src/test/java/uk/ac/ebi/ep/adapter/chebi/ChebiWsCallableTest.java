package uk.ac.ebi.ep.adapter.chebi;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.ep.enzyme.model.Molecule;

//@Ignore
public class ChebiWsCallableTest {

	private ChebiWsCallable cwc;
	private ChebiConfig config;
	
	@Before
	public void setUp() throws Exception {
		config = new ChebiConfig();
		cwc = new ChebiWsCallable(config, null, null);
	}

	@After
	public void tearDown() throws Exception {
	}

//	@Test
//	public void testCall() throws Exception {
//		ChebiWsCallable callable = new ChebiWsCallable(
//				config, "caffeine", SearchCategory.ALL_NAMES);
//		Molecule molecule = callable.call();
//		//assertNotNull(molecule);
//		assertEquals("caffeine", molecule.getName());
//                assertEquals("CAFFEINE CITRATE", molecule.getName());
//		assertEquals("C14H18N4O9", molecule.getFormula());
//		assertEquals("CHEBI:774520", molecule.getId());
//	}

	@Test
	public void testGetMoleculeByChebiId() throws Exception {
		Molecule molecule = cwc.getMoleculeByChebiId("CHEBI:15377");
		assertNotNull(molecule);
		assertEquals("water", molecule.getName());
		assertEquals("H2O", molecule.getFormula());
		assertEquals("CHEBI:15377", molecule.getId());
	}

	@Test
	public void testGetMoleculeByXref() throws Exception {
		Molecule molecule = cwc.getMoleculeByXref("MTO"); // PDBeChem
		assertNotNull(molecule);
		assertEquals("water", molecule.getName());
		assertEquals("H2O", molecule.getFormula());
		assertEquals("CHEBI:15377", molecule.getId());
	}

	@Test
	public void testGetMoleculeByName() throws Exception {
		Molecule molecule = cwc.getMoleculeByName("water");
		assertNotNull(molecule);
		assertEquals("water", molecule.getName());
		assertEquals("H2O", molecule.getFormula());
		assertEquals("CHEBI:15377", molecule.getId());
	}

}

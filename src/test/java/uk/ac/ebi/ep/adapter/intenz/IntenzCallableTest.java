package uk.ac.ebi.ep.adapter.intenz;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.adapter.intenz.IntenzCallable.GetCofactorsCaller;
import uk.ac.ebi.ep.enzyme.model.Molecule;

public class IntenzCallableTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCofactors() throws Exception {
		GetCofactorsCaller cofactorsCaller = new GetCofactorsCaller(
				"ftp://ftp.ebi.ac.uk/pub/databases/intenz/xml/ASCII/EC_1/EC_1.1/EC_1.1.1/EC_1.1.1.1.xml");
		Collection<Molecule> cofactors = cofactorsCaller.call();
		assertEquals(2, cofactors.size());
	}

}

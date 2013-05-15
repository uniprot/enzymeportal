package uk.ac.ebi.ep.mm.app;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ep.mm.Entry;

import static org.junit.Assert.*;

public class UniprotSaxParserTest {

	UniprotSaxParser parser;
	
	@Before
	public void init(){
		parser = new UniprotSaxParser();
	}

	@Test
	public void testCharacters() throws Exception {
		// isEntry, isAccession, isEntryName... flags are all false
		parser.characters("foo".toCharArray(), 0, 3);
		// we don't expect nothing to be recorded:
		assertEquals(0, parser.currentChars.length());
		assertEquals(0, parser.accessions.size());
		assertEquals(0, parser.entryNames.size());
		assertEquals(0, parser.ecs.size());
		assertNull(parser.orgSciName);
		parser.isEntry = true;
		parser.characters("foo".toCharArray(), 0, 3);
		// nothing yet, only recording for interesting elements:
		assertEquals(0, parser.currentChars.length());
		assertEquals(0, parser.accessions.size());
		assertEquals(0, parser.entryNames.size());
		assertEquals(0, parser.ecs.size());
		assertNull(parser.orgSciName);
		parser.isAccession = true;
		parser.characters("foo".toCharArray(), 0, 3);
		// Now we should get something:
		assertEquals("foo", parser.currentChars.toString());
		parser.characters("bar".toCharArray(), 0, 3);
		assertEquals("foobar", parser.currentChars.toString());
		parser.characters("baz".toCharArray(), 0, 3);
		assertEquals("foobarbaz", parser.currentChars.toString());
	}

	@Test
	public void testStartElement() throws Exception {
		parser.startElement(null, "uniprot", null, null);
		assertFalse(parser.isEntry);
		assertFalse(parser.isAccession);
		assertFalse(parser.isEntryName);
		assertFalse(parser.isDbRef);
		assertFalse(parser.isOrgSciName);
		parser.startElement(null, "entry", null, null);
		assertTrue(parser.isEntry);
		assertFalse(parser.isAccession);
		assertFalse(parser.isEntryName);
		assertFalse(parser.isDbRef);
		assertFalse(parser.isOrgSciName);
		parser.startElement(null, "accession", null, null);
		assertFalse(parser.isEntry);
		assertTrue(parser.isAccession);
		assertFalse(parser.isEntryName);
		assertFalse(parser.isDbRef);
		assertFalse(parser.isOrgSciName);
		parser.startElement(null, "foo", null, null);
		assertFalse(parser.isEntry);
		assertFalse(parser.isAccession);
		assertFalse(parser.isEntryName);
		assertFalse(parser.isDbRef);
		assertFalse(parser.isOrgSciName);
	}

	@Test
	public void testEndElement() throws Exception {
		parser.startElement(null, "uniprot", null, null);
		assertEquals(0, parser.accessions.size());
		parser.startElement(null, "entry", null, null);
		assertEquals(0, parser.accessions.size());
		parser.startElement(null, "accession", null, null);
		assertEquals(0, parser.accessions.size());
		parser.characters("foo".toCharArray(), 0, 3);
		assertEquals(0, parser.accessions.size());
		parser.endElement(null, "accession", null);
		assertEquals(1, parser.accessions.size());
		parser.startElement(null, "accession", null, null);
		assertEquals(1, parser.accessions.size());
		parser.characters("bar".toCharArray(), 0, 3);
		assertEquals(1, parser.accessions.size());
		parser.endElement(null, "accession", null);
		assertEquals(2, parser.accessions.size());
	}

    @Test
    public void testSearchMoleculeInChEBI(){
        Entry molecule = null;

        molecule = parser.searchMoleculeInChEBI("foo");
        assertNull(molecule);

        molecule = parser.searchMoleculeInChEBI("foo (ZZZ)");
        assertNull(molecule);

        molecule = parser.searchMoleculeInChEBI("ATP");
        assertNotNull(molecule);
        assertEquals("CHEBI:30616", molecule.getEntryId());

        // name matches:
        molecule =
                parser.searchMoleculeInChEBI("3,4-dichloroisocoumarin (DCI)");
        assertNotNull(molecule);
        assertEquals("CHEBI:132349", molecule.getEntryId());

        // only acronym matches:
        molecule = parser.searchMoleculeInChEBI("foo zzz (ATP)");
        assertNotNull(molecule);
        assertEquals("CHEBI:30616", molecule.getEntryId());

    }
}

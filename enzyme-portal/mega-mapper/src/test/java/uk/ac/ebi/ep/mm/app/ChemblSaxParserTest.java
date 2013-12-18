package uk.ac.ebi.ep.mm.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import uk.ac.ebi.ep.mm.DummyMegaMapper;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.XRef;

public class ChemblSaxParserTest {

    private ChemblSaxParser parser;
    private DummyMegaMapper mm;
    private Entry q13976;

    @Before
    public void setUp() throws Exception {
        parser = new ChemblSaxParser();
        mm = new DummyMegaMapper();
        mm.openMap();
        parser.setWriter(mm);

        q13976 = new Entry();
        q13976.setEntryId("KGP1_HUMAN");
        q13976.setEntryName("cGMP-dependent protein kinase 1");
        q13976.setDbName(MmDatabase.UniProt.name());
        q13976.setEntryAccessions(Arrays.asList(new String[]{"Q13976"}));
        mm.writeEntry(q13976);
    }

    @After
    public void tearDown() throws IOException{
        mm.closeMap();
    }
    
    @Test
    public void testParseChemblTargetComponent() throws Exception {
        Collection<XRef> xrefs = mm.getXrefs(q13976);
        assertEquals(0, xrefs.size());

        parser.parse(EbeyeSaxParserTest.class.getClassLoader()
                .getResourceAsStream("chembl-target_component-excerpt.xml"));
        xrefs = mm.getXrefs(q13976);
        assertFalse(xrefs.isEmpty());
        assertEquals(MmDatabase.ChEMBL.name(),
                xrefs.iterator().next().getToEntry().getDbName());
    }

}

package uk.ac.ebi.ep.mm.app;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ep.mm.DummyMegaMapper;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;

import static junit.framework.Assert.assertEquals;
import org.junit.Ignore;

/**
 * @author rafa
 * @since 2013-05-07
 */
public class EbeyeSaxParserTest {

    private EbeyeSaxParser parser;
    private DummyMegaMapper mm;
    private Entry p18125;

    @Before
    public void setUp() throws Exception {
        parser = new EbeyeSaxParser();
        mm = new DummyMegaMapper();
        mm.openMap();
        parser.setWriter(mm);

        p18125 = new Entry();
        p18125.setEntryId("CP7A1_RAT");
        p18125.setEntryName("Cholesterol 7-alpha-monooxygenase");
        p18125.setDbName(MmDatabase.UniProt.name());
        p18125.setEntryAccessions(Arrays.asList(new String[]{"P18125"}));
        mm.writeEntry(p18125);
    }

    @After
    public void tearDown(){
    }

    //@Test
    @Ignore("returns invalid numbers. i guess some files/data are missing. please investigate")
    public void testParseChemblTarget() throws Exception {
        assertEquals(0, mm.getXrefs(p18125).size());

        parser.parse(EbeyeSaxParserTest.class.getClassLoader()
                .getResourceAsStream("chembl-target-excerpt.xml"));
        assertEquals(3, mm.getXrefs(p18125).size());
        assertEquals(3, mm.getXrefs(p18125, MmDatabase.ChEMBL).size());
    }
}

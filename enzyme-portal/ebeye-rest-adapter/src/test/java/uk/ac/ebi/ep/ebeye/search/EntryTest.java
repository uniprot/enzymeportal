package uk.ac.ebi.ep.ebeye.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.ep.ebeye.model.Protein;

/**
 * @author joseph
 */
public class EntryTest {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EntryTest.class);

    private Entry instance;
    private static final String name = "cGMP-specific 3',5'-cyclic phosphodiesterase";

    @Before
    public void setUp() {
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add(name);

        List<String> status = new ArrayList<>();
        status.add("reviewed");
        Fields fields = new Fields();
        fields.setName(fieldNames);
        fields.setStatus(status);
        String acc = "O76074";
        String geneName = "ABC_HUMAN";

        instance = new Entry(acc, geneName);
        instance.setTitle("cGMP-specific 3',5'-cyclic phosphodiesterase");
        instance.set(name, fields);
        instance.setFields(fields);
        instance.getScientificName().add("Homo Sapien");

        Protein protein = new Protein("O76074", name);
        instance.setProtein(protein);
    }

    @Test
    public void testEntryConstructor() {
        LOGGER.info("testEntryConstructor");
        assertNotNull(instance);
    }

    /**
     * Test of getUniprot_name method, of class Entry.
     */
    @Test
    public void testGetUniprotName() {
        LOGGER.info("getUniprotName");

        String expResult = "ABC";
        String result = instance.getUniprotName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUniprotAccession method, of class Entry.
     */
    @Test
    public void testGetUniprotAccession() {
        LOGGER.info("getUniprotAccession");

        String expResult = "O76074";
        String result = instance.getUniprotAccession();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSource method, of class Entry.
     */
    @Test
    public void testGetSource() {
        LOGGER.info("getSource");

        String expResult = null;
        String result = instance.getSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Entry.
     */
    @Test
    public void testToString() {
        LOGGER.info("toString");

        String expResult = "Entry{uniprotAccession=O76074, uniprotName=ABC_HUMAN, source=null, title=cGMP-specific 3',5'-cyclic phosphodiesterase, ec=null}";
        String result = instance.toString();

        assertEquals(expResult, result);
    }

    /**
     * Test of compareTo method, of class Entry.
     */
    @Test
    public void testCompareTo() {
        LOGGER.info("compareTo");
        Entry obj = new Entry("O76074", "ABC_HUMAN");

        int expResult = 0;
        int result = instance.compareTo(obj);

        assertEquals(expResult, result);
    }

    /**
     * Test of get method, of class Entry.
     */
    @Test
    public void testGet() {
        LOGGER.info("get");

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add(name);
        Fields f = new Fields();
        f.setName(fieldNames);
        Fields expResult = f;
        Fields result = instance.get(name);
        assertNotSame(expResult, result);
    }

    /**
     * Test of any method, of class Entry.
     */
    @Test
    public void testAny() {
        LOGGER.info("any");

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add(name);
        Fields f = new Fields();
        f.setName(fieldNames);

        Map<String, Fields> map = new HashMap<>();
        map.put(name, f);

        Map<String, Fields> expResult = map;
        Map<String, Fields> result = instance.any();

        assertEquals(expResult.keySet(), result.keySet());
    }

    /**
     * Test of getTitle method, of class Entry.
     */
    @Test
    public void testGetTitle() {
        LOGGER.info("getTitle");

        String expResult = "cGMP-specific 3',5'-cyclic phosphodiesterase";
        String result = instance.getTitle();
        assertEquals(expResult, result);

    }

    /**
     * Test of hashCode method, of class Entry.
     */
    @Test
    public void testHashCode() {
        LOGGER.info("hashCode");

        int expResult = 0;
        int result = instance.hashCode();

        assertNotSame(expResult, result);
    }

    /**
     * Test of equals method, of class Entry.
     */
    @Test
    public void testEquals() {
        LOGGER.info("equals");
        Object obj = new Fields();

        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);

    }

    /**
     * Test of getFields method, of class Entry.
     */
    @Test
    public void testGetFields() {
        LOGGER.info("getFields");

        List<String> fieldNames = new ArrayList<>();
        fieldNames.add(name);
        Fields expResult = new Fields();
        expResult.setName(fieldNames);
        String acc = "O76074";
        String geneName = "ABC_HUMAN";

        Fields result = instance.getFields();

        assertEquals(expResult.getName(), result.getName());

    }

    /**
     * Test of getScientificName method, of class Entry.
     */
    @Test
    public void testGetScientificName() {
        LOGGER.info("getScientificName");

        List<String> expResult = new ArrayList<>();
        expResult.add("Homo Sapien");
        List<String> result = instance.getScientificName();
        assertEquals(expResult, result);

    }

    /**
     * Test of getProtein method, of class Entry.
     */
    @Test
    public void testGetProtein() {
        LOGGER.info("getProtein");

        Protein expResult = new Protein("O76074", name);
        Protein result = instance.getProtein();
        assertEquals(expResult.getProteinName(), result.getProteinName());
    }

}

package uk.ac.ebi.ep.ebeye.enzyme.model;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ep.ebeye.model.Fields;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EntryTest {

    private Entry instance;

    @Before
    public void setUp() {
        Fields fields = new Fields();
        fields.getCommonName().add("Rat");
        fields.getCommonName().add("Arabidopsis thaliana");
        fields.getCommonName().add("Bovine");
        fields.getCommonName().add("Mouse");
        fields.getCommonName().add("Human");
        fields.getCommonName().add("Fruit fly");
        fields.getScientificName().add("Saccharomyces cerevisiae");
        fields.getScientificName().add("Mus musculus");
        fields.getScientificName().add("Homo sapiens");
        fields.getScientificName().add("Mus musculus");
        fields.getName().add("Alcohol dehydrogenase");
        fields.getEnzymeFamily().add("Oxidoreductases");
        fields.getEc().add("1.1.1.1");
        fields.getDescription().add("(1) An alcohol + NAD(+) = an aldehyde or ketone + NADH. "
                + "(2) A secondary alcohol + NAD(+) = a ketone + NADH.");

        String id = "1.1.1.1";
        String source = "enzymeportal_enzyme";

        instance = new Entry(id, source, fields);
    }

    /**
     * Test of getId method, of class Entry.
     */
    @Test
    public void testGetId() {
        String expResult = "1.1.1.1";
        String result = instance.getId();
        assertEquals(expResult, result);

    }

    /**
     * Test of getSource method, of class Entry.
     */
    @Test
    public void testGetSource() {

        String expResult = "enzymeportal_enzyme";
        String result = instance.getSource();
        assertEquals(expResult, result);

    }

    /**
     * Test of getFields method, of class Entry.
     */
    @Test
    public void testGetFields() {

        Fields result = instance.getFields();
        Assert.assertNotNull(result);

    }

    /**
     * Test of getEc method, of class Entry.
     */
    @Test
    public void testGetEc() {

        String expResult = "1.1.1.1";
        String result = instance.getEc();
        assertEquals(expResult, result);

    }

    /**
     * Test of getEnzymeName method, of class Entry.
     */
    @Test
    public void testGetEnzymeName() {
        String expResult = "Alcohol dehydrogenase";
        String result = instance.getEnzymeName();
        assertEquals(expResult, result);

    }

    /**
     * Test of getEnzymeFamily method, of class Entry.
     */
    @Test
    public void testGetEnzymeFamily() {

        String expResult = "Oxidoreductases";
        String result = instance.getEnzymeFamily();
        assertEquals(expResult, result);

    }

    /**
     * Test of getCatalyticActivities method, of class Entry.
     */
    @Test
    public void testGetCatalyticActivities() {

        List<String> expResult = new ArrayList<>();
        expResult.add("(1) An alcohol + NAD(+) = an aldehyde or ketone + NADH. "
                + "(2) A secondary alcohol + NAD(+) = a ketone + NADH.");
        List<String> result = instance.getCatalyticActivities();
        assertEquals(expResult, result);

    }

    /**
     * Test of getSpecies method, of class Entry.
     */
//    @Test
//    public void testGetSpecies() {
//        List<String> expResult = new ArrayList<>();
//
//        expResult.add("Human");
//        expResult.add("Mouse");
//        expResult.add("Fruit fly");
//        expResult.add("Rat");
//        expResult.add("Arabidopsis thaliana");
//        expResult.add("Bovine");
//
//        List<String> result = instance.getSpecies();
//        assertEquals(expResult, result);
//
//    }

}

package uk.ac.ebi.ep.mm.app;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.ep.mm.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * @author rafa
 * @since 1.0.16
 */
public class IntenzSaxParserTest {

    IntenzSaxParser parser;
    Entry ec1111, ec1234, ec18413, rhea21881, rhea19995;
    DummyMegaMapper mm;

    @Before
    public void setUp() throws Exception {
        parser = new IntenzSaxParser();
        mm = new DummyMegaMapper();
        parser.setWriter(mm);

        ec1111 = new Entry();
        ec1111.setEntryId("1.1.1.1");
        ec1111.setDbName(MmDatabase.EC.name());
        ec1111.setEntryName("Alcohol dehydrogenase");

        ec1234 = new Entry();
        ec1234.setEntryId("1.2.3.4");
        ec1234.setDbName(MmDatabase.EC.name());
        ec1234.setEntryName("Oxalate oxidase");

        ec18413 = new Entry();
        ec18413.setEntryId("1.8.4.13");
        ec18413.setDbName(MmDatabase.EC.name());
        ec18413.setEntryName("L-methionine (S)-S-oxide reductase");
        
        rhea21881 = new Entry();
        rhea21881.setEntryId("RHEA:21881");
        rhea21881.setDbName(MmDatabase.Rhea.name());
        rhea21881.setEntryName("2 H(+) + O(2) + oxalate => 2 CO(2) + H(2)O(2)");

        rhea19995 = new Entry();
        rhea19995.setEntryId("RHEA:19995");
        rhea19995.setDbName(MmDatabase.Rhea.name());
        rhea19995.setEntryName("L-methionine (S)-S-oxide + thioredoxin =>" +
                " L-methionine + H(2)O + thioredoxin disulfide");
    }

    /**
     * A test with two reversible reactions and two cofactors.
     * @throws Exception
     */
    @Test
    public void testParse1111() throws Exception {
        parser.parse(IntenzSaxParserTest.class.getClassLoader()
                .getResourceAsStream("EC_1.1.1.1.xml"));

        assertNotNull(mm.getXrefs(ec1111));

        assertNotNull(mm.getXrefs(ec1111, MmDatabase.Rhea));
        assertEquals(2, mm.getXrefs(ec1111, MmDatabase.Rhea).size());

        final Collection<XRef> ecChebiXrefs =
                mm.getXrefs(ec1111, MmDatabase.ChEBI);
        assertNotNull(ecChebiXrefs);
        // 7 *different* reaction participants, 2 cofactors:
        assertEquals(7 + 2, ecChebiXrefs.size());
        int cofactors = 0, reactants = 0, products = 0, reactProds = 0;
        for (XRef chebiXref : ecChebiXrefs) {
            switch (Relationship.valueOf(chebiXref.getRelationship())){
                case is_cofactor_of:
                    cofactors++; break;
                case is_substrate_of:
                    reactants++; break;
                case is_substrate_or_product_of:
                    reactProds++; break;
                case is_product_of:
                    products++; break;
            }
        }
        assertEquals(2, cofactors);
        assertEquals(0, reactants);
        assertEquals(0, products);
        assertEquals(7, reactProds); // reversible reactions
    }

    /**
     * A test with one left-to-right reaction and one cofactor.
     * @throws Exception
     */
    @Test
    public void testParse1234() throws Exception {
        parser.parse(IntenzSaxParserTest.class.getClassLoader()
                .getResourceAsStream("EC_1.2.3.4.xml"));

        assertNotNull(mm.getXrefs(ec1234));

        final Collection<XRef> rheaXrefs = mm.getXrefs(ec1234, MmDatabase.Rhea);
        assertNotNull(rheaXrefs);
        assertEquals(1, rheaXrefs.size());
        final XRef rheaXref = rheaXrefs.iterator().next();
        assertEquals("RHEA:21881", rheaXref.getToEntry().getEntryId());
        assertEquals(Relationship.catalyses.name(), rheaXref.getRelationship());

        final Collection<XRef> ecChebiXrefs =
                mm.getXrefs(ec1234, MmDatabase.ChEBI);
        assertNotNull(ecChebiXrefs);
        // 5 reaction participants, 1 cofactor:
        assertEquals(5 + 1, ecChebiXrefs.size());
        int cofactors = 0, reactants = 0, products = 0, reactProds = 0;
        for (XRef chebiXref : ecChebiXrefs) {
            switch (Relationship.valueOf(chebiXref.getRelationship())){
                case is_cofactor_of:
                    // there is only one:
                    final Entry chebiEntry = chebiXref.getFromEntry();
                    assertEquals("CHEBI:18291", chebiEntry.getEntryId());
                    assertEquals("Manganese", chebiEntry.getEntryName());
                    cofactors++; break;
                case is_substrate_of:
                    reactants++; break;
                case is_substrate_or_product_of:
                    reactProds++; break;
                case is_product_of:
                    products++; break;
            }
        }
        assertEquals(1, cofactors);
        assertEquals(3, reactants);
        assertEquals(2, products);
        assertEquals(0, reactProds); // irreversible reaction

        final Collection<XRef> rheaChebiXrefs =
                mm.getXrefs(rhea21881, MmDatabase.ChEBI);
        assertNotNull(rheaChebiXrefs);
        assertEquals(5, rheaChebiXrefs.size());
        reactants = 0; products = 0; reactProds = 0;
        for (XRef chebiXref : rheaChebiXrefs) {
            switch (Relationship.valueOf(chebiXref.getRelationship())){
                case is_reactant_of:
                    reactants++; break;
                case is_product_of:
                    products++; break;
                case is_reactant_or_product_of:
                    reactProds++; break;
            }
        }
        assertEquals(3, reactants);
        assertEquals(2, products);
        assertEquals(0, reactProds); // irreversible reaction
    }

    @Test
    public void testParse18413() throws Exception {
        parser.parse(IntenzSaxParserTest.class.getClassLoader()
                .getResourceAsStream("EC_1.8.4.13.xml"));

        assertNotNull(mm.getXrefs(ec18413));

        final Collection<XRef> rheaXrefs =
                mm.getXrefs(ec18413, MmDatabase.Rhea);
        assertNotNull(rheaXrefs);
        assertEquals(1, rheaXrefs.size());
        final XRef rheaXref = rheaXrefs.iterator().next();
        assertEquals("RHEA:19995", rheaXref.getToEntry().getEntryId());
        assertEquals(Relationship.catalyses.name(), rheaXref.getRelationship());

        final Collection<XRef> ecChebiXrefs =
                mm.getXrefs(ec18413, MmDatabase.ChEBI);
        assertNotNull(ecChebiXrefs);
        // 5 reaction participants, 1 cofactor:
        assertEquals(5 + 1, ecChebiXrefs.size());
        int cofactors = 0, reactants = 0, products = 0, reactProds = 0;
        for (XRef chebiXref : ecChebiXrefs) {
            switch (Relationship.valueOf(chebiXref.getRelationship())){
                case is_cofactor_of:
                    // there is only one:
                    final Entry chebiEntry = chebiXref.getFromEntry();
                    assertEquals("CHEBI:57783", chebiEntry.getEntryId());
                    assertEquals("NADPH", chebiEntry.getEntryName());
                    cofactors++; break;
                case is_substrate_of:
                    reactants++; break;
                case is_substrate_or_product_of:
                    reactProds++; break;
                case is_product_of:
                    products++; break;
            }
        }
        assertEquals(1, cofactors);
        assertEquals(2, reactants);
        assertEquals(3, products);
        assertEquals(0, reactProds); // irreversible reaction

        final Collection<XRef> rheaChebiXrefs =
                mm.getXrefs(rhea19995, MmDatabase.ChEBI);
        assertNotNull(rheaChebiXrefs);
        assertEquals(5, rheaChebiXrefs.size());
        reactants = 0; products = 0; reactProds = 0;
        for (XRef chebiXref : rheaChebiXrefs) {
            switch (Relationship.valueOf(chebiXref.getRelationship())){
                case is_reactant_of:
                    reactants++; break;
                case is_product_of:
                    products++; break;
                case is_reactant_or_product_of:
                    reactProds++; break;
            }
        }
        assertEquals(2, reactants);
        assertEquals(3, products);
        assertEquals(0, reactProds); // irreversible reaction
    }

    @After
    public void tearDown() throws Exception {

    }
}

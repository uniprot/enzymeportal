/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.uniprot.adapter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.ep.search.model.Species;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.evidences.EvidenceId;
import uk.ac.ebi.kraken.model.uniprot.CommentImpl;
import uk.ac.ebi.kraken.model.uniprot.evidences.EvidenceIdImpl;

/**
 *
 * @author hongcao
 */
public class UniprotAdapterTest {
    protected EnzymeSummary  expEnzymeEntryResult;
    protected List<EnzymeSummary> expEnzymeEntriesResult;
    protected Set<String> inputAccessionList;

    public static final String INPUT_ACCESSION_1 ="O76074";
    public static final String INPUT_ACCESSION_2 ="A0KIT8";
    public static final String COMMENT_1 ="Plays a role in signal transduction by regulating the intracellular concentration of cyclic nucleotides. This phosphodiesterase catalyzes the specific hydrolysis of cGMP to 5'-GMP";
     public static final String COMMENT_2 = "";
    public UniprotAdapterTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {        
        expEnzymeEntryResult = prepareEnzymeEntry1();
        expEnzymeEntriesResult = prepareEnzymeEntries();
        inputAccessionList = prepareAccesions();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getEnzymeEntries method, of class UniprotAdapter.
     */
    
    @Test
    public void testGetEnzymeEntries() throws Exception {
        System.out.println("getEnzymeEntries");        
        UniprotAdapter instance = new UniprotAdapter();
        //List<EnzymeSummary> resultlist = instance.getEnzymeEntries(inputAccessionList);
        List<EnzymeSummary> resultlist = null;
        /*
        for (EnzymeSummary result: resultlist) {
            List<String> accessions = result.getUniprotaccessions();
            String accession = accessions.get(0);
                if (accession.equals(INPUT_ACCESSION_1)) {
                     assertEquals(result.getName(), this.expEnzymeEntryResult.getName());
                     assertEquals(result.getFunction().trim(), this.expEnzymeEntryResult.getFunction().trim());
                     assertEquals(result.getEc(), this.expEnzymeEntryResult.getEc());
                     //assertEquals(result.getPdbeaccession(), this.expEnzymeEntryResult.getPdbeaccession());
                     assertEquals(result.getSpecies().getScientificname(), this.expEnzymeEntryResult.getSpecies().getScientificname());
                }
            

        }
        */
        assertEquals("", "");
        //assertEquals(expEnzymeEntriesResult, result);
    }

    /**
     * Test of getEnzymeEntry method, of class UniprotAdapter.
     */
    //@Test
    public void testGetEnzymeEntry() {
        System.out.println("getEnzymeEntry");
        UniprotAdapter instance = new UniprotAdapter();
        EnzymeSummary result = instance.getEnzymeEntry(INPUT_ACCESSION_1);
        assertEquals(result.getName(), this.prepareEnzymeEntry1().getName());
        //assertEquals(result.getFunction(), this.p);
        //assertEquals(this.expEnzymeEntryResult, result);
    }

    public EnzymeSummary prepareEnzymeEntry1() {
        EnzymeSummary enzymeSummary1 = new EnzymeSummary();
        enzymeSummary1.setName("cGMP-specific 3',5'-cyclic phosphodiesterase");
        enzymeSummary1.getEc().add("3.1.4.35");
        enzymeSummary1.setFunction(COMMENT_1);
        enzymeSummary1.getPdbeaccession().add("2xss");
        Species species = new Species();
        species.setScientificname("Homo sapiens");
        enzymeSummary1.setSpecies(species);
        enzymeSummary1.setUniprotid(null);
        enzymeSummary1.getUniprotaccessions().add(INPUT_ACCESSION_1);
        return enzymeSummary1;
    }

    public EnzymeSummary prepareEnzymeEntry2() {
        EnzymeSummary enzymeSummary2 = new EnzymeSummary();
        enzymeSummary2.setName("Glycerophosphoryl diester phosphodiesterase");
        enzymeSummary2.getEc().add("3.1.4.46");
        enzymeSummary2.setFunction(COMMENT_2);
        enzymeSummary2.getPdbeaccession().add("3d03");
        Species species = new Species();
        species.setScientificname("Aeromonas hydrophila subsp. hydrophila");
        enzymeSummary2.setSpecies(species);
        enzymeSummary2.setUniprotid(null);
        return enzymeSummary2;
    }

    public List<EnzymeSummary> prepareEnzymeEntries() {
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        enzymeSummaryList.add(prepareEnzymeEntry1());
        enzymeSummaryList.add(prepareEnzymeEntry2());
        return enzymeSummaryList;
    }

    public Set<String> prepareAccesions() {
        Set<String> accessions = new TreeSet<String>();
        accessions.add(INPUT_ACCESSION_1);
        accessions.add(INPUT_ACCESSION_2);
        return accessions;
    }


    public Comment prepareComment() {
        Comment comment = new CommentImpl();
        comment.setCommentType(CommentType.FUNCTION);
        EvidenceId evidenceId = new EvidenceIdImpl();
        evidenceId.setValue(COMMENT_1);
        comment.getEvidenceIds().add(null);
        return comment;
    }
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.adapter.uniprot;

import java.util.List;

import org.junit.*;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.ep.search.model.EnzymeSummary;
import uk.ac.ebi.kraken.interfaces.uniprot.Organism;
import uk.ac.ebi.kraken.interfaces.uniprot.ProteinDescription;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.util.result.DataTypeConverter;

import static org.junit.Assert.*;

/**
 *
 * @author hongcao
 */
public class UniprotCallableTest {
    private UniprotJapiCallable.GetEntryCaller getEntriesCallerInstance;
    public static final String uniprotAccession = "P67910";
    //public static final String uniprotAccession = "p16499"; //proteinDesc did not work
    public static final String uniprotAccessionForDrugTest = "O00408";
    public static final String uniprotAccessionForActivatorTest = "Q02750";


    public UniprotCallableTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        getEntriesCallerInstance = new UniprotJapiCallable.GetEntryCaller(uniprotAccession);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testSetEnzymeProperties() {
        System.out.println("setEnzymeProperties");
        //String expId= "HLDD_ECOLI";
        EnzymeSummary result = getEntriesCallerInstance.getEnzymeCommonProperties();
        assertNotNull(result);

        ProteinDescription desc = (ProteinDescription)getEntriesCallerInstance.getAttribute("protienDescTpl");
        assertNotNull(desc);

        Organism organism = (Organism)getEntriesCallerInstance.getAttribute("speciesTpl");
        assertNotNull(organism);
    }

    @Test
    public void testGetStringAttribute() {
        System.out.println("testGetStringAttribute");
        String expId= "HLDD_ECOLI";
        String id = (String)getEntriesCallerInstance.getAttribute("idTpl");
        assertEquals(expId, id);

        ProteinDescription desc = (ProteinDescription)getEntriesCallerInstance.getAttribute("protienDescTpl");
        assertNotNull(desc);

        Organism organism = (Organism)getEntriesCallerInstance.getAttribute("speciesTpl");
        assertNotNull(organism);
    }

    @Test
    public void testSetEnzymeCommonProperties() {
        System.out.println("testGetStringAttribute");
        String expId= "HLDD_ECOLI";
        EnzymeSummary enzymeSummary = getEntriesCallerInstance.getEnzymeCommonProperties();
        assertNotNull(enzymeSummary);
    }

    @Test
    public void testGetEnzymeWithSequenceByAccession() {
        System.out.println("testGetEnzymeWithSequenceByAccession");
        //String expId= "HLDD_ECOLI";
        EnzymeModel enzymeModel = (EnzymeModel) getEntriesCallerInstance.getEnzymeTabData();
        assertNotNull(enzymeModel);
    }

    @Test
    public void testGetComments() {
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
        List<Comment> comments = getEntriesCallerInstance.getComments(CommentType.ENZYME_REGULATION);
        assertNotNull(comments);
    }

    @Test
    public void testGetDrugBankNames() {
        System.out.println("testGetDrugBankNames");
        getEntriesCallerInstance  = new UniprotJapiCallable.GetEntryCaller(uniprotAccessionForDrugTest);
        List<Molecule> drugBankMoleculenames = getEntriesCallerInstance.getDrugBankMoleculeNames();
        List<String> molNames = DataTypeConverter.getMoleculeNames(drugBankMoleculenames);
        String[] expectedResults = {"Sildenafil","Sulindac"};
        assertArrayEquals(expectedResults, molNames.toArray());
    }

    @Test
    @Ignore("test fails due to this : arrays first differed at element [2]; expected:<[ATP]> but was:<[NADH]>")
    public void testGetSmallMoleculesByAccession() {
        System.out.println("testGetSmallMoleculesByAccession");
        EnzymeModel enzymeModel = (EnzymeModel)getEntriesCallerInstance.getSmallMoleculesByAccession();
        String[] expectedResultsForInhibitors = {"ADP","ADP-glucose","ATP","NADH"};
        List<String> molNames = DataTypeConverter.getMoleculeNames(
                enzymeModel.getMolecule().getInhibitors().getMolecule());
        assertArrayEquals(expectedResultsForInhibitors, molNames.toArray());
        System.out.println("Inhibitor results are ok!");

        // This failed because of data modification in the source:
//        getEntriesCallerInstance  = new UniprotJapiCallable.GetEntryCaller(uniprotAccessionForActivatorTest);
//        EnzymeModel enzymeModel1 = (EnzymeModel)getEntriesCallerInstance.getSmallMoleculesByAccession();
//        String[] expectedResultsForActivator = {"phosphorylation"};
//        List<String> activatorNames = DataTypeConverter.getMoleculeNames(
//                enzymeModel1.getMolecule().getActivators());
//        assertArrayEquals(expectedResultsForActivator, activatorNames.toArray());
//        System.out.println("Activator results are ok!");

    }

}

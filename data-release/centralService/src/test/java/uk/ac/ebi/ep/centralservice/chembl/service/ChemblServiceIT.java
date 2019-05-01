package uk.ac.ebi.ep.centralservice.chembl.service;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.ep.centralservice.chembl.config.ChemblServiceConfig;
import uk.ac.ebi.ep.model.TempCompoundCompare;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ChemblServiceConfig.class})
public class ChemblServiceIT {

    @Autowired
    private ChemblService chemblService;

    /**
     * Test of capitalizeFirstLetter method, of class ChemblService.
     */
    @Test
    public void testCapitalizeFirstLetter() {
        System.out.println("capitalizeFirstLetter");
        String original = "sildenafil";

        String expResult = "Sildenafil";
        String result = chemblService.capitalizeFirstLetter(original);
        assertEquals(expResult, result);

    }

    /**
     * Test of getMoleculesByCuratedMechanism method, of class ChemblService.
     */
    @Test
    public void testGetMoleculesByCuratedMechanism() {
        System.out.println("getMoleculesByCuratedMechanism");
        List<String> targetList = Arrays.asList("CHEMBL1827","CHEMBL748212","CHEMBL2111470","CHEMBL829810");
        String protein = "P1234";

        chemblService.getMoleculesByCuratedMechanism(targetList, protein);

        List<TempCompoundCompare> result = chemblService.getFdaChemblCompounds();
    
        result.forEach(data ->System.out.println("FDA "+ data));
        Assert.assertNotNull(result);

    }

}

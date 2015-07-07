/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.blast;

import java.util.List;
import org.junit.Test;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.model.blast.JobStatus;
import uk.ac.ebi.uniprot.dataservice.client.ebiservice.BlastHit;
import uk.ac.ebi.uniprot.dataservice.client.exception.BlastServiceException;
import uk.ac.ebi.uniprot.dataservice.client.exception.EBIServiceException;

/**
 *
 * @author joseph
 */
public class UniprotBlastServiceTest extends AbstractUniprotServiceTest {



    /**
     * Test of blastService method, of class UniprotBlastService.
     *
     * @throws java.lang.Exception
     */
    @Test(expected = BlastServiceException.class)
    public void testBlastService() throws Exception {
        LOGGER.info("blastService");
        //String jobId = "ncbiblast-R20150707-103859-0454-63524315-oy";
        List<BlastHit<UniProtEntry>> result = service.blastService(jobId);
        assertNotNull(result);
   

    }

    /**
     * Test of jobStatus method, of class UniprotBlastService.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testJobStatus() throws Exception {
        LOGGER.info("jobStatus");
        //String jobId = "ncbiblast-R20150707-103859-0454-63524315-oy";

        JobStatus expResult = JobStatus.RUNNING;
        JobStatus result = service.jobStatus(jobId);
    
        assertEquals(expResult, result);

    }

    /**
     * Test of jApiBlast method, of class UniprotBlastService.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testJApiBlast() throws Exception {
        LOGGER.info("jApiBlast");

        String sequence = ">sp|Q9M5K2|DLDH2_ARATH Dihydrolipoyl dehydrogenase 2, mitochondrial OS=Arabidopsis thaliana GN=LPD2 PE=1 SV=1\n"
                + "MAMASLARRKAYFLTRNISNSPTDAFRFSFSLTRGFASSGSDDNDVVIIGGGPGGYVAAI\n"
                + "KAAQLGLKTTCIEKRGALGGTCLNVGCIPSKALLHSSHMYHEAKHVFANHGVKVSSVEVD\n"
                + "LPAMLAQKDTAVKNLTRGVEGLFKKNKVNYVKGYGKFLSPSEVSVDTIDGENVVVKGKHI\n"
                + "IVATGSDVKSLPGITIDEKKIVSSTGALSLTEIPKKLIVIGAGYIGLEMGSVWGRLGSEV\n"
                + "TVVEFAADIVPAMDGEIRKQFQRSLEKQKMKFMLKTKVVGVDSSGDGVKLIVEPAEGGEQ\n"
                + "TTLEADVVLVSAGRTPFTSGLDLEKIGVETDKGGRILVNERFSTNVSGVYAIGDVIPGPM\n"
                + "LAHKAEEDGVACVEFIAGKHGHVDYDKVPGVVYTYPEVASVGKTEEQLKKEGVSYNVGKF\n"
                + "PFMANSRAKAIDTAEGMVKILADKETDKILGVHIMSPNAGELIHEAVLAINYDASSEDIA\n"
                + "RVCHAHPTMSEAIKEAAMATYDKPIHM";

        String result = service.jApiBlast(sequence);
        assertNotNull(result);
      

    }

    @Test
    public void testJApiBlastThrowException() throws EBIServiceException {
        LOGGER.info("jApiBlast");
        String sequence = "bla bla bla ..,j)(7454563%%^^44@%";

        String expResult = "<?xml version='1.0' encoding='UTF-8'?> <error>\n"
                + " <description>Invalid parameters: \n"
                + "Sequence -> Error in reading input sequence. Please check your input.</description>\n"
                + "</error>";
        String result = service.jApiBlast(sequence);

        assertTrue(result.contains("UTF-8"));

    }

}

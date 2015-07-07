/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.blast;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.ac.ebi.uniprot.dataservice.client.exception.EBIServiceException;

/**
 *
 * @author joseph
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {BlastServiceConfig.class})
public abstract class AbstractUniprotServiceTest extends TestCase {

    protected static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AbstractUniprotServiceTest.class);
    //@Autowired
    // protected UniProtBlastService uniprotBlastService;
    protected UniprotBlastService service = new UniprotBlastService();
    protected String jobId;

    @Before
    @Override
    public void setUp() throws EBIServiceException {

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

        jobId = service.jApiBlast(sequence);
    }

}

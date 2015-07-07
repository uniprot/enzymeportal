/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.blast;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.model.blast.JobStatus;
import uk.ac.ebi.kraken.model.blast.parameters.DatabaseOptions;
import uk.ac.ebi.kraken.model.blast.parameters.ExpectedThreshold;
import uk.ac.ebi.kraken.model.blast.parameters.GapAlign;
import uk.ac.ebi.kraken.model.blast.parameters.MaxNumberResultsOptions;
import uk.ac.ebi.kraken.model.blast.parameters.SimilarityMatrixOptions;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.ebiservice.BlastHit;
import uk.ac.ebi.uniprot.dataservice.client.ebiservice.BlastServiceParameter;
import uk.ac.ebi.uniprot.dataservice.client.ebiservice.UniProtBlastService;
import uk.ac.ebi.uniprot.dataservice.client.exception.EBIServiceException;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;

/**
 *
 * @author joseph
 */
@Service
public class UniprotBlastService {

    // private static final Logger logger = LoggerFactory.getLogger(UniprotBlastService.class);
    @Bean
    public UniProtBlastService uniprotBlastService() {
        ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
        UniProtBlastService uniprotBlastService = serviceFactoryInstance.getUniProtBlastService();
        return uniprotBlastService;
    }

    /**
     *
     * @param jobId job identifier
     * @return Blast results mapped with UniProtEntry objects
     * @throws uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException
     */
    public List<BlastHit<UniProtEntry>> blastService(String jobId) throws ServiceException {

        return jApiBlastResult(jobId);
    }

    private List<BlastHit<UniProtEntry>> jApiBlastResult(String jobId) throws ServiceException {
        List<BlastHit<UniProtEntry>> results = uniprotBlastService().getUniProtResult(jobId);

        return results;

    }

    /**
     *
     * @param jobId
     * @return job status
     * @throws EBIServiceException
     */
    public JobStatus jobStatus(String jobId) throws EBIServiceException {

        if (jobId.contains("UTF-8")) {

            return JobStatus.NOT_FOUND;
        }

        return uniprotBlastService().checkStatus(jobId);
    }

    /**
     *
     * @param sequence sequence in FASTA format
     * @return job Id
     * @throws EBIServiceException
     */
    public String jApiBlast(String sequence) throws EBIServiceException {

        // Blast input parameters
        BlastServiceParameter input = new BlastServiceParameter(DatabaseOptions.SWISSPROT, sequence, MaxNumberResultsOptions.TWO_HUNDRED_FIFTY);
        input.setExp(ExpectedThreshold.TEN);
        input.setGapalign(GapAlign.FALSE);
        //input.setFilter(FilterOptions.NONE);
        input.setMatrix(SimilarityMatrixOptions.BLOSUM_62);

        // Initialise and run a UniProtBlastService
        ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
        UniProtBlastService uniprotBlastService = serviceFactoryInstance.getUniProtBlastService();

        return uniprotBlastService.submit(input);

    }

 
}

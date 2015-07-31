/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.uniprotservice.blast;

import java.util.LinkedList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.model.blast.JobStatus;
import uk.ac.ebi.kraken.model.blast.parameters.BlastVersionOption;
import uk.ac.ebi.kraken.model.blast.parameters.DatabaseOptions;
import uk.ac.ebi.kraken.model.blast.parameters.ExpectedThreshold;
import uk.ac.ebi.kraken.model.blast.parameters.MaxNumberResultsOptions;
import uk.ac.ebi.kraken.model.blast.parameters.SimilarityMatrixOptions;
import uk.ac.ebi.kraken.model.blast.parameters.SortOptions;
import uk.ac.ebi.kraken.model.blast.parameters.StatsOptions;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtQueryService;
import uk.ac.ebi.kraken.uuw.services.remoting.blast.BlastData;
import uk.ac.ebi.kraken.uuw.services.remoting.blast.BlastHit;
import uk.ac.ebi.kraken.uuw.services.remoting.blast.BlastInput;

/**
 *
 * @author joseph
 */
@Service
public class UniprotProdBlastService {

    public List<BlastHit<UniProtEntry>> blastService(String jobId) {

        return prodBlastResult(jobId);
    }

    public String jApiBlast(String sequence) {

        //Default options
//    uk.ac.ebi.kraken.model.blast.parameters.BlastVersionOption.BLASTP
//    uk.ac.ebi.kraken.model.blast.parameters.SimilarityMatrixOptions.BLOSUM_62
//    uk.ac.ebi.kraken.model.blast.parameters.ExpectedThreshold.TEN
//    uk.ac.ebi.kraken.model.blast.parameters.ViewFilterOptions.NO
//    uk.ac.ebi.kraken.model.blast.parameters.FilterOptions.NONE
//    uk.ac.ebi.kraken.model.blast.parameters.MaxNumberResultsOptions.ONE_HUNDRED_FIFTY
//    uk.ac.ebi.kraken.model.blast.parameters.ScoreOptions.ONE_HUNDRED
//    uk.ac.ebi.kraken.model.blast.parameters.SensitivityValue.NORMAL
//    uk.ac.ebi.kraken.model.blast.parameters.SortOptions.PVALUE
//    uk.ac.ebi.kraken.model.blast.parameters.StatsOptions.KAP
//    uk.ac.ebi.kraken.model.blast.parameters.FormatOptions.DEFAULT
//    uk.ac.ebi.kraken.model.blast.parameters.TopcomboN.ONE
//    
        BlastInput input = new BlastInput(DatabaseOptions.UNIPROTKB, sequence, BlastVersionOption.BLASTP, SimilarityMatrixOptions.BLOSUM_62, MaxNumberResultsOptions.FIVE_HUNDRED, ExpectedThreshold.TEN, StatsOptions.POISSON, SortOptions.HIGHSCORE);

        //Get the UniProt Service. This is how to access the blast service
        //UniProtQueryService service = UniProtJAPI.factory.getUniProtQueryService();
        //Submitting the input to the service will return a job id
        return queryService().submitBlast(input);

    }

    private List<BlastHit<UniProtEntry>> prodBlastResult(String jobId) {

        List<BlastHit<UniProtEntry>> result = new LinkedList<>();
        BlastData<UniProtEntry> blastResult = queryService().getResults(jobId);
        if (blastResult != null) {
            result = blastResult.getBlastHits();
            return result;
        }
        return result;
    }

    public JobStatus jobStatus(String jobId) {

        if (jobId.contains("UTF-8")) {

            return JobStatus.FAILURE;
        }

        return queryService().checkStatus(jobId);
    }

    @Bean
    public UniProtQueryService queryService() {
        UniProtQueryService service = UniProtJAPI.factory.getUniProtQueryService();
        return service;
    }
}

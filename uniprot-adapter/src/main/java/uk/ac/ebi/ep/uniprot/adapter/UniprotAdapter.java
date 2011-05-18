package uk.ac.ebi.ep.uniprot.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummary;
import uk.ac.ebi.ep.uniprot.adapter.UniprotCallable.GetEntriesCaller;
import uk.ac.ebi.kraken.interfaces.uniprot.ProteinDescription;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class UniprotAdapter implements IUniprotAdapter{


//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public List<EnzymeSummary> getEnzymeEntries(
                    List<String> accessionList)
                    throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService pool = Executors.newCachedThreadPool();
        Iterator it = accessionList.iterator();
        List<EnzymeSummary> enzymeSummaryList = new ArrayList<EnzymeSummary>();
        while (it.hasNext()) {
            String accession = (String)it.next();
            Callable caller = new GetEntriesCaller(accession);
            Future<EnzymeSummary> future = pool.submit(caller);
            EnzymeSummary enzymeSummary = future.get(
                    IUniprotAdapter.ENTRY_TIMEOUT, TimeUnit.SECONDS );
            enzymeSummaryList.add(enzymeSummary);
        }
        return enzymeSummaryList;

    }

    public EnzymeSummary getEnzymeEntry(String uniprotAccession) {
        throw new UnsupportedOperationException("Not supported yet.");
    }




}

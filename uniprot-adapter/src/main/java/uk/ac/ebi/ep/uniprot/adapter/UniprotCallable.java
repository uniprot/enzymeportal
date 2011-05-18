package uk.ac.ebi.ep.uniprot.adapter;

import java.util.List;
import java.util.concurrent.Callable;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummary;
import uk.ac.ebi.kraken.interfaces.uniprot.DatabaseType;
import uk.ac.ebi.kraken.interfaces.uniprot.ProteinDescription;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
import uk.ac.ebi.kraken.uuw.services.remoting.EntryRetrievalService;
import uk.ac.ebi.kraken.uuw.services.remoting.UniProtJAPI;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class UniprotCallable {

//********************************* VARIABLES ********************************//
   protected  static EntryRetrievalService entryRetrievalService
            = UniProtJAPI.factory.getEntryRetrievalService();

//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

//******************************** INNER CLASS *******************************//

    public static class GetEntriesCaller implements Callable<EnzymeSummary> {
        protected String accession;

        public GetEntriesCaller(String accession) {
            this.accession = accession;
        }


        public EnzymeSummary call() throws Exception {
            return getEnzymeEntry();
        }

        public EnzymeSummary getEnzymeEntry()  {
            //Retrieve UniProt entry by its accession number
            UniProtEntry entry = (UniProtEntry) entryRetrievalService
                    .getUniProtEntry(accession);
            return setEnzymeEntries(entry);
        }

    public EnzymeSummary setEnzymeEntries(UniProtEntry entry) {
        EnzymeSummary enzymeSummary = new EnzymeSummary();
        if (entry != null) {            
            List<Comment> commentList = entry.getComments(CommentType.FUNCTION);
            String function = Transformer.transformToFunction(commentList);
            enzymeSummary.setFunction(function);

            ProteinDescription desc = entry.getProteinDescription();            
            String name = null;
            if (desc.hasSubNames()){
                 List<Name> names  = desc.getSubNames();
                name = Transformer.transformNames(names).get(0);
            }
            else {
                Name recName = desc.getRecommendedName();
                name = Transformer.transformToName(recName.getFields());
            }

            enzymeSummary.setName(name);

            if (desc.hasAlternativeNames()){
                List<Name> names = desc.getAlternativeNames();
                enzymeSummary.getSynonym().addAll(Transformer.transformNames(names));
            }
        }
        return enzymeSummary;
    }

  }
}

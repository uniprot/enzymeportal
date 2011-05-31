package uk.ac.ebi.ep.uniprot.adapter;

import java.util.List;
import java.util.concurrent.Callable;
import uk.ac.ebi.ep.search.result.jaxb.EnzymeSummary;
import uk.ac.ebi.ep.search.result.jaxb.Species;
import uk.ac.ebi.kraken.interfaces.uniprot.ProteinDescription;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.CommentType;
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
            return setEnzymeProperties(entry);
        }

    public EnzymeSummary setEnzymeProperties(UniProtEntry entry) {
        EnzymeSummary enzymeSummary = new EnzymeSummary();
        enzymeSummary.getUniprotaccessions().add(accession);
        if (entry != null) {            
                List<Comment> commentList = entry.getComments(CommentType.FUNCTION);
            String function = Transformer.getFunction(commentList);
            enzymeSummary.setFunction(function);

            ProteinDescription desc = entry.getProteinDescription();            
            String name = null;
            if (desc.hasSubNames()){
                 List<Name> names  = desc.getSubNames();
                name = Transformer.getFullName(names.get(0));
            }
            else {
                Name recName = desc.getRecommendedName();
                name = Transformer.getFullName(recName);
            }

            enzymeSummary.setName(name);

            if (desc.hasAlternativeNames()){
                List<Name> names = desc.getAlternativeNames();
                enzymeSummary.getSynonym().addAll(Transformer.getAltNames(names));
            }
            enzymeSummary.getEc().addAll(desc.getEcNumbers());
            String speciesCommonName = entry.getOrganism().getCommonName().getValue();
            String scientificName = entry.getOrganism().getScientificName().getValue();
            Species species = new Species();
            species.setCommonname(speciesCommonName);
            species.setScientificname(scientificName);
            enzymeSummary.setSpecies(species);
        }
        return enzymeSummary;
    }

  }
}

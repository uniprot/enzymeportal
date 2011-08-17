package uk.ac.ebi.ep.core.search;

import java.util.ArrayList;
import java.util.List;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.reactome.ReactomeAdapter;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.rhea.ws.client.IRheaAdapter;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.client.RheasResourceClient;
import uk.ac.ebi.rhea.ws.response.cmlreact.Link;
import uk.ac.ebi.rhea.ws.response.cmlreact.Map;
import uk.ac.ebi.rhea.ws.response.cmlreact.Reaction;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class EnzymeRetriever extends EnzymeFinder implements IEnzymeRetriever {

//********************************* VARIABLES ********************************//
    protected String accesion;

    protected IRheaAdapter rheaAdapter;

    protected IReactomeAdapter reactomeAdapter;

    public String getAccesion() {
        return accesion;
    }

    public void setAccesion(String accesion) {
        this.accesion = accesion;
    }


//******************************** CONSTRUCTORS ******************************//

    public EnzymeRetriever() {
        rheaAdapter = new RheasResourceClient();
        reactomeAdapter = new ReactomeAdapter();
    }


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public EnzymeModel getEnzyme(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = (EnzymeModel)this.uniprotAdapter.getEnzymeSummary(uniprotAccession);
        try {
            this.intenzAdapter.getEnzymeDetails(enzymeModel);
        } catch (MultiThreadingException ex) {
            throw new EnzymeRetrieverException ("Unable to retrieve the entry details! ", ex);
        }
        return enzymeModel;
    }

    public EnzymeModel getReactions(String ec) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getPathways(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getReactionsPathways(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        try {                        
            List<String> ecList = enzymeModel.getEc();
            String query = LuceneQueryBuilder.createQueryIN(IRheaAdapter.RheaQueryFields.EC.name(), false, ecList);
            List<Reaction> reactions =  this.rheaAdapter.getRheasInCmlreact(query);
            List<ReactionPathway> reactionPathways = new ArrayList<ReactionPathway>();
            for (uk.ac.ebi.rhea.ws.response.cmlreact.Reaction reaction:reactions) {
                ReactionPathway reactionPathway = new ReactionPathway();
                getReactomeRef(reaction);
                EnzymeReaction epReaction = new EnzymeReaction();
                epReaction.setId(reaction.getId());
                reactionPathways.add(reactionPathway);
                //reactionPathway.getReactions().add(epReaction);
                //epReaction.setTitle();
            }
            //enzymeModel.
        } catch (RheaFetchDataException ex) {
            throw new EnzymeRetrieverException("Query data from Rhea failed! ", ex);
        }
        return enzymeModel;
    }

    public List<String> getReactomeRef(Reaction reaction) {
        List<String> accessions = new ArrayList<String>();
        List<Object> reactionElements = reaction.getReactiveCentreAndMechanismAndReactantList();
        Map xRefs = getRheaXrefs(reactionElements);
        if (xRefs != null) {
            List<Link> linkList = xRefs.getLink();
            if (linkList != null) {
                accessions.addAll(getAccessionList(linkList, "rctm"));
            }
        }
        return accessions;
    }

    public void getReactionDescription(List<String> reactomeAccessions) {

    }
    public Map getRheaXrefs(List<Object> reactionElements) {
        Map xRefs = null;
        for (Object reactionElement: reactionElements) {
            if (reactionElement instanceof Map) {
                xRefs = (Map)reactionElement;
                break;
            }
        }
        return xRefs;
    }

    public List<String> getAccessionList(List<Link> linkList, String domainPrefix) {
        List<String> accessionList = new ArrayList<String>();
        for (Link link:linkList) {
            String id = link.getId();
            String[] idFragments = id.split(":");
            String prefix = idFragments[0];
            String accession = idFragments[1];
            if (prefix.equalsIgnoreCase(domainPrefix)) {
                accessionList.add(accession);
            }
        }
        return accessionList;

    }

    public EnzymeModel getProteinStructure(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getMolecules(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getDiseases(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getLiterarture(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

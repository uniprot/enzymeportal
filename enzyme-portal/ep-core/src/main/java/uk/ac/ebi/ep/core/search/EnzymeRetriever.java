package uk.ac.ebi.ep.core.search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;

import uk.ac.ebi.das.jdas.adapters.features.DasGFFAdapter.SegmentAdapter;
import uk.ac.ebi.das.jdas.adapters.features.FeatureAdapter;
import uk.ac.ebi.das.jdas.exceptions.ValidationException;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.DASSummary;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Equation;
import uk.ac.ebi.ep.enzyme.model.Image;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ProteinStructure;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;
import uk.ac.ebi.ep.reactome.IReactomeAdapter;
import uk.ac.ebi.ep.reactome.ReactomeAdapter;
import uk.ac.ebi.ep.reactome.ReactomeServiceException;
import uk.ac.ebi.ep.search.exception.MultiThreadingException;
import uk.ac.ebi.ep.util.query.LuceneQueryBuilder;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.ws.client.IRheaAdapter;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.client.RheasResourceClient;
import uk.ac.ebi.rhea.ws.response.cmlreact.Link;
import uk.ac.ebi.rhea.ws.response.cmlreact.Map;
import uk.ac.ebi.rhea.ws.response.cmlreact.Molecule;
import uk.ac.ebi.rhea.ws.response.cmlreact.Product;
import uk.ac.ebi.rhea.ws.response.cmlreact.ProductList;
import uk.ac.ebi.rhea.ws.response.cmlreact.Reactant;
import uk.ac.ebi.rhea.ws.response.cmlreact.ReactantList;
import uk.ac.ebi.rhea.ws.response.cmlreact.Reaction;
import uk.ac.ebi.xchars.SpecialCharacters;


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
    private static Logger log = Logger.getLogger(EnzymeRetriever.class);

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
        EnzymeModel enzymeModel = (EnzymeModel) this.uniprotAdapter.getEnzymeSummary(uniprotAccession);
        try {
            this.intenzAdapter.getEnzymeDetails(enzymeModel);
        } catch (MultiThreadingException ex) {
            throw new EnzymeRetrieverException("Unable to retrieve the entry details! ", ex);
        }
        return enzymeModel;
    }

    public EnzymeModel getReactions(String ec) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getPathways(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getReactionsPathways(EnzymeModel enzymeModel) throws EnzymeRetrieverException {
        List<ReactionPathway> reactionPathways = new ArrayList<ReactionPathway>();
        List<String> ecList = enzymeModel.getEc();
        String query = LuceneQueryBuilder.createQueryIN(IRheaAdapter.RheaQueryFields.EC.name(), false, ecList);
        List<Reaction> reactions;
        try {
            reactions = this.rheaAdapter.getRheasInCmlreact(query);
        } catch (RheaFetchDataException ex) {
            throw new EnzymeRetrieverException("Query data from Rhea failed! ", ex);
        }
        for (Reaction reaction : reactions) {
            ReactionPathway reactionPathway = mapReactionToReactionPathway(reaction);
            reactionPathways.add(reactionPathway);
        }
        enzymeModel.setReactionpathway(reactionPathways);
        return enzymeModel;

    }

    public EnzymeModel getReactionsPathways(String uniprotAccession) throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        getReactionsPathways(enzymeModel);
        List<ReactionPathway> reactionPathways = enzymeModel.getReactionpathway();
        for (ReactionPathway reactionPathway: reactionPathways) {
            List<Pathway> pathways = reactionPathway.getPathways();
            EnzymeReaction enzymeReaction = reactionPathway.getReaction();
            for (Pathway pathway: pathways) {
                String reactomeUrl = (String)pathway.getUrl();
                try {
                    String[] result = this.reactomeAdapter.getReaction(reactomeUrl);
                    if (result.length > 1){
                        enzymeReaction.setDescription(result[1]);
                    }
                } catch (ReactomeServiceException ex) {
                    log.error("Failed to retrieve reaction desciption for " +reactomeUrl);
                }
            }
        }        
        return enzymeModel;
    }

    public void createReactionTitle(Reaction reaction) {
    }
    /*
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
     */

    public void getReactionDescription(List<String> reactomeAccessions) {
    }
    /*
    public List<Link> getRheaXrefs(List<Object> xRefs) {
    List<Link> linkList = new ArrayList<Link>();
    for (Object xRef: xRefs) {
    linkList.add((Link)xRefs);
    }
    return linkList;
    }
     *
     */


    public List<Object> getLinks(String dbCode,List<Link> xRefs) {
        List<Object> links = new ArrayList<Object>();
        for (Link link: xRefs) {
            String idPrefix = link.getId();
            String dbCodeInLink = idPrefix.split(":")[0];
            if (dbCodeInLink.equalsIgnoreCase(dbCode)) {
                links.add(link);
            }
        }
        return links;
    }

    public List<Object> getMechanismLinks(List<Link> xRefs) {
        List<Object> links = new ArrayList<Object>();
        String macieDbCode = Database.MACIE.getDbCode();
        return getLinks(macieDbCode, xRefs);
    }

        public List<Object> getPathwayLinks(List<Link> xRefs) {
        List<Object> links = new ArrayList<Object>();
        String reactomeDbCode = Database.REACTOME.getDbCode();
        return getLinks(reactomeDbCode, xRefs);
    }
    public ReactionPathway mapReactionToReactionPathway(Reaction reaction) {
        List<Object> reactionElements = reaction.getReactiveCentreAndMechanismAndReactantList();
        ReactionPathway reactionPathway = new ReactionPathway();
        EnzymeReaction enzymeReaction = new EnzymeReaction();
        reactionElements.get(0);
        Equation equation = new Equation();
        equation.setDirection(createEquationSymbol(reaction));
        List<Link> linkList = new ArrayList<Link>();
        for (Object reactionElement : reactionElements) {
            if (reactionElement instanceof Map) {
                Map xRefs = (Map) reactionElement;
                linkList.addAll(xRefs.getLink());
            }
            if (reactionElement instanceof ReactantList) {
                ReactantList reactantList = (ReactantList) reactionElement;
                List<Object> molObjList = reactantList.getReactantListOrReactant();
                List<Molecule> reactantMolList = getMolecules(molObjList);
                equation.getReactantlist().addAll(reactantMolList);
            }
            if (reactionElement instanceof ProductList) {
                ProductList productList = (ProductList) reactionElement;
                List<Object> molObjList = productList.getProductListOrProduct();
                List<Molecule> productMolList = getMolecules(molObjList);
                equation.getProductlist().addAll(productMolList);

            }
        }
        //Set reaction properties
        String rheaId = reaction.getId();
        enzymeReaction.setId(rheaId);
        String reactionTitle = createReactionTitle(equation);
        enzymeReaction.setTitle(reactionTitle);
        enzymeReaction.setEquation(equation);
        reactionPathway.setReaction(enzymeReaction);
        List<Object> mecList = getMechanismLinks(linkList);
        reactionPathway.setMechanism(mecList);
        List<Object> pwList = getPathwayLinks(linkList);
        setPathwayUrls(reactionPathway,pwList);
        return reactionPathway;
    }

    public void setPathwayUrls(ReactionPathway reactionPathway, List<Object> urls) {
        List<Pathway> pathways = reactionPathway.getPathways();
        if (pathways == null) {
            pathways = new ArrayList<Pathway>();
            reactionPathway.setPathways(pathways);
            pathways = reactionPathway.getPathways();
        }
        for (Object url:urls) {
            Link urlString = (Link)url;
            Pathway pathway = new Pathway();
            pathway.setUrl(urlString.getHref());
            pathways.add(pathway);
        }

    }
    public String createReactionTitle(Equation equation) {
        StringBuffer sb = new StringBuffer();
        List<Object> reacanttList = equation.getReactantlist();
        String reactantsString = createParticipantString(reacanttList);
        sb.append(reactantsString);
        sb.append(equation.getDirection());
        sb.append(createParticipantString(equation.getProductlist()));
        return sb.toString();
    }

    public String createEquationSymbol(Reaction reaction) {
        String rheaDirectionConv = reaction.getConvention();
        String direction = rheaDirectionConv.split(":")[1];
        return createEquationSymbol(direction);
    }

    public String createEquationSymbol(String direction) {
        String symbol = null;
        if (direction.equalsIgnoreCase("direction.UN")) {
            symbol = " <?> ";
        }
        if (direction.equalsIgnoreCase("direction.BI")) {
            symbol = " <=> ";
        }
        if (direction.equalsIgnoreCase("direction.LR")) {
            symbol = " => ";
        }
        if (direction.equalsIgnoreCase("direction.RL")) {
            symbol = " <= ";
        }

        return symbol;
    }

    public String createParticipantString(List<Object> molList) {
        StringBuffer sb = new StringBuffer();
        SpecialCharacters xchars = SpecialCharacters.getInstance(null);
        int counter = 1;
        for (Object molObj : molList) {
            Molecule molecule = (Molecule) molObj;
            String molName = molecule.getTitle();
            String chemMolName = xchars.xml2Display(molName);
            sb.append(chemMolName);
            if (counter < molList.size()) {
                sb.append(" + ");
            }
            counter++;
        }
        return sb.toString();
    }

    public List<Molecule> getMolecules(List<Object> molObjList) {
        List<Molecule> molList = new ArrayList<Molecule>();
        for (Object molObject : molObjList) {
            Molecule molecule = null;
            if (molObject instanceof Reactant) {
                Reactant reactant = (Reactant) molObject;
                molecule = reactant.getMolecule();
            } else {
                Product product = (Product) molObject;
                molecule = product.getMolecule();
            }
            molList.add(molecule);
        }
        return molList;
    }

    public List<String> getAccessionList(List<Link> linkList, String domainPrefix) {
        List<String> accessionList = new ArrayList<String>();
        for (Link link : linkList) {
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

    public EnzymeModel getProteinStructure(String uniprotAccession)
	throws EnzymeRetrieverException {
        EnzymeModel enzymeModel = this.getEnzyme(uniprotAccession);
        List<String> pdbIds = enzymeModel.getPdbeaccession();
    	try {
			Collection<SegmentAdapter> segments = pdbeAdapter.getSegments(pdbIds);
            for (SegmentAdapter segment : segments){
                ProteinStructure structure = new ProteinStructure();
                structure.setPdbCode(segment.getId());
                for (FeatureAdapter feature : segment.getFeature()){
                    if (feature.getType().getId().equals("description")){
                        structure.setDescription(feature.getNotes());
                    } else if (feature.getType().getId().equals("image")){
                        Image image = new Image();
                        image.setSource(feature.getLinks().get(0).getHref());
                        image.setCaption(feature.getLinks().get(0).getContent());
                        image.setHref(feature.getLinks().get(1).getHref());
                        structure.setImage(image);
                    } else if (feature.getType().getId().equals("provenance")){
                        structure.setProvenance(feature.getNotes());
                    } else if (feature.getType().getId().equals("summary")){
                        DASSummary summary = new DASSummary();
                        summary.setLabel(feature.getLabel());
                        summary.setNote(feature.getNotes());
                        structure.getSummary().add(summary);
                    }
                }
                enzymeModel.getProteinstructure().add(structure);
            }
		} catch (MalformedURLException e) {
	        throw new EnzymeRetrieverException("Wrong URL", e);
		} catch (JAXBException e) {
	        throw new EnzymeRetrieverException("Unable to get data from DAS server", e);
		} catch (ValidationException e){
	        throw new EnzymeRetrieverException("Validation error for DASGGF", e);
        }
    	return enzymeModel;
    }

    
    public EnzymeModel getMolecules(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getDiseases(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public EnzymeModel getLiterature(String uniprotAccession) throws EnzymeRetrieverException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

package uk.ac.ebi.ep.enzymeservices.rhea;

import java.util.ArrayList;
import java.util.List;
import org.xml_cml.schema.cml2.react.Link;
import org.xml_cml.schema.cml2.react.Map;
import org.xml_cml.schema.cml2.react.Molecule;
import org.xml_cml.schema.cml2.react.Name;
import org.xml_cml.schema.cml2.react.Product;
import org.xml_cml.schema.cml2.react.ProductList;
import org.xml_cml.schema.cml2.react.Reactant;
import org.xml_cml.schema.cml2.react.ReactantList;
import org.xml_cml.schema.cml2.react.Reaction;
import uk.ac.ebi.ep.data.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.data.enzyme.model.Equation;
import uk.ac.ebi.ep.data.enzyme.model.ReactionPathway;
import uk.ac.ebi.rhea.domain.Database;
import uk.ac.ebi.rhea.ws.client.RheaFetchDataException;
import uk.ac.ebi.rhea.ws.client.RheasResourceClient;
import uk.ac.ebi.rhea.ws.response.search.RheaReaction;

public class RheaWsAdapter implements IRheaAdapter {

	private RheasResourceClient wsClient;
	
	public RheaWsAdapter(){
		wsClient = new RheasResourceClient();
	}
	
	public Reaction getReactionInCmlreact(String rheaCmlReactUri)
			throws RheaFetchDataException {
        throw new UnsupportedOperationException("Not supported yet.");
	}

	public List<RheaReaction> searchRheasInCmlreact(String query)
			throws RheaFetchDataException {
		return wsClient.searchRheasInCmlreact(query);
	}

        @Override
	public List<Reaction> getRheasInCmlreact(String query)
			throws RheaFetchDataException {
		return wsClient.getRheasInCmlreact(query);
	}

        @Override
    public ReactionPathway getReactionPathway(Reaction reaction) {
       
        ReactionPathway reactionPathway = new ReactionPathway();
        
         if(reaction != null){
         List<Object> reactionElements = reaction.getReactiveCentreAndMechanismAndReactantList();
             EnzymeReaction enzymeReaction = new EnzymeReaction();
         //EnzymePortalReaction enzymeReaction = new EnzymePortalReaction();
//        reactionElements.get(0);
        Equation equation = new Equation();
        equation.setDirection(createEquationSymbol(reaction));
        List<Link> linkList = new ArrayList<>();
        for (Object reactionElement : reactionElements) {
            if (reactionElement instanceof Map) {
                Map xRefs = (Map) reactionElement;
                linkList.addAll(xRefs.getLink());
            }
            if (reactionElement instanceof ReactantList) {
                List<Object> reactantList = ((ReactantList) reactionElement)
                		.getReactantListOrReactant();
                equation.getReactantlist().addAll(reactantList);
            }
            if (reactionElement instanceof ProductList) {
                List<Object> productList = ((ProductList) reactionElement)
                		.getProductListOrProduct();
                equation.getProductlist().addAll(productList);

            }
        }
        //Set reaction properties
        String rheaId = reaction.getId();
        enzymeReaction.setId(rheaId);
        String reactionTitle = createReactionTitle(equation);
        enzymeReaction.setName(reactionTitle);
        enzymeReaction.setEquation(equation);
        reactionPathway.setReaction( enzymeReaction);
        reactionPathway.getReactions().add(enzymeReaction);
        List<Object> mecList = getMechanismLinks(linkList);
        reactionPathway.setMechanism(mecList);
        List<Object> pwList = getPathwayLinks(linkList);
        setReactomeReactionRef(reactionPathway,pwList);
        return reactionPathway;
         }
          return reactionPathway;
    }

    private List<Molecule> getMolecules(List<Object> molObjList) {
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

    private String createEquationSymbol(Reaction reaction) {
        String rheaDirectionConv = reaction.getConvention();
        String direction = rheaDirectionConv.split(":")[1];
        return createEquationSymbol(direction);
    }

    private String createEquationSymbol(String direction) {
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

    private String createReactionTitle(Equation equation) {
        StringBuffer sb = new StringBuffer();
        List<Object> reacanttList = equation.getReactantlist();
        String reactantsString = createParticipantString(reacanttList);
        sb.append(reactantsString);
        sb.append(equation.getDirection());
        sb.append(createParticipantString(equation.getProductlist()));
        return sb.toString();
    }

    private String createParticipantString(List<Object> molList) {
        StringBuffer sb = new StringBuffer();
         int counter = 1;
        for (Object participant : molList) {
        	Molecule molecule = null;
        	Double coef = null;
        	if (participant instanceof Reactant){
        		final Reactant reactant = (Reactant) participant;
				molecule = reactant.getMolecule();
				coef = reactant.getCount();
        	} else if (participant instanceof Product){
        		final Product product = (Product) participant;
				molecule = product.getMolecule();
				coef = product.getCount();
        	} else {
        		throw new IllegalArgumentException("");
        	}
        	if (coef > 1){
        		sb.append(coef.intValue()).append(' ');
        	}
                for(Object name : molecule.getFormulaOrNameOrLabel()){
                    if(name instanceof Name){
                     Name n = (Name) name;  
                        sb.append(n.getValue());
                    }
                }
           
            if (counter < molList.size()) {
                sb.append(" + ");
            }
            counter++;
        }
        return sb.toString();
    }
    
    private List<Object> getMechanismLinks(List<Link> xRefs) {
        String macieDbCode = Database.MACIE.getDbCode();
        return getLinks(macieDbCode, xRefs);
    }
    
    private List<Object> getLinks(String dbCode,List<Link> xRefs) {
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

    private List<Object> getPathwayLinks(List<Link> xRefs) {
	    String reactomeDbCode = Database.REACTOME.getDbCode();
	    return getLinks(reactomeDbCode, xRefs);
    }

    private void setReactomeReactionRef(ReactionPathway reactionPathway, List<Object> urls) {
        EnzymeReaction enzymeReaction = reactionPathway.getReaction();
        for (Object url:urls) {
            Link urlString = (Link)url;
            enzymeReaction.getXrefs().add(urlString.getHref());
        }

    }
}

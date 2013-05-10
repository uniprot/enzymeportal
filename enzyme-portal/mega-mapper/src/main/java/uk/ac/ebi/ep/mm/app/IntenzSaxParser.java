package uk.ac.ebi.ep.mm.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import uk.ac.ebi.ep.mm.Entry;
import uk.ac.ebi.ep.mm.MmDatabase;
import uk.ac.ebi.ep.mm.Relationship;
import uk.ac.ebi.ep.mm.XRef;

/**
 * Parser for IntEnzXML which extracts data interesting for the Enzyme Portal
 * mega-map, namely cofactors, reactions, reactants and products as
 * cross-references:
 * <ul>
 *  <li>EC number &rarr; Rhea reactions</li>
 *  <li>EC number &rarr; ChEBI entities (cofactors, substrates, products)</li>
 *  <li>Rhea reaction &rarr; ChEBI entities (reactants, products)</li>
 * </ul>
 * Please note that in case of <b>reversible</b> (bidirectional) reactions -
 * most of the enzyme classification - the relationship for ChEBI entities will
 * be set
 * to {@link Relationship.is_substrate_or_product_of} (for EC cross-reference)
 * or {@link Relationship.is_reactant_or_product_of} (for Rhea cross-reference),
 * while <b>irreversible</b> (unidirectional: left-to-right or right-to-left in
 * Rhea speech) will have the more specific
 * {@link Relationship.is_substrate_of}/{@link Relationship.is_product_of}
 * (for EC cross-reference) or
 * {@link Relationship.is_reactant_of}/{@link Relationship.is_product_of}
 * (for Rhea cross-reference).
 * @author rafa
 * @since 1.0.16
 */
public class IntenzSaxParser extends MmSaxParser {

    private static final String ENZYME =
            "//intenz/ec_class/ec_subclass/ec_sub-subclass/enzyme";

    private static final String EC =
            ENZYME + "/ec";

    public static final String ACCEPTED_NAME =
            ENZYME + "/accepted_name";

    public static final String CML_REACTION =
            ENZYME +"/reactions/cml:reaction";

    public static final String REACTION_NAME =
            CML_REACTION + "/cml:name";

    public static final String RHEA_ID =
            CML_REACTION + "/cml:identifier";

    public static final String REACTANT =
            CML_REACTION + "/cml:reactantList/cml:reactant/cml:molecule";

    public static final String PRODUCT =
            CML_REACTION + "/cml:productList/cml:product/cml:molecule";

    public static final String COFACTOR =
            ENZYME + "/cofactors/cofactor";

    private Entry ecEntry, rheaEntry;

    private List<Entry> reactants = new ArrayList<Entry>(),
            products = new ArrayList<Entry>(),
            cofactors = new ArrayList<Entry>();

    /* Flags for processing element contents. */
    private boolean isEnzyme, isEc, isAcceptedName, isCmlReaction,
            isReactionName, isRheaId, isCofactor, isReactant, isProduct,
            isReversibleReaction;
    // Cross-references to write to the mega-map:
    Collection<XRef> xrefs = new ArrayList<XRef>();

    /**
     * Parses an IntEnzXML file and stores cross-references from EC numbers to
     * cofactors, reactants, products and reactions into a mega-map.
     * @param args see {@link CliOptionsParser#getCommandLine(String...)}.
     * @throws Exception in case of error while parsing.
     */
    public static void main(String... args) throws Exception {
        CommandLine cl = CliOptionsParser.getCommandLine(args);
        if (cl != null) mainParse(cl, new IntenzSaxParser());
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes)
    throws SAXException {
        currentContext.push(qName.isEmpty()? localName : qName);

        // Update flags: todo
        String currentXpath = getCurrentXpath();
        isEnzyme = ENZYME.equals(currentXpath);
        isEc = EC.equals(currentXpath);
        isAcceptedName = ACCEPTED_NAME.equals(currentXpath);
        isCmlReaction = CML_REACTION.equals(currentXpath);
        isReactionName = REACTION_NAME.equals(currentXpath);
        isRheaId = RHEA_ID.equals(currentXpath);
        isReactant = REACTANT.equals(currentXpath);
        isProduct = PRODUCT.equals(currentXpath);
        if (isCmlReaction){
            isReversibleReaction = attributes.getValue("", "convention")
                    .contains("direction.BI");
        }
        isCofactor = COFACTOR.equals(currentXpath);

        // Clear placeholder:
        if (currentChars.length() > 0){
            currentChars.delete(0, currentChars.length());
        }

        if (isEnzyme){
            ecEntry = new Entry();
            ecEntry.setDbName(MmDatabase.EC.name());
        } else if (isCmlReaction){
            rheaEntry = new Entry();
            rheaEntry.setDbName(MmDatabase.Rhea.name());
        } else if (isRheaId){
            rheaEntry.setEntryId(attributes.getValue("", "value"));
        } else if (isReactant){
            reactants.add(createChebiEntry(attributes));
        } else if (isProduct){
            products.add(createChebiEntry(attributes));
        } else if (isCofactor){
            Entry cofactor = new Entry();
            cofactor.setDbName(MmDatabase.ChEBI.name());
            cofactor.setEntryId(attributes.getValue("", "accession"));
            cofactors.add(cofactor);
        }
    }

    private Entry createChebiEntry(Attributes attributes) {
        Entry chebiEntry = new Entry();
        chebiEntry.setDbName(MmDatabase.ChEBI.name());
        chebiEntry.setEntryId(attributes.getValue("", "id"));
        chebiEntry.setEntryName(attributes.getValue("", "title"));
        return chebiEntry;
    }

    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {
        // Check whether we need to do something:
        if (isEc || isAcceptedName || isReactionName || isCofactor){
            currentChars.append(Arrays.copyOfRange(ch, start, start + length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException {
        if (isEc){
            ecEntry.setEntryId(currentChars.toString().replace("EC ", ""));
        } else if (isAcceptedName){
            ecEntry.setEntryName(currentChars.toString());
        } else if (isReactionName){
            rheaEntry.setEntryName(currentChars.toString());
        } else if (isCmlReaction){
            // Cross-reference EC-Rhea:
            XRef ecRheaXref = new XRef();
            ecRheaXref.setFromEntry(ecEntry);
            ecRheaXref.setToEntry(rheaEntry);
            ecRheaXref.setRelationship(Relationship.between(
                    MmDatabase.EC, MmDatabase.Rhea).name());
            xrefs.add(ecRheaXref);
            // Cross-references ChEBI-Rhea and ChEBI-EC:
            for (Entry reactant : reactants) {
                XRef chebiRheaXref = new XRef();
                chebiRheaXref.setFromEntry(reactant);
                chebiRheaXref.setToEntry(rheaEntry);
                chebiRheaXref.setRelationship(isReversibleReaction?
                    Relationship.is_reactant_or_product_of.name():
                    Relationship.is_reactant_of.name());
                xrefs.add(chebiRheaXref);
                XRef chebiEcXref = new XRef();
                chebiEcXref.setFromEntry(reactant);
                chebiEcXref.setToEntry(ecEntry);
                chebiEcXref.setRelationship(isReversibleReaction?
                        Relationship.is_substrate_or_product_of.name():
                        Relationship.is_substrate_of.name());
                xrefs.add(chebiEcXref);
            }
            for (Entry product : products) {
                XRef chebiRheaXref = new XRef();
                chebiRheaXref.setFromEntry(product);
                chebiRheaXref.setToEntry(rheaEntry);
                chebiRheaXref.setRelationship(isReversibleReaction?
                        Relationship.is_reactant_or_product_of.name():
                        Relationship.is_product_of.name());
                xrefs.add(chebiRheaXref);
                XRef chebiEcXref = new XRef();
                chebiEcXref.setFromEntry(product);
                chebiEcXref.setToEntry(ecEntry);
                chebiEcXref.setRelationship(isReversibleReaction?
                        Relationship.is_substrate_or_product_of.name():
                        Relationship.is_product_of.name());
                xrefs.add(chebiEcXref);
            }
            // Clean up:
            rheaEntry = null;
            reactants.clear();
            products.clear();
        } else if (isCofactor){
            // Add the missing name:
            cofactors.get(cofactors.size()-1)
                    .setEntryName(currentChars.toString());
        } else if (isEnzyme){
            for (Entry cofactor : cofactors) {
                XRef xref = new XRef();
                xref.setFromEntry(cofactor);
                xref.setToEntry(ecEntry);
                xref.setRelationship(Relationship.is_cofactor_of.name());
                xrefs.add(xref);
            }
            if (!xrefs.isEmpty()) try {
                mm.writeXrefs(xrefs);
                // Update enzyme name from EC:
                mm.updateEntry(ecEntry);
            } catch (IOException e) {
                throw new RuntimeException("Unable to add xrefs", e);
            }
            // Clean up:
            xrefs.clear();
            ecEntry = null;
            cofactors.clear();
        }

        currentContext.pop();
        // Update flags:
        String currentXpath = getCurrentXpath();
        isEnzyme = ENZYME.equals(currentXpath);
        isEc = false;
        isAcceptedName = false;
        isCmlReaction = CML_REACTION.equals(currentXpath);
        isReactionName = false;
        isRheaId = false;
        isReactant = false;
        isProduct = false;
        isCofactor = false;
    }
}

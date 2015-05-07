package uk.ac.ebi.ep.parser.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalEcNumbers;
import uk.ac.ebi.ep.data.domain.EnzymePortalReaction;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalEcNumbersRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalReactionRepository;
import uk.ac.ebi.ep.centralservice.helper.CompoundUtil;
import uk.ac.ebi.ep.centralservice.helper.EbinocleParser;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.centralservice.helper.Relationship;

/**
 * Parser for IntEnzXML which extracts data interesting for the Enzyme Portal
 * mega-map, namely cofactors, reactions, reactants and products as
 * cross-references:
 * <ul>
 * <li>EC number &rarr; Rhea reactions</li>
 * <li>EC number &rarr; ChEBI entities (cofactors, substrates, products)</li>
 * <li>Rhea reaction &rarr; ChEBI entities (reactants, products)</li>
 * </ul>
 * Please note that in case of <b>reversible</b> (bidirectional) reactions -
 * most of the enzyme classification - the relationship for ChEBI entities will
 * be set to {@link Relationship.is_substrate_or_product_of} (for EC
 * cross-reference) or {@link Relationship.is_reactant_or_product_of} (for Rhea
 * cross-reference), while <b>irreversible</b> (unidirectional: left-to-right or
 * right-to-left in Rhea speech) will have the more specific
 * {@link Relationship.is_substrate_of}/{@link Relationship.is_product_of} (for
 * EC cross-reference) or
 * {@link Relationship.is_reactant_of}/{@link Relationship.is_product_of} (for
 * Rhea cross-reference).
 *
 * $Author$
 *
 * @since 1.0.16
 */
public class IntenzSaxParser extends DefaultHandler implements EbinocleParser {

    private final Logger LOGGER = Logger.getLogger(IntenzSaxParser.class);
    /**
     * The current element (tree path) being parsed.
     */
    protected Stack<String> currentContext = new Stack<>();
    /**
     * The text value of the current element being parsed.
     */
    protected StringBuilder currentChars = new StringBuilder();

    private static final String ENZYME
            = "//intenz/ec_class/ec_subclass/ec_sub-subclass/enzyme";

    private static final String EC
            = ENZYME + "/ec";

    public static final String ACCEPTED_NAME
            = ENZYME + "/accepted_name";

    public static final String CML_REACTION
            = ENZYME + "/reactions/cml:reaction";

    public static final String REACTION_NAME
            = CML_REACTION + "/cml:name";

    public static final String RHEA_ID
            = CML_REACTION + "/cml:identifier";

    public static final String REACTANT
            = CML_REACTION + "/cml:reactantList/cml:reactant/cml:molecule";

    public static final String PRODUCT
            = CML_REACTION + "/cml:productList/cml:product/cml:molecule";

    public static final String COFACTOR
            = ENZYME + "/cofactors/cofactor";

    public static final String LINKS = ENZYME + "/links/link";

    private UniprotEntry ecEntry;
    private EnzymePortalReaction rheaEntry;

    private final List<EnzymePortalCompound> reactants = new ArrayList<>();
    private final List<EnzymePortalCompound> products = new ArrayList<>();
    private final List<EnzymePortalCompound> cofactors = new ArrayList<>();

    /* Flags for processing element contents. */
    private boolean isEnzyme, isEc, isAcceptedName, isCmlReaction,
            isReactionName, isRheaId, isCofactor, isReactant, isProduct,
            isReversibleReaction;
    // Cross-references to write to the mega-map:
    Collection<EnzymePortalCompound> xrefs = new HashSet<>();

    private final EnzymePortalCompoundRepository compoundRepository;

    private final EnzymePortalEcNumbersRepository ecNumbersRepository;
    private final EnzymePortalReactionRepository reactionRepository;

    public IntenzSaxParser(EnzymePortalCompoundRepository repository, EnzymePortalEcNumbersRepository ec, EnzymePortalReactionRepository reactionRepo) {
        this.compoundRepository = repository;
        this.ecNumbersRepository = ec;
        this.reactionRepository = reactionRepo;
    }

    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes)
            throws SAXException {
        currentContext.push(qName.isEmpty() ? localName : qName);

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
        if (isCmlReaction) {
            isReversibleReaction = attributes.getValue("", "convention")
                    .contains("direction.BI");
        }
        isCofactor = COFACTOR.equals(currentXpath);

        // Clear placeholder:
        if (currentChars.length() > 0) {
            currentChars.delete(0, currentChars.length());
        }

        if (isEnzyme) {

            ecEntry = new UniprotEntry();

        } else if (isCmlReaction) {
            rheaEntry = new EnzymePortalReaction();
            rheaEntry.setReactionSource(MmDatabase.Rhea.name());

        } else if (isRheaId) {
            rheaEntry.setReactionId(attributes.getValue("", "value"));

        } else if (isReactant) {
            reactants.add(createChebiEntry(attributes));
        } else if (isProduct) {
            products.add(createChebiEntry(attributes));
        } else if (isCofactor) {
            EnzymePortalCompound cofactor = new EnzymePortalCompound();
            cofactor.setCompoundSource(MmDatabase.ChEBI.name());
            cofactor.setCompoundId(attributes.getValue("", "accession"));
            cofactors.add(cofactor);

        }
    }

    private EnzymePortalCompound createChebiEntry(Attributes attributes) {
        EnzymePortalCompound chebiEntry = new EnzymePortalCompound();
        chebiEntry.setCompoundSource(MmDatabase.ChEBI.name());
        chebiEntry.setCompoundId(attributes.getValue("", "id"));
        chebiEntry.setCompoundName(attributes.getValue("", "title"));

        return chebiEntry;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        // Check whether we need to do something:
        if (isEc || isAcceptedName || isReactionName || isCofactor) {
            currentChars.append(Arrays.copyOfRange(ch, start, start + length));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (isEc) {

            ecEntry.setName(currentChars.toString().replace("EC ", ""));

        } else if (isAcceptedName) {

            ecEntry.setProteinName(currentChars.toString());
        } else if (isReactionName) {
            rheaEntry.setReactionName(currentChars.toString());

        } else if (isCmlReaction) {

            List<EnzymePortalEcNumbers> summaryList = ecNumbersRepository.findByEcNumber(ecEntry.getName());
            if (summaryList != null && !summaryList.isEmpty()) {
                for (EnzymePortalEcNumbers summary : summaryList) {

                    // Cross-reference EC-Rhea:
                    EnzymePortalReaction reaction_per_ec = null;

                    if (rheaEntry != null) {
                        EnzymePortalReaction reaction = new EnzymePortalReaction();

                        reaction.setReactionId(rheaEntry.getReactionId());
                        reaction.setReactionName(rheaEntry.getReactionName());
                        reaction.setReactionSource(rheaEntry.getReactionSource());
                        reaction.setRelationship(Relationship.between(
                                MmDatabase.EC, MmDatabase.Rhea).name());
                        reaction.setUniprotAccession(summary.getUniprotAccession());

                        reaction_per_ec = reactionRepository.save(reaction);
                    }

                    // Cross-references ChEBI-Rhea and ChEBI-EC:
                    for (EnzymePortalCompound reactant : reactants) {

                        EnzymePortalCompound chebiEcXref = new EnzymePortalCompound();
                        chebiEcXref.setCompoundId(reactant.getCompoundId());
                        chebiEcXref.setCompoundName(reactant.getCompoundName());
                        chebiEcXref.setCompoundSource(reactant.getCompoundSource());
                        chebiEcXref.setUniprotAccession(summary.getUniprotAccession());
                        chebiEcXref.getEnzymePortalReactionSet().add(reaction_per_ec);

                        chebiEcXref.setRelationship(isReversibleReaction
                                ? Relationship.is_substrate_or_product_of.name()
                                : Relationship.is_substrate_of.name());
                        chebiEcXref = CompoundUtil.computeRole(chebiEcXref, chebiEcXref.getRelationship());
                        if (chebiEcXref.getCompoundName() != null) {
                            xrefs.add(chebiEcXref);
                        }

                    }
                    for (EnzymePortalCompound product : products) {

                        EnzymePortalCompound chebiEcXref = new EnzymePortalCompound();

                        chebiEcXref.setCompoundId(product.getCompoundId());
                        chebiEcXref.setCompoundName(product.getCompoundName());
                        chebiEcXref.setCompoundSource(product.getCompoundSource());
                        chebiEcXref.setUniprotAccession(summary.getUniprotAccession());
                        chebiEcXref.getEnzymePortalReactionSet().add(reaction_per_ec);

                        chebiEcXref.setRelationship(isReversibleReaction
                                ? Relationship.is_substrate_or_product_of.name()
                                : Relationship.is_product_of.name());
                        chebiEcXref = CompoundUtil.computeRole(chebiEcXref, chebiEcXref.getRelationship());
                        if (chebiEcXref.getCompoundName() != null) {
                            xrefs.add(chebiEcXref);
                        }

                    }

                    // Clean up:
                    rheaEntry = null;
                    reactants.clear();
                    products.clear();
                    reaction_per_ec = null;

                }
            }//end if ec does not return protein
        } else if (isCofactor) {
            // Add the missing name:
            cofactors.get(cofactors.size() - 1)
                    .setCompoundName(currentChars.toString());
        } else if (isEnzyme) {

            //we now get cofactors from uniprot -- comment out for now
//            if (cofactors != null || !cofactors.isEmpty()) {
//                List<EnzymePortalEcNumbers> summaryList = ecNumbersRepository.findByEcNumber(ecEntry.getName());
//
//                if (summaryList != null && !summaryList.isEmpty()) {
//                    for (EnzymePortalEcNumbers summary : summaryList) {
//                        for (EnzymePortalCompound cofactor : cofactors) {
//                            EnzymePortalCompound chebi_cofactor = new EnzymePortalCompound();
//                            chebi_cofactor.setCompoundId(cofactor.getCompoundId());
//                            chebi_cofactor.setCompoundName(cofactor.getCompoundName());
//                            chebi_cofactor.setCompoundSource(cofactor.getCompoundSource());
//                            chebi_cofactor.setUniprotAccession(summary.getUniprotAccession());
//
//                            chebi_cofactor.setRelationship(Relationship.is_cofactor_of.name());
//                            chebi_cofactor = CompoundUtil.computeRole(chebi_cofactor, chebi_cofactor.getRelationship());
//                            if (chebi_cofactor.getCompoundName() != null) {
//                                xrefs.add(chebi_cofactor);
//                            }
//
//                        }
//                    }
//                }
//            }

            if (!xrefs.isEmpty()) {


                LOGGER.info("writing reaction and compounds to DB "+ xrefs.size());
                compoundRepository.save(xrefs);

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

    // use abstract class to manage this methods across various parsers
    protected String getCurrentXpath() {
        StringBuilder xpath = new StringBuilder("/");
        currentContext.stream().forEach((string) -> {
            xpath.append('/').append(string);
        });
        return xpath.toString();
    }

    /**
     * Parses a XML file and indexes/stores cross-references in a mega-map.<br>
     * This method is not thread safe.
     *
     * @param xmlFilePath the XML file to parse
     * @throws java.io.FileNotFoundException if the UniProt XML file is not
     * found or not readable.
     * @throws org.xml.sax.SAXException if no default XMLReader can be found or
     * instantiated, or exception during parsing.
     * @throws java.io.IOException if the lucene index cannot be opened/created,
     * or from the parser.
     */
    public void parse(String xmlFilePath) throws Exception {
        File xmlFile = new File(xmlFilePath);
        parse(new FileInputStream(xmlFile));
    }

    private void parse(InputStream is) throws IOException, SAXException {

        try {

            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(this);
            xr.setErrorHandler(this);
            InputSource source = new InputSource(is);
            LOGGER.info("Parsing start");
            xr.parse(source);
            LOGGER.info("Parsing end");

        } catch (IOException | SAXException e) {
            LOGGER.error("During parsing", e);

            throw e;
        }
    }

}

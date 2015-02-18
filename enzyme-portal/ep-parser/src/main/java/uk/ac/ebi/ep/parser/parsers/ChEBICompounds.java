/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.DataItem;
import uk.ac.ebi.chebi.webapps.chebiWS.model.Entity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntityList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;
import uk.ac.ebi.ep.data.domain.EnzymePortalCompound;
import uk.ac.ebi.ep.data.domain.EnzymePortalSummary;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.parser.helper.CompoundUtil;
import uk.ac.ebi.ep.parser.helper.EPUtil;
import uk.ac.ebi.ep.parser.helper.MmDatabase;
import uk.ac.ebi.ep.parser.helper.Relationship;

/**
 *
 * @author joseph
 */
public class ChEBICompounds {

    // use this when parsing the molecule name - we don't want a molecule with parentesis eg sucrose (DTTI)
    private static final Pattern COMPOUND_NAME_PATTERN
            = Pattern.compile("(.*?)(?: \\((.*?)\\))?");

    private final Logger LOGGER = Logger.getLogger(ChEBICompounds.class);
    private final ChebiWebServiceClient chebiWsClient;

    Map<UniprotEntry, Set<String>> inhibitors = new LinkedHashMap<>();
    Map<UniprotEntry, Set<String>> activators = new LinkedHashMap<>();

    List<EnzymePortalCompound> compounds = new LinkedList<>();

    //private static final String COMMENT_TYPE = "ENZYME_REGULATION";
     private static final String COMMENT_TYPE = "REGULATION";

    private final EnzymePortalCompoundRepository compoundRepository;

    private final EnzymePortalSummaryRepository enzymeSummaryRepository;

    public static final String[] BLACKLISTED_COMPOUNDS = {"ACID", "acid" ,"H(2)O", "H(+)", "ACID", "WATER", "water", "ion", "ION"};
    List<String> blackList = Arrays.asList(BLACKLISTED_COMPOUNDS);

    public ChEBICompounds(EnzymePortalSummaryRepository enzymeSummaryRepository, EnzymePortalCompoundRepository repository) {
        this.compoundRepository = repository;
        this.enzymeSummaryRepository = enzymeSummaryRepository;
        chebiWsClient = new ChebiWebServiceClient();

    }

    public void computeAndLoadChEBICompounds() {


       
          List<EnzymePortalSummary> enzymeSummary = enzymeSummaryRepository.findSummariesByCommentType(COMMENT_TYPE);

       //Java 7 and before only. uncomment if Java 8 is not available in your env
//
//        for (EnzymePortalSummary summary : enzymeSummary) {
//            String enzyme_regulation_text = summary.getCommentText();
//         
//            inhibitors.put(summary.getUniprotAccession(), EPUtil.parseTextForInhibitors(enzyme_regulation_text));
//            activators.put(summary.getUniprotAccession(), EPUtil.parseTextForActivators(enzyme_regulation_text));
//        }

//        for (Map.Entry<UniprotEntry, Set<String>> map : inhibitors.entrySet()) {
//            UniprotEntry key = map.getKey();
//            for (String inhibitor : map.getValue()) {
//                EnzymePortalCompound inhibitor_from_chebi = searchMoleculeInChEBI(inhibitor);
//                if (inhibitor_from_chebi != null) {
//                    inhibitor_from_chebi.setRelationship(Relationship.is_inhibitor_of.name());
//                    inhibitor_from_chebi.setUniprotAccession(map.getKey());
//                    compounds.add(inhibitor_from_chebi);
//                }
//            }
//
//        }
//
//        for (Map.Entry<UniprotEntry, Set<String>> map : activators.entrySet()) {
//            UniprotEntry key = map.getKey();
//            for (String activator : map.getValue()) {
//                EnzymePortalCompound activator_from_chebi = searchMoleculeInChEBI(activator);
//                if (activator_from_chebi != null) {
//           
//                    activator_from_chebi.setRelationship(Relationship.is_activator_of.name());
//                    activator_from_chebi.setUniprotAccession(key);
//                    compounds.add(activator_from_chebi);
//                }
//            }
//
//        }
        //Java 8 specifics - comment out  and uncomment above if java 8 is not found in env
        enzymeSummary.stream().forEach((summary) -> {
            String enzyme_regulation_text = summary.getCommentText();
          
            inhibitors.put(summary.getUniprotAccession(), EPUtil.parseTextForInhibitors(enzyme_regulation_text));
            activators.put(summary.getUniprotAccession(), EPUtil.parseTextForActivators(enzyme_regulation_text));
        });

        inhibitors.entrySet().stream().forEach((map) -> {
            map.getValue().stream().map((inhibitor) -> searchMoleculeInChEBI(inhibitor)).filter((inhibitor_from_chebi) -> (inhibitor_from_chebi != null)).map((inhibitor_from_chebi) -> {
                inhibitor_from_chebi.setRelationship(Relationship.is_inhibitor_of.name());
                inhibitor_from_chebi = CompoundUtil.computeRole(inhibitor_from_chebi, inhibitor_from_chebi.getRelationship());
                return inhibitor_from_chebi;
            }).map((inhibitor_from_chebi) -> {
                inhibitor_from_chebi.setUniprotAccession(map.getKey());
                return inhibitor_from_chebi;
            }).forEach((inhibitor_from_chebi) -> {
                compounds.add(inhibitor_from_chebi);
            });
        });

        activators.entrySet().stream().forEach((map) -> {
            map.getValue().stream().map((activator) -> searchMoleculeInChEBI(activator)).filter((activator_from_chebi) -> (activator_from_chebi != null)).map((activator_from_chebi) -> {
                activator_from_chebi.setRelationship(Relationship.is_activator_of.name());
                activator_from_chebi = CompoundUtil.computeRole(activator_from_chebi, activator_from_chebi.getRelationship());
                return activator_from_chebi;
            }).map((activator_from_chebi) -> {
                activator_from_chebi.setUniprotAccession(map.getKey());
                return activator_from_chebi;
            }).forEach((activator_from_chebi) -> {
                compounds.add(activator_from_chebi);
            });
        });

        
        LOGGER.debug("Writing to Enzyme Portal database...");
        compoundRepository.save(compounds);

        inhibitors.clear();
        activators.clear();
    }

    /**
     * Searches a compound name in ChEBI. Please note that if the name does not
     * match <i>exactly</i> any names/synonyms returned by ChEBI, the result
     * will be <code>null</code>.
     *
     * @param moleculeName the compound name.
     * @return an entry with a ChEBI ID, or <code>null</code> if not found.
     */
    protected EnzymePortalCompound searchMoleculeInChEBI(String moleculeName) {
        EnzymePortalCompound entry = null;
        // Sometimes moleculeName comes as "moleculeName (ACRONYM)"
        // sometimes as "moleculeName (concentration)":
        Matcher m = COMPOUND_NAME_PATTERN.matcher(moleculeName);
        m.matches(); // always
        String[] nameAcronym = {m.group(1), m.group(2)};
        // first name, then acronym (if any):
        nameLoop:
        for (String name : nameAcronym) {
            if (name == null) {
                continue; // acronym, usually
            }
            try {
                LiteEntityList lites = chebiWsClient.getLiteEntity(
                        name, SearchCategory.ALL_NAMES, 25, StarsCategory.ALL);
                String chebiId = null;
                if (lites != null) {
                    liteLoop:
                    for (LiteEntity lite : lites.getListElement()) {
                        Entity completeEntity = chebiWsClient
                                .getCompleteEntity(lite.getChebiId());
                        List<String> synonyms = new ArrayList<>();
                        for (DataItem dataItem : completeEntity.getSynonyms()) {
                            synonyms.add(dataItem.getData().toLowerCase());
                        }
                        List<String> formulae = new ArrayList<>();
                        for (DataItem formula : completeEntity.getFormulae()) {
                            formulae.add(formula.getData());
                        }
                        if (completeEntity.getChebiAsciiName()
                                .equalsIgnoreCase(name)
                                || synonyms.contains(name.toLowerCase())
                                || formulae.contains(name)) {
                            chebiId = completeEntity.getChebiId();
                        }
                        if (chebiId != null) {
                            break liteLoop;
                        }
                    }
                }

                if (chebiId != null && !blackList.contains(name) && !StringUtils.isEmpty(name)) {
                    entry = new EnzymePortalCompound();
                    entry.setCompoundSource(MmDatabase.ChEBI.name());
                    entry.setCompoundId(chebiId);
                    entry.setCompoundName(name);
                    break nameLoop;
                } else {
                    LOGGER.warn("Not found in ChEBI: " + name);
                }
            } catch (ChebiWebServiceFault_Exception e) {
                LOGGER.error("Searching for " + name, e);
            }
        }
        return entry;
    }
}

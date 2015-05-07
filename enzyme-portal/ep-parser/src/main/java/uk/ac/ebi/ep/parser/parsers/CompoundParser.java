/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import uk.ac.ebi.ep.data.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;

/**
 *
 * @author joseph
 */
public abstract class CompoundParser {

    protected final Logger LOGGER = Logger.getLogger(CompoundParser.class);
    protected final ChebiWebServiceClient chebiWsClient;

    protected final EnzymePortalCompoundRepository compoundRepository;

    protected final EnzymePortalSummaryRepository enzymeSummaryRepository;

    protected static final Pattern COMPOUND_NAME_PATTERN
            = Pattern.compile("(.*?)(?: \\((.*?)\\))?");

    public static final String[] BLACKLISTED_COMPOUNDS = {"ACID", "acid", "H(2)O", "H(+)", "ACID", "WATER", "water", "ion", "ION", "", " "};
    protected List<String> blackList = Arrays.asList(BLACKLISTED_COMPOUNDS);

    public CompoundParser(ChebiWebServiceClient chebiWsClient, EnzymePortalCompoundRepository compoundRepository, EnzymePortalSummaryRepository enzymeSummaryRepository) {
        this.chebiWsClient = chebiWsClient;
        this.compoundRepository = compoundRepository;
        this.enzymeSummaryRepository = enzymeSummaryRepository;

    }

    public abstract void loadCofactors();

    /**
     * Searches a compound name in ChEBI. Please note that if the name does not
     * match <i>exactly</i> any names/synonyms returned by ChEBI, the result
     * will be <code>null</code>.
     *
     * @param moleculeName the compound name.
     * @return an entry with a ChEBI ID, or <code>null</code> if not found.
     */
    protected EnzymePortalCompound searchCompoundInChEBI(String moleculeName) {

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
                String chebiName = null;

                if (lites != null) {
                    liteLoop:
                    for (LiteEntity lite : lites.getListElement()) {
                        chebiId = lite.getChebiId();
                        chebiName = lite.getChebiAsciiName();//default

                        Entity completeEntity = chebiWsClient
                                .getCompleteEntity(lite.getChebiId());
                        for (DataItem dataItem : completeEntity.getSynonyms()) {

                            if ("UniProt".equalsIgnoreCase(dataItem.getSource())) {
                                chebiName = dataItem.getData();
                            }
                        }

                    }
                }

                if (chebiId != null && !blackList.contains(chebiName) && !StringUtils.isEmpty(chebiName)) {
                    entry = new EnzymePortalCompound();
                    entry.setCompoundSource(MmDatabase.ChEBI.name());
                    entry.setCompoundId(chebiId);
                    entry.setCompoundName(chebiName);
                    entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString="+chebiId);
                    break;
                } else {
                    LOGGER.warn("Not found in ChEBI: " + name);
                }
            } catch (ChebiWebServiceFault_Exception e) {
                LOGGER.error("ChebiWebServiceFault_Exception while Searching for " + name, e);
            }
        }
        return entry;
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
                            break;
                        }
                    }
                }

                if (chebiId != null && !blackList.contains(name) && !StringUtils.isEmpty(name)) {
                    entry = new EnzymePortalCompound();
                    entry.setCompoundSource(MmDatabase.ChEBI.name());
                    entry.setCompoundId(chebiId);
                    entry.setCompoundName(name);
                     entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString="+chebiId);
                    break;
                }
//                else {
//                    LOGGER.warn("Not found in ChEBI: " + name);
//                }
            } catch (ChebiWebServiceFault_Exception e) {
                LOGGER.error("Searching for " + name, e);
            }
        }
        return entry;
    }

}

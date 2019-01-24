/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import uk.ac.ebi.chebi.webapps.chebiWS.client.ChebiWebServiceClient;
import uk.ac.ebi.chebi.webapps.chebiWS.model.ChebiWebServiceFault_Exception;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntity;
import uk.ac.ebi.chebi.webapps.chebiWS.model.LiteEntityList;
import uk.ac.ebi.chebi.webapps.chebiWS.model.SearchCategory;
import uk.ac.ebi.chebi.webapps.chebiWS.model.StarsCategory;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.model.repositories.EnzymePortalCompoundRepository;
import uk.ac.ebi.ep.model.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author joseph <joseph@ebi.ac.uk>
 */
@Deprecated
public abstract class CompoundParser {

    protected final Logger LOGGER = Logger.getLogger(CompoundParser.class);
    protected final ChebiWebServiceClient chebiWsClient;

    protected final EnzymePortalCompoundRepository compoundRepository;

    protected final EnzymePortalSummaryRepository enzymeSummaryRepository;
    protected final EnzymePortalParserService parserService;

    protected static final Pattern COMPOUND_NAME_PATTERN
            = Pattern.compile("(.*?)(?: \\((.*?)\\))?");

    public static final String[] BLACKLISTED_COMPOUNDS = {"ACID", "acid", "H(2)O", "H(+)", "ACID", "WATER", "water", "ion", "ION", "", " "};
    protected List<String> blackList = Arrays.asList(BLACKLISTED_COMPOUNDS);

    public CompoundParser(ChebiWebServiceClient chebiWsClient, EnzymePortalCompoundRepository compoundRepository, EnzymePortalSummaryRepository enzymeSummaryRepository, EnzymePortalParserService pService) {
        this.chebiWsClient = chebiWsClient;
        this.compoundRepository = compoundRepository;
        this.enzymeSummaryRepository = enzymeSummaryRepository;
        this.parserService = pService;

    }

    public abstract void loadCofactors();

    /**
     * Searches a compound name in ChEBI. Please note that if the name does not
     * match <i>exactly</i> any names/synonyms returned by ChEBI, the result
     * will be <code>null</code>.
     *
     * @param moleculeName the compound name (special cases are chebi ID's.
     * @return an entry with a ChEBI ID, or <code>null</code> if not found.
     */
    protected LiteCompound findByChEBIiD(String moleculeName) {

        LiteCompound entry = null;
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

            Optional<LiteEntityList> lites = Optional.empty();
            try {
                lites = Optional.ofNullable(chebiWsClient.getLiteEntity(
                        name, SearchCategory.ALL_NAMES, 25, StarsCategory.ALL));
            } catch (Exception e) {
                LOGGER.error("Error while searching ChEBI using the name : " + name, e);
            }
            String chebiId = null;
            String chebiName = null;

            if (lites.isPresent()) {
                liteLoop:
                for (LiteEntity lite : lites.get().getListElement()) {
                    chebiId = lite.getChebiId();
                    chebiName = lite.getChebiAsciiName();//default
                }
            }

            if (chebiId != null && !blackList.contains(chebiName) && !StringUtils.isEmpty(chebiName)) {

                entry = new LiteCompound();
                entry.setCompoundSource(MmDatabase.ChEBI.name());
                entry.setCompoundId(chebiId);
                entry.setCompoundName(chebiName);
                entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId);
          
            } else {
                LOGGER.info("Not found in ChEBI: " + name);
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
    protected LiteCompound findByCompoundName(String moleculeName) {

        LiteCompound entry = null;
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
                Optional<LiteEntityList> lites = Optional.ofNullable(chebiWsClient.getLiteEntity(
                        name, SearchCategory.ALL_NAMES, 25, StarsCategory.ALL));
                String chebiId = null;
                String chebiName = null;

                if (lites.isPresent()) {
                    liteLoop:
                    for (LiteEntity lite : lites.get().getListElement()) {

                        chebiId = lite.getChebiId();
                        chebiName = lite.getChebiAsciiName();//name is used instead of chebiName as to maintain names from uniprot

                    }
                }

                if (chebiId != null && !blackList.contains(name) && !StringUtils.isEmpty(chebiName)) {

                    entry = new LiteCompound();
                    entry.setCompoundSource(MmDatabase.ChEBI.name());
                    entry.setCompoundId(chebiId);
                    entry.setCompoundName(name);
                    entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId);
                 } else {
                    LOGGER.info("Not found in ChEBI: " + name);
                }
            } catch (ChebiWebServiceFault_Exception e) {
                LOGGER.error("Error while Searching for molecule name : " + name, e);
            }
        }
        return entry;
    }
    


}

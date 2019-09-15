package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.metaboliteService.service.ChebiService;
import uk.ac.ebi.ep.model.ChebiCompound;
import uk.ac.ebi.ep.model.dao.CofactorView;
import uk.ac.ebi.ep.model.dao.Compound;
import uk.ac.ebi.ep.model.dao.Summary;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.model.LiteCompound;

/**
 *
 * @author joseph
 */
@Slf4j
public class ChebiCofactors {

    private static final String COFACTOR_URL = "https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=";

    protected static final String UNIPROT = "UniProt";
    protected static final String IUPAC = "IUPAC";
    protected static final String COMMENT_TYPE = "COFACTORS";
    protected static final String NAME = "Name=([^\\s]+)";
    protected static final String XREF = "Xref=ChEBI:([^\\s]+)";
    protected static final String NOTE = "Note=([^\\*]+)";

    protected static final Pattern COMPOUND_NAME_PATTERN
            = Pattern.compile("(.*?)(?: \\((.*?)\\))?");

    public static final String[] BLACKLISTED_COMPOUNDS = {"A", "ACID", "acid", "H(2)O", "H2O", "H(+)", "ACID", "WATER", "water", "ion", "ION", "", " "};
    protected List<String> blackList = Arrays.asList(BLACKLISTED_COMPOUNDS);
    protected final EnzymePortalParserService enzymePortalParserService;
    protected final ChebiService chebiService;
    private List<LiteCompound> compounds = null;

    public ChebiCofactors(EnzymePortalParserService enzymePortalParserService, ChebiService chebiService) {
        this.enzymePortalParserService = enzymePortalParserService;
        this.chebiService = chebiService;
        compounds = new ArrayList<>();
    }

    public void loadUniqueCofactorsToDatabase() {
        List<CofactorView> cofactors = enzymePortalParserService.findCofactors();
        log.info("Number of cofactors found " + cofactors.size());
        log.debug("About to load cofactors to database .......");
        cofactors.forEach(cofactor -> createCofactor(cofactor));
        log.info("Done loading unique cofactors to database");
    }

    private void createCofactor(CofactorView cofactor) {
        String url = COFACTOR_URL + cofactor.getCompoundId();
        enzymePortalParserService.createCofactor(cofactor.getCompoundId(), cofactor.getCompoundName(), url);
    }

    public void loadChebiCofactorToDatabase() {
        List<Summary> enzymeSummary = enzymePortalParserService.findSummariesByCommentType(COMMENT_TYPE);
        //.stream().limit(10).collect(Collectors.toList());

        log.info("Number of Regulation Text from EnzymeSummary Table to parse for cofactors " + enzymeSummary.size());

        parseCofactorText(enzymeSummary);
    }

    protected ChebiCompound findChebiCompoundById(String chebiId) {
        ChebiCompound chebiCompound = enzymePortalParserService.findChebiCompoundById(chebiId, UNIPROT)
                .stream()
                .filter(c -> c.getSource().equalsIgnoreCase(UNIPROT))
                .distinct().findFirst().orElse(null);
        if (chebiCompound == null) {
            chebiCompound = enzymePortalParserService.findChebiCompoundById(chebiId, IUPAC)
                    .stream()
                    .filter(c -> c.getSource().equalsIgnoreCase(IUPAC))
                    .distinct().findFirst().orElse(null);
        }

        return chebiCompound;
    }

    private LiteCompound buildCompoundDao(LiteCompound compound, String accession, String note) {

        String compoundId = compound.getCompoundId();
        String compoundName = compound.getCompoundName();
        String compoundSource = compound.getCompoundSource();
        String relationship = Relationship.is_cofactor_of.name();
        String compoundRole = Compound.Role.COFACTOR.name();
        String url = compound.getUrl();

        compound.setCompoundId(compoundId);
        compound.setCompoundName(compoundName);
        compound.setCompoundSource(compoundSource);
        compound.setRelationship(relationship);
        compound.setUniprotAccession(accession);
        compound.setUrl(url);
        compound.setCompoundRole(compoundRole);
        compound.setNote(note);
        return compound;
    }

    protected String getChebiSynonyms(String chebiId, String preferredName) {
        return chebiService.getChebiSynonyms(chebiId)
                .stream()
                .filter(c -> !c.equalsIgnoreCase(preferredName))
                .filter(l -> l.length() > 1)
                .limit(1_000)
                .collect(Collectors.joining(";"));
    }

    private void loadCompound(LiteCompound compound, AtomicInteger count) {
        String synonyms = null;
        if (compound.getCompoundId() != null && compound.getCompoundName() != null) {
            synonyms = getChebiSynonyms(compound.getCompoundId(), compound.getCompoundName());
        }

        log.info("writing cofactor " + compound.getCompoundId() + " count " + count.getAndIncrement());

        enzymePortalParserService.createChebiCompound(compound.getCompoundId(), compound.getCompoundName(), synonyms, compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());

        //load Web enzyme_portal compound table
        //enzymePortalParserService.createCompound(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
    }

    private void computeSpecialCases(String text, Summary summary, String note) {
        final Pattern xrefPattern = Pattern.compile(XREF);
        final Matcher xrefMatcher = xrefPattern.matcher(text);

        while (xrefMatcher.find()) {

            String xref = xrefMatcher.group(1).replaceAll(";", "");

            if (xref != null) {

                log.debug("Special case : xref search in CHEBI " + xref);
                Optional<LiteCompound> liteCompound = Optional.ofNullable(findByChEBIiD(xref));

                if (liteCompound.isPresent()) {

                    LiteCompound daoCompound = buildCompoundDao(liteCompound.get(), summary.getAccession(), note);

                    compounds.add(daoCompound);
                    log.debug("added compound for special case " + daoCompound.getCompoundId() + " <> " + daoCompound.getCompoundName());

                }

            }

        }

    }

    AtomicInteger count = new AtomicInteger(1);

    private void parseCofactorText(List<Summary> enzymeSummary) {
        enzymeSummary.stream().forEach(summary -> processCofactors(summary));
        //save compounds
        log.info("Writing to Enzyme Portal database... Number of cofactors to write : " + compounds.size());

        compounds
                .stream()
                .filter(compound -> compound != null)
                .forEach(compound -> loadCompound(compound, count));
        log.info("-------- Done populating the database with cofactors ---------------");
        compounds.clear();

    }

    private void processCofactors(Summary summary) {
        String cofactorText = summary.getCommentText();
        String note = "";
        final Pattern notePattern = Pattern.compile(NOTE);
        final Matcher noteMatcher = notePattern.matcher(cofactorText);

        while (noteMatcher.find()) {

            note = noteMatcher.group(1);

        }

        final Pattern namePattern = Pattern.compile(NAME);
        final Matcher nameMatcher = namePattern.matcher(cofactorText);

        while (nameMatcher.find()) {

            String cofactorName = nameMatcher.group(1).replaceAll(";", "");

            if (cofactorName != null) {
                log.debug("cofactor name search in CHEBI Compound Table " + cofactorName);
                Optional<LiteCompound> liteCompound = Optional.ofNullable(findByCompoundName(cofactorName));

                if (liteCompound.isPresent()) {

                    LiteCompound daoCompound = buildCompoundDao(liteCompound.get(), summary.getAccession(), note);
                    compounds.add(daoCompound);

                }
                if (!liteCompound.isPresent()) {
                    computeSpecialCases(cofactorText, summary, note);
                }
            }

        }
    }

    /**
     * Searches a compound name in ChEBI. Please note that if the name does not
     * match <i>exactly</i> any names/synonyms returned by ChEBI, the result
     * will be <code>null</code>.
     *
     * @param moleculeName the compound name (special cases are chebi ID's.
     * @return an entry with a ChEBI ID, or <code>null</code> if not found.
     */
    private LiteCompound findByChEBIiD(String moleculeName) {

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

            ChebiCompound chebiCompound = findChebiCompoundById(name);

            if (chebiCompound != null) {

                entry = buildLiteCompound(chebiCompound);
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
    private LiteCompound findByCompoundName(String moleculeName) {

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

            ChebiCompound chebiCompound = findChebiCompoundById(name);

            if (chebiCompound != null) {

                entry = buildLiteCompound(chebiCompound);

            }

        }
        return entry;
    }

    private LiteCompound buildLiteCompound(ChebiCompound chebiCompound) {
        LiteCompound entry = null;
        String chebiId = chebiCompound.getChebiAccession();
        String chebiName = chebiCompound.getCompoundName();

        if (chebiId != null && !blackList.contains(chebiName) && !StringUtils.isEmpty(chebiName)) {

            entry = new LiteCompound();
            entry.setCompoundSource(MmDatabase.ChEBI.name());
            entry.setCompoundId(chebiId);
            entry.setCompoundName(chebiName);
            entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId);

        }
        return entry;
    }

}

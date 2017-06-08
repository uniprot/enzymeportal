package uk.ac.ebi.ep.parser.parsers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.data.domain.ChebiCompound;
import uk.ac.ebi.ep.data.entry.Summary;
import uk.ac.ebi.ep.data.repositories.EnzymePortalSummaryRepository;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.service.EnzymePortalParserService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class CofactorsFtpFiles implements ICompoundParser {

    private final Logger logger = Logger.getLogger(CofactorsFtpFiles.class);
    private List<LiteCompound> compounds = null;
    private static final String COMMENT_TYPE = "COFACTORS";
    private static final String NAME = "Name=([^\\s]+)";
    private static final String XREF = "Xref=ChEBI:([^\\s]+)";
    private static final String NOTE = "Note=([^\\*]+)";

    protected static final Pattern COMPOUND_NAME_PATTERN
            = Pattern.compile("(.*?)(?: \\((.*?)\\))?");

    public static final String[] BLACKLISTED_COMPOUNDS = {"ACID", "acid", "H(2)O", "H(+)", "ACID", "WATER", "water", "ion", "ION", "", " "};
    protected List<String> blackList = Arrays.asList(BLACKLISTED_COMPOUNDS);

    private final EnzymePortalParserService enzymePortalParserService;

    private final EnzymePortalSummaryRepository enzymeSummaryRepository;

    private static final String UNIPROT = "UniProt";
    private static final String IUPAC = "IUPAC";

    public CofactorsFtpFiles(EnzymePortalParserService enzymePortalParserService, EnzymePortalSummaryRepository enzymeSummaryRepository) {
        this.enzymePortalParserService = enzymePortalParserService;
        this.enzymeSummaryRepository = enzymeSummaryRepository;
        compounds = new ArrayList<>();
    }

    @Override
    public void loadCofactors() {
        List<Summary> enzymeSummary = enzymeSummaryRepository.findSummariesByCommentType(COMMENT_TYPE);

        logger.info("Number of Regulation Text from EnzymeSummary Table to parse for cofactors " + enzymeSummary.size());

        parseCofactorText(enzymeSummary);
    }

    private void computeSpecialCases(String text, Summary summary, String note) {
        final Pattern xrefPattern = Pattern.compile(XREF);
        final Matcher xrefMatcher = xrefPattern.matcher(text);

        while (xrefMatcher.find()) {

            String xref = xrefMatcher.group(1).replaceAll(";", "");

            if (xref != null) {

                logger.info("Special case : xref search in CHEBI " + xref);
                Optional<LiteCompound> liteCompound = Optional.ofNullable(findByChEBIiD(xref));

                if (liteCompound.isPresent()) {
                    LiteCompound compound = liteCompound.get();
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
                    compound.setUniprotAccession(summary.getAccession());
                    compound.setUrl(url);
                    compound.setCompoundRole(compoundRole);
                    compound.setNote(note);
                    compounds.add(compound);
                    logger.info("added compound for special case " + compound.getCompoundId() + " <> " + compound.getCompoundName());

                }

            }

        }

    }

    private void parseCofactorText(List<Summary> enzymeSummary) {
        enzymeSummary.stream().forEach(summary -> processCofactors(summary));
        //save compounds
        logger.warn("Writing to Enzyme Portal database... Number of cofactors to write : " + compounds.size());

        compounds
                .stream()
                .filter(compound -> compound != null)
                .forEach(compound -> {
             enzymePortalParserService.createCompound(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
                });
        logger.warn("-------- Done populating the database with cofactors ---------------");
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
                logger.info("cofactor name search in CHEBI Compound Table " + cofactorName);
                Optional<LiteCompound> liteCompound = Optional.ofNullable(findByCompoundName(cofactorName));

                if (liteCompound.isPresent()) {
                    LiteCompound compound = liteCompound.get();
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
                    compound.setUniprotAccession(summary.getAccession());
                    compound.setUrl(url);
                    compound.setCompoundRole(compoundRole);
                    compound.setNote(note);
                    compounds.add(compound);

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

            ChebiCompound chebiCompound = enzymePortalParserService.findChebiCompoundById(name, UNIPROT)
                    .stream()
                    .filter(c -> c.getSource().equalsIgnoreCase(UNIPROT))
                    .distinct().findFirst().orElse(null);
            if (chebiCompound == null) {
                chebiCompound = enzymePortalParserService.findChebiCompoundById(name, IUPAC)
                        .stream()
                        .filter(c -> c.getSource().equalsIgnoreCase(IUPAC))
                        .distinct().findFirst().orElse(null);
            }

            if (chebiCompound != null) {
                String chebiId = chebiCompound.getChebiAccession();
                String chebiName = chebiCompound.getCompoundName();

                if (chebiId != null && !blackList.contains(chebiName) && !StringUtils.isEmpty(chebiName)) {

                    entry = new LiteCompound();
                    entry.setCompoundSource(MmDatabase.ChEBI.name());
                    entry.setCompoundId(chebiId);
                    entry.setCompoundName(chebiName);
                    entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId);

                }
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

            ChebiCompound chebiCompound = enzymePortalParserService.findChebiCompoundByName(name, UNIPROT)
                    .stream()
                    .filter(c -> c.getSource().equalsIgnoreCase(UNIPROT))
                    .distinct().findFirst().orElse(null);
            if (chebiCompound == null) {
                chebiCompound = enzymePortalParserService.findChebiCompoundByName(name, IUPAC)
                        .stream()
                        .filter(c -> c.getSource().equalsIgnoreCase(IUPAC))
                        .distinct().findFirst().orElse(null);

            }

            if (chebiCompound != null) {

                String chebiId = chebiCompound.getChebiAccession();
                String chebiName = chebiCompound.getCompoundName();

                if (chebiId != null && !blackList.contains(name) && !StringUtils.isEmpty(chebiName)) {

                    entry = new LiteCompound();
                    entry.setCompoundSource(MmDatabase.ChEBI.name());
                    entry.setCompoundId(chebiId);
                    entry.setCompoundName(name);
                    entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId);
                }

            }

        }
        return entry;
    }

}
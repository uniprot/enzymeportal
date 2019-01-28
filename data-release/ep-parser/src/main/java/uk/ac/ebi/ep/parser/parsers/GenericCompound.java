package uk.ac.ebi.ep.parser.parsers;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
@Slf4j
public abstract class GenericCompound {

    protected static final String COMMENT_TYPE = "COFACTORS";
    protected static final String NAME = "Name=([^\\s]+)";
    protected static final String XREF = "Xref=ChEBI:([^\\s]+)";
    protected static final String NOTE = "Note=([^\\*]+)";

    protected static final Pattern COMPOUND_NAME_PATTERN
            = Pattern.compile("(.*?)(?: \\((.*?)\\))?");

    public static final String[] BLACKLISTED_COMPOUNDS = {"ACID", "acid", "H(2)O","H2O", "H(+)", "ACID", "WATER", "water", "ion", "ION", "", " "};
    protected List<String> blackList = Arrays.asList(BLACKLISTED_COMPOUNDS);

    protected final EnzymePortalParserService enzymePortalParserService;

    protected static final String UNIPROT = "UniProt";
    protected static final String IUPAC = "IUPAC";

    public GenericCompound(EnzymePortalParserService enzymePortalParserService) {
        this.enzymePortalParserService = enzymePortalParserService;
    }

    abstract void loadCompoundToDatabase();
}

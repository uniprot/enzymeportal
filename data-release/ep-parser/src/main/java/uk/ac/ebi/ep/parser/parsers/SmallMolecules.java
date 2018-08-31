package uk.ac.ebi.ep.parser.parsers;

import java.util.List;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author Joseph
 */
public abstract class SmallMolecules {

    private final EnzymePortalParserService parserService;

    public SmallMolecules(EnzymePortalParserService parserService) {
        this.parserService = parserService;
    }

    abstract void loadChEMBL();

    protected List<String> findUniqueTargetedproteins() {

        return parserService.findUniqueTargetedproteins();

    }

    protected List<String> findProteinTargetets(String accession) {
        return parserService.findTargetetsByProtein(accession);
    }
}

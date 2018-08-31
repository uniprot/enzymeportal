package uk.ac.ebi.ep.parser.parsers;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.model.Targets;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;

/**
 *
 * @author Joseph
 */
@Slf4j
public class TargetParser {

    private final ChemblXmlParser chemblXmlParser;
    private final EnzymePortalParserService parserService;

    public TargetParser(ChemblXmlParser chemblXmlParser, EnzymePortalParserService parserService) {
        this.chemblXmlParser = chemblXmlParser;
        this.parserService = parserService;
    }

    @Deprecated
    public Map<String, List<String>> parseChemblTargetXML() {
        Map<String, List<String>> chemblTargets = new HashMap<>();
        try {
            chemblTargets = chemblXmlParser.parseChemblTarget();
        } catch (FileNotFoundException ex) {
            log.error("chembl-target_component.xml not found", ex);
        }
        return chemblTargets;
    }

    public Set<Targets> parseChemblTargetsXML() {
        Set<Targets> chemblTargets = new HashSet<>();
        try {
            chemblTargets = chemblXmlParser.parseChemblTargets();

        } catch (FileNotFoundException ex) {
            log.error("chembl-target_component.xml not found", ex);
        }
        return chemblTargets;
    }
//load targets to database

    public void loadChemblTargetsToDB() {

        parserService.disableTargetContraints();
        Set<Targets> targets = parseChemblTargetsXML();

        targets.stream().forEach(target
                -> target.getChemblId()
                        .stream()
                        .forEach(chemblId -> loadToDB(chemblId, target.getComponentType(), target.getAccession())));

        parserService.deleteNonEnzymesTargets();

        parserService.enableTargetContraints();
    }

    private void loadToDB(String chemblId, String componentType, String accession) {
        parserService.addChemblTargets(chemblId, componentType, accession);
    }
}

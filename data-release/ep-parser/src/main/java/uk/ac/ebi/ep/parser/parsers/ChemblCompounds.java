package uk.ac.ebi.ep.parser.parsers;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.model.Targets;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;

/**
 *
 * @author Joseph
 */
@Slf4j
public class ChemblCompounds {

    private final ChemblXmlParser chemblXmlParser;
    private final EnzymePortalParserService parserService;

    private final ChemblService chemblService;

    public ChemblCompounds(ChemblService chemblService, ChemblXmlParser chemblXmlParser, EnzymePortalParserService parserService) {
        this.chemblService = chemblService;
        this.chemblXmlParser = chemblXmlParser;
        this.parserService = parserService;
    }

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

    public void loadChEMBL() {

        List<String> uniqueTargetedproteins = findUniqueTargetedproteins();
        //.stream().limit(1).collect(Collectors.toList());

        log.warn(" Number of unique targeted proteins found " + uniqueTargetedproteins.size());

        uniqueTargetedproteins
                .forEach(protein -> chemblService.processChemblSmallMolecules(findProteinTargetets(protein), protein));

        loadToDB();

    }

    private List<String> findUniqueTargetedproteins() {

        return parserService.findUniqueTargetedproteins();

    }

    private List<String> findProteinTargetets(String accession) {
        return parserService.findTargetetsByProtein(accession);
    }

    private void loadToDB() {
        List<TempCompoundCompare> compounds = chemblService.getChemblCompounds().stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());

        //load into database
        if (compounds != null) {

//            System.out.println("Num compounds found " + compounds.size());
//            compounds.stream().forEach(x->System.out.println(x.getUniprotAccession() +" Data "+ x.getCompoundId() + " "+ x.getCompoundName() + " "+ x.getPrimaryTargetId()));
            log.warn("About to load the temporal compounds found ::::::  " + compounds.size());
            //UPDATE DB
            compounds.stream().filter((compound) -> (compound != null)).forEach((compound) -> {
                parserService.addTempCompound(compound.getPrimaryTargetId(), compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
            });

            log.warn("Finished loading temporal compound table ::::::  ");
            log.warn("*******************Updating compound table ignoring duplicates ************");
            parserService.insertCompoundsFromTempTable();
            log.warn("**********DONE*************** ");

        }
    }

}

package uk.ac.ebi.ep.parser.parsers;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.model.ChemblTargets;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.model.Targets;
import uk.ac.ebi.ep.parser.xmlparser.ChemblXmlParser;

/**
 *
 * @author Joseph
 */
public class ChemblCompounds {

    private final Logger logger = Logger.getLogger(ChemblCompounds.class);
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
            logger.error("chembl-target_component.xml not found", ex);
        }
        return chemblTargets;
    }

    public Set<Targets> parseChemblTargetsXML() {
        Set<Targets> chemblTargets = new HashSet<>();
        try {
            chemblTargets = chemblXmlParser.parseChemblTargets();

        } catch (FileNotFoundException ex) {
            logger.error("chembl-target_component.xml not found", ex);
        }
        return chemblTargets;
    }

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

        List<ChemblTargets> chemblTargets = parserService.findChemblTargets();
        logger.warn("Number of ChEMBL targets to process  : " + chemblTargets.size());

        chemblTargets
                .stream()
                .forEach(chemblTarget -> chemblService.chemblSmallMolecules(chemblTarget.getChemblId(), chemblTarget.getUniprotAccession().getAccession()));

        logger.warn("Done processing ChEMBL targets and now reading TempCompoundCompare table to update compound table : " + chemblTargets.size());

        List<TempCompoundCompare> compounds = chemblService.getChemblCompounds().stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());

        //load into database
        if (compounds != null) {

            System.out.println("Num compounds found " + compounds.size());

            logger.warn("About to load the temporal compounds found ::::::  " + compounds.size());
            //UPDATE DB
            compounds.stream().filter((compound) -> (compound != null)).forEach((compound) -> {
                parserService.addTempCompound(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
            });

            logger.warn("Finished loading temporal compound table ::::::  ");
            logger.warn("*******************Updating compound table ignoring duplicates ************");
            parserService.insertCompoundsFromTempTable();
            logger.warn("**********DONE*************** ");

        }

    }

}

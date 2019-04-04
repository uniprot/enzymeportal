package uk.ac.ebi.ep.parser.parsers;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.centralservice.chembl.service.ChemblService;
import uk.ac.ebi.ep.model.TempCompoundCompare;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author Joseph
 */
@Slf4j
public class ChemblCompounds extends SmallMolecules {

    private final EnzymePortalParserService parserService;

    private final ChemblService chemblService;


    public ChemblCompounds(EnzymePortalParserService parserService, ChemblService chemblService) {
        super(parserService);
        this.parserService = parserService;
        this.chemblService = chemblService;
    }

    @Override
    public void loadChEMBL() {

        List<String> uniqueTargetedproteins = findUniqueTargetedproteins();
        //.stream().limit(20).collect(Collectors.toList());

        log.info(" Number of unique targeted proteins found " + uniqueTargetedproteins.size());
        
      //uncomment if we don't need to track how many processed
//        uniqueTargetedproteins
//                .forEach(protein -> chemblService.processChemblSmallMolecules(findProteinTargetets(protein), protein));

        AtomicInteger count = new AtomicInteger(1);
        uniqueTargetedproteins
                .stream()
                .map(protein -> {
            chemblService.processChemblSmallMolecules(findProteinTargetets(protein), protein);
            return protein;
        }).forEachOrdered((x) -> log.info(x+ " Num processed : " + count.getAndIncrement()));

        loadToDB();

    }


    private void loadToDB() {
        List<TempCompoundCompare> compounds = chemblService.getChemblCompounds().stream().distinct().filter(Objects::nonNull).collect(Collectors.toList());

        //load into database
        if (compounds != null) {


            log.info("About to load the temporal compounds found ::::::  " + compounds.size());
            //UPDATE DB
            compounds.stream().filter((compound) -> (compound != null)).forEach((compound) -> {
                parserService.addTempCompound(compound.getPrimaryTargetId(), compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
            });

            log.info("Finished loading temporal compound table ::::::  ");
            log.info("*******************Updating compound table ignoring duplicates ************");
            parserService.insertCompoundsFromTempTable();
            log.info("**********DONE*************** ");

        }
    }

}

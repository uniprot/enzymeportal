package uk.ac.ebi.ep.parser.parsers;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import uk.ac.ebi.ep.centralservice.helper.MmDatabase;
import uk.ac.ebi.ep.centralservice.helper.Relationship;
import uk.ac.ebi.ep.model.ChebiCompound;
import uk.ac.ebi.ep.model.EnzymeReactionInfo;
import uk.ac.ebi.ep.model.dao.Compound;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
@Deprecated // no longer needed. Rhea reactants are pulled by database pipeline
@Slf4j
public class CompoundReactantParser extends GenericCompound {

    private static final String XREF_TYPE = "CHEBI";
    //private List<LiteCompound> reactants = null;

    public CompoundReactantParser(EnzymePortalParserService enzymePortalParserService) {
        super(enzymePortalParserService);
       // reactants = new ArrayList<>();
    }

//    private void loadCompound(LiteCompound compound) {
//        enzymePortalParserService.createReactant(compound.getCompoundId(), compound.getCompoundName(), compound.getCompoundSource(), compound.getRelationship(), compound.getUniprotAccession(), compound.getUrl(), compound.getCompoundRole(), compound.getNote());
//    }

    @Transactional(readOnly = false)
    @Override
    void loadCompoundToDatabase() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AtomicInteger counter = new AtomicInteger(1);
        Long total = enzymePortalParserService.countReactionInfo(XREF_TYPE);
        //Long total = enzymePortalParserService.countDistinctReactionInfoByXrefType(XREF_TYPE);
        log.info("About to start streaming and processing " + total + " entries.");
         try (Stream<EnzymeReactionInfo> reactionInfo = enzymePortalParserService.findAllReactionInfoByXrefTypeAndStream(XREF_TYPE)) {
        //try (Stream<EnzymeReactionInfo> reactionInfo = enzymePortalParserService.findUniqueXrefReactionInfoByXrefTypeAndStream(XREF_TYPE)) {
            reactionInfo.forEach(data -> processReactionInfo(data, counter));

        }
       // log.info("Writing to Enzyme Portal database... Number of reactants to write : " + reactants.size());

//        reactants
//                .stream()
//                .filter(compound -> compound != null)
//                .forEach(compound -> loadCompound(compound));
        log.warn("-------- Done populating the database with reactants ---------------"+ LocalDate.now());
        //reactants.clear();
        stopWatch.stop();
     
        log.info("Time taken to process "+ total +" entries:  "+ stopWatch.getTotalTimeSeconds() + " seconds");

    }

    private void processReactionInfo(EnzymeReactionInfo reactionInfo, AtomicInteger counter) {
       log.info("Processing CHEBI ID : " + reactionInfo.getXref() + " Num processed so far : " + counter.getAndIncrement());

        ChebiCompound chebiCompound = enzymePortalParserService.findChebiCompoundById(reactionInfo.getXref(), UNIPROT)
                .stream()
                .filter(c -> c.getSource().equalsIgnoreCase(UNIPROT))
                .distinct().findFirst().orElse(null);
        if (chebiCompound == null) {
            chebiCompound = enzymePortalParserService.findChebiCompoundById(reactionInfo.getXref(), IUPAC)
                    .stream()
                    .filter(c -> c.getSource().equalsIgnoreCase(IUPAC))
                    .distinct().findFirst().orElse(null);
        }

        if (chebiCompound != null) {
            String chebiId = chebiCompound.getChebiAccession();
            String chebiName = chebiCompound.getCompoundName();
            if(chebiName == null){
                chebiName = chebiId;
            }

            //if (chebiId != null && !blackList.contains(chebiName) && !StringUtils.isEmpty(chebiName)) {

//                LiteCompound entry = new LiteCompound();
//                entry.setCompoundId(chebiId);
//                entry.setCompoundName(chebiName);
//                entry.setUniprotAccession(reactionInfo.getUniprotAccession());
//                entry.setCompoundSource(MmDatabase.ChEBI.name());
//                entry.setRelationship(Relationship.is_reactant_of.name());
//                entry.setCompoundRole(Compound.Role.REACTANT.name());
//                entry.setNote(reactionInfo.getReactionDirection());//this is only specific to reactant
//                entry.setUrl("https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId);
//                reactants.add(entry);
                
                enzymePortalParserService.createReactant(chebiId, chebiName, MmDatabase.ChEBI.name(),Relationship.is_reactant_of.name(), reactionInfo.getUniprotAccession(), "https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=" + chebiId, Compound.Role.REACTANT.name(), reactionInfo.getReactionDirection());
    

            //}

        }
    }

}

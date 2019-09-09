package uk.ac.ebi.ep.parser.parsers;

/**
 *
 * @author joseph
 */
//@Slf4j
@Deprecated
public class MetaboliteParser {

//    AtomicInteger count = new AtomicInteger(1);
//    private final EnzymePortalReactantRepository enzymePortalReactantRepository;
//
//    public MetaboliteParser(EnzymePortalReactantRepository enzymePortalReactantRepository) {
//
//        this.enzymePortalReactantRepository = enzymePortalReactantRepository;
//
//    }
//
//    @Transactional(readOnly = true)
//    public void updateMetabolite(MetabolightService metabolightService) {
//        AtomicInteger counter = new AtomicInteger(1);
//        List<String> chebiList = new ArrayList<>();
//
//        StopWatch watch = new StopWatch();
//        watch.start();
//        log.info("Starting streaming now ...");
//        try (Stream<String> chebiIds = enzymePortalReactantRepository.streamChebiIds()) {
//
//            chebiIds.forEach(x -> System.out.println(" my data " + x));
////                    .parallel()
////                    .filter(id -> metabolightService.isMetabolite(id))
////                    .forEach(id -> chebiList.add(id));
//
//            log.info("Number of Reactants to process " + chebiList.size());
//
//            chebiList
//                    .parallelStream()
//                    .limit(10)
//                    //                    .parallel()
//                    //                    .map(chebiId -> enzymePortalReactantRepository.findEnzymePortalReactantByReactantId(chebiId))
//                    //                    .flatMap(x -> x.parallelStream())
//                    //                    .map(m -> updateEnzymePortalReactant(m))
//                    //                    .parallel()
//                    //                    .forEach(x -> enzymePortalReactantRepository.saveAndFlush(x));
//                    .forEach(id -> updateReactantByChebiId(id, counter));
//
////            
////            List<String> chebiList1 = chebiList.stream().limit(10).collect(Collectors.toList());
////            for (String chebiId : chebiList1) {
////         
////                List<EnzymePortalReactant> reactants = enzymePortalReactantRepository.findEnzymePortalReactantByReactantId(chebiId);
////                //List<EnzymePortalReactant> reactantList = new ArrayList<>();
////                log.error("NUM REACTANT TO PROCESS "+ reactants.size());
////                reactants.parallelStream()
////                        .map(reactant -> updateEnzymePortalReactant(reactant))
////                        .forEach(updateReactant -> enzymePortalReactantRepository.saveAndFlush(updateReactant) );
//////                for (EnzymePortalReactant reactant : reactants) {
//////
//////                    EnzymePortalReactant updateReactant = updateEnzymePortalReactant(reactant);
//////                    //reactantList.add(updateReactant);
//////                    enzymePortalReactantRepository.saveAndFlush(updateReactant);
//////                }
////               // enzymePortalReactantRepository.saveAll(reactantList);
////                   log.info("Just updated ChEBI Id ::: " + chebiId + " Count : " + counter.getAndIncrement());
////            }
//        }
//
//        watch.stop();
//
//        log.info("Time taken  ::: " + watch.getTotalTimeSeconds() + " secs" + " Or " + watch.getTotalTimeSeconds() / 60 + " mins");
//
//    }
//
//    @Transactional(readOnly = true)
//    public void updateMetaboliteStream(MetabolightService metabolightService) {
//        AtomicInteger counter = new AtomicInteger(1);
//        // List<String> chebiList = new ArrayList<>();
//
//        StopWatch watch = new StopWatch();
//        watch.start();
//        log.info("Starting streaming now ...");
//        try (Stream<ChebiReactant> chebiReactants = enzymePortalReactantRepository.streamChebiReactants()) {
//
//            chebiReactants
//                    //.parallel()
//                    .filter(reactant -> metabolightService.isMetabolite(reactant.getChebiId()))
//                    //.parallel()
//                    .forEach(reactant -> updateReactantByChebiId(reactant, counter));
//
//        }
//// Just updated ChEBI Id ::: CHEBI:30839 Count : 22473
//        watch.stop();
//
//        log.info("Time taken  ::: " + watch.getTotalTimeSeconds() + " secs" + " Or " + watch.getTotalTimeSeconds() / 60 + " mins");
//
//    }
//
//    //@Modifying(clearAutomatically = true, flushAutomatically = true)
//    //@Transactional
//    private void updateReactantByChebiId(ChebiReactant chebiReactant, AtomicInteger counter) {
//
//        String relationship = Relationship.is_reactant_or_product_of.name();
//        String role = Compound.Role.METABOLITE.name();
//        enzymePortalReactantRepository.updateReactant(role, relationship, chebiReactant.getChebiId(), chebiReactant.getReactantInternalId());
//
//        log.info("Just updated ChEBI Id ::: " + chebiReactant.getChebiId() + " Count : " + counter.getAndIncrement());
//
//    }
//
//    //@Transactional
//    private void updateReactantByChebiId(String chebiId, AtomicInteger counter) {
//        List<EnzymePortalReactant> reactants = enzymePortalReactantRepository.findEnzymePortalReactantByReactantId(chebiId);
//
//        // updateALLById(reactants, chebiId);
//        updateById(reactants, chebiId);
//        //log.info(reactants.size() + " entries updated done for ChEBI Id ::: " + chebiId + " Count : " + counter.getAndIncrement());
//        //updateEnzymePortalReactant(chebiId);
//        log.info("Just updated ChEBI Id ::: " + chebiId + " Count : " + counter.getAndIncrement());
//
//    }
//
//    //@Deprecated
//    //@Modifying(clearAutomatically = true)
//    // @Transactional
//    private void updateALLById(List<EnzymePortalReactant> reactants, String chebiId) {
//        reactants
//                .parallelStream()
//                //.parallel()
//                .map(m -> updateEnzymePortalReactant(m))
//                .collect(Collectors.toList());
//
//        log.warn("Num of Entries to update for ID " + chebiId + " = " + reactants.size());
//
//        enzymePortalReactantRepository.saveAll(reactants);
//    }
//
//    //@Deprecated
//    // @Modifying(clearAutomatically = true)
//    //@Transactional
//    private void updateById(List<EnzymePortalReactant> reactants, String chebiId) {
//        log.debug("Num of Entries to update for ID " + chebiId + " = " + reactants.size());
//        reactants
//                .parallelStream()
//                .map(m -> updateEnzymePortalReactant(m))
//                //.collect(Collectors.toList())
//                //.parallelStream()
//                .forEach(metabolite -> enzymePortalReactantRepository.saveAndFlush(metabolite));
//
//    }
//
//    //@Deprecated
//    private EnzymePortalReactant updateEnzymePortalReactant(EnzymePortalReactant reactant) {
//        String relationship = Relationship.is_reactant_or_product_of.name();
//        String role = Compound.Role.METABOLITE.name();
//        reactant.setRelationship(relationship);
//        reactant.setReactantRole(role);
//        //log.error("count this "+ count.getAndIncrement());
//        return reactant;
//    }

//    @Modifying(clearAutomatically = true)
//    @Transactional
//    private void updateEnzymePortalReactant(String reactantId) {
//        String relationship = Relationship.is_reactant_or_product_of.name();
//        String role = Compound.Role.METABOLITE.name();
//        enzymePortalReactantRepository.updateReactant(role, relationship, reactantId);
//    }
}

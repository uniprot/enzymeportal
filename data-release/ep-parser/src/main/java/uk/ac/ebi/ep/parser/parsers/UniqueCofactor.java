package uk.ac.ebi.ep.parser.parsers;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.model.search.model.CofactorView;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;

/**
 *
 * @author joseph
 */
@Slf4j
public class UniqueCofactor extends GenericCompound {
    
    private static final String COFACTOR_URL = "https://www.ebi.ac.uk/chebi/advancedSearchFT.do?searchString=";
    
    public UniqueCofactor(EnzymePortalParserService enzymePortalParserService) {
        super(enzymePortalParserService);
    }
    
    @Override
    void loadCompoundToDatabase() {
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
    
}

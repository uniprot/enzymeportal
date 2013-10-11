package uk.ac.ebi.ep.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import uk.ac.ebi.ep.adapter.bioportal.BioportalConfig;
import uk.ac.ebi.ep.adapter.chebi.ChebiConfig;
import uk.ac.ebi.ep.adapter.ebeye.EbeyeConfig;
import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;
import uk.ac.ebi.ep.adapter.reactome.ReactomeConfig;
import uk.ac.ebi.ep.adapter.uniprot.UniprotConfig;
import uk.ac.ebi.ep.core.compare.EnzymeComparison;
import uk.ac.ebi.ep.core.search.Config;
import uk.ac.ebi.ep.core.search.EnzymeRetriever;
import uk.ac.ebi.ep.entry.exception.EnzymeRetrieverException;
import uk.ac.ebi.ep.enzyme.model.EnzymeModel;
import uk.ac.ebi.ep.search.model.SearchModel;
import uk.ac.ebi.ep.search.model.SearchParams;

/**
 * Controller for enzyme comparisons.
 * @author rafa
 *
 */
@Controller
public class CompareController {

    private final Logger LOGGER = Logger.getLogger(CompareController.class);
    
    @Autowired
    private EbeyeConfig ebeyeConfig;
    @Autowired
    private UniprotConfig uniprotConfig;
    @Autowired
    private Config searchConfig;
    @Autowired
    private IntenzConfig intenzConfig;
    @Autowired
    private ReactomeConfig reactomeConfig;
    @Autowired
    private ChebiConfig chebiConfig;
    @Autowired
    private BioportalConfig bioportalConfig;

    @RequestMapping(value="/compare")
    protected String getComparison(Model model,
            /* as params only for testing, move them to session: */
            @RequestParam(value = "acc1", required = true) String acc1,
            @RequestParam(value = "acc2", required = true) String acc2)
    throws EnzymeRetrieverException{
        LOGGER.debug("Creating enzyme retriever...");
        EnzymeRetriever retriever = new EnzymeRetriever(searchConfig);
        retriever.getEbeyeAdapter().setConfig(ebeyeConfig);
        retriever.getUniprotAdapter().setConfig(uniprotConfig);
        retriever.getIntenzAdapter().setConfig(intenzConfig);
        retriever.getReactomeAdapter().setConfig(reactomeConfig);
        retriever.getChebiAdapter().setConfig(chebiConfig);
        retriever.getBioportalAdapter().setConfig(bioportalConfig);
        
        final EnzymeModel model1 = getWholeEnzymeModel(acc1, retriever);
        final EnzymeModel model2 = getWholeEnzymeModel(acc2, retriever);
        LOGGER.debug("Comparison started...");
        EnzymeComparison comparison = new EnzymeComparison(model1, model2);
        LOGGER.debug("Comparison finished");
        model.addAttribute("comparison", comparison);
        model.addAttribute("searchModel", newEmptySearchModel());
        return "comparison";
    }

    private SearchModel newEmptySearchModel() {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setStart(0);
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }
    
    private EnzymeModel getWholeEnzymeModel(String acc,
            EnzymeRetriever retriever) throws EnzymeRetrieverException{
        LOGGER.debug("Retrieving enzyme model...");
        EnzymeModel model = retriever.getEnzyme(acc);
        model.setProteinstructure(
                retriever.getProteinStructure(acc).getProteinstructure());
        model.setReactionpathway(
                retriever.getReactionsPathways(acc).getReactionpathway());
        model.setMolecule(retriever.getMolecules(acc).getMolecule());
        model.setDisease(retriever.getDiseases(acc).getDisease());
        LOGGER.debug("Retrieved");
        return model;
    }
}

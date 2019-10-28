package uk.ac.ebi.ep.web.service;

import uk.ac.ebi.ep.web.model.EnzymeModel;

/**
 *
 * @author joseph
 */
public interface EntryPageService {

    EnzymeModel getDefaultEnzymeModel(String accession);

    EnzymeModel showEntryEnzymePage(EnzymeModel model);

    EnzymeModel showProteinStructurePage(EnzymeModel model);

    EnzymeModel showRheaReactionsAndMechanisms(EnzymeModel model);

    EnzymeModel showPathwaysPage(EnzymeModel model);

    EnzymeModel showLiteraturePage(EnzymeModel model, int limit);

    EnzymeModel showDiseasePage(EnzymeModel model);

    EnzymeModel showCompoundPage(EnzymeModel model);

}

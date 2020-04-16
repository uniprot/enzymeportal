package uk.ac.ebi.ep.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;
import uk.ac.ebi.ep.web.logging.SeachCategory;
import uk.ac.ebi.ep.web.logging.SeachType;
import uk.ac.ebi.ep.web.logging.SearchQueryLog;
import uk.ac.ebi.ep.web.model.EnzymeModel;
import uk.ac.ebi.ep.web.service.EntryPageService;
import uk.ac.ebi.ep.web.tools.Attribute;
import uk.ac.ebi.ep.web.utils.EntryPageField;

/**
 *
 * @author joseph
 */
@Controller
@Slf4j
public class EntryPageController extends LegacyMethods {

    private static final String ENZYME_MODEL = "enzymeModel";
    private static final String ERROR = "entryPageError";
    private static final String ENTRY = "entry";

    private static final Integer MAX_CITATIONS = 50;

    private final EntryPageService entryPageService;

    @Autowired
    public EntryPageController(EntryPageService entryPageService) {
        this.entryPageService = entryPageService;
    }

    private void addEnzymeModel(Model model, String accession, EntryPageField requestedField, HttpSession session, EnzymeModel enzymeModel) {
        if (enzymeModel != null) {

            enzymeModel.setRequestedfield(requestedField.name().toLowerCase());
            model.addAttribute(ENZYME_MODEL, enzymeModel);

            addToHistory(session, accession);

        }
    }

    private EntryPageField getEntryPageField(String field) {
        if (field == null || StringUtils.isEmpty(field)) {
            field = EntryPageField.ENZYME.getName();
        }
        return EntryPageField.valueOf(field.toUpperCase());
    }

    /**
     * Process the entry page,
     *
     * @param accession The UniProt accession of the enzyme.
     * @param field the requested tab.
     * @param model
     * @param session
     * @return
     */
    @GetMapping(value = "/search/{accession}/{field}")
    public String getEnzymeModel(Model model, @PathVariable(required = true) String accession, @PathVariable(required = false) String field, HttpSession session) {

        EntryPageField requestedField = getEntryPageField(field);
        EnzymeModel enzymeModel = entryPageService.getDefaultEnzymeModel(accession);
        if (enzymeModel == null) {
            return ERROR;
        }

        switch (requestedField) {
            case ENZYME:
                enzymeModel = entryPageService.showEntryEnzymePage(enzymeModel);
                SearchQueryLog.logSearchQuery(SeachType.PROTEIN_PAGE, SeachCategory.UNIPROT_ACCESSION, accession);
                break;

            case PROTEINSTRUCTURE:
                enzymeModel = entryPageService.showProteinStructurePage(enzymeModel);
                break;
            case REACTIONSMECHANISMS:
                enzymeModel = entryPageService.showRheaReactionsAndMechanisms(enzymeModel);
                break;
            case MOLECULES:

                enzymeModel = entryPageService.showCompoundPage(enzymeModel);
                break;
            case DISEASEDRUGS:

                enzymeModel = entryPageService.showDiseasePage(enzymeModel);
                break;
            case LITERATURE:

                enzymeModel = entryPageService.showLiteraturePage(enzymeModel, MAX_CITATIONS);
                model.addAttribute("maxCitations", MAX_CITATIONS);
                break;
            default:
                enzymeModel = entryPageService.showEntryEnzymePage(enzymeModel);
                requestedField = EntryPageField.ENZYME;
                break;
        }
        addEnzymeModel(model, accession, requestedField, session, enzymeModel);
        return ENTRY;
    }

    @GetMapping(value = "/search/{accession}/pathways")
    public String getEnzymePathway(Model model, @PathVariable(required = true) String accession, HttpSession session) {
        EnzymeModel enzymeModel = entryPageService.getDefaultEnzymeModel(accession);
        enzymeModel = entryPageService.showPathwaysPage(enzymeModel);

        model.addAttribute(ENZYME_MODEL, enzymeModel);

        addToHistory(session, accession);
        return "reactomePathways";
    }

    /**
     * Updates the {@link lastSummaries Attribute#lastSummaries} attribute in
     * the user's session.
     *
     * @param session
     * @param summaries
     */
    protected void setLastSummaries(HttpSession session, List<ProteinView> summaries) {
        @SuppressWarnings("unchecked")
        Map<String, ProteinView> lastSummaries = (Map<String, ProteinView>) session.getAttribute(Attribute.lastSummaries.getName());
        if (lastSummaries == null) {
            lastSummaries = new HashMap<>();
            session.setAttribute(Attribute.lastSummaries.getName(), lastSummaries);
        } else {
            lastSummaries.clear();
        }
        for (ProteinView summary : summaries) {
            lastSummaries.put(summary.getAccession(), summary);
        }
    }

}

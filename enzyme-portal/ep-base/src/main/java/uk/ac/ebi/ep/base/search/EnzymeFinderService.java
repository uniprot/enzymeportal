package uk.ac.ebi.ep.base.search;

import java.util.List;
import uk.ac.ebi.ep.data.domain.EnzymePortalDisease;
import uk.ac.ebi.ep.data.domain.EnzymePortalPathways;
import uk.ac.ebi.ep.data.exceptions.EnzymeFinderException;
import uk.ac.ebi.ep.data.search.model.SearchParams;
import uk.ac.ebi.ep.data.search.model.SearchResults;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface EnzymeFinderService {

    SearchResults getEnzymes(SearchParams searchParameters);

    SearchResults getAssociatedProteinsByEcAndFulltextSearch(String ec, String searchTerm, int limit);

    SearchResults getAssociatedProteinsByOmimIdAndEc(String omimId, String ec, int limit);

    SearchResults getAssociatedProteinsByEc(String ec, int limit);

    SearchResults getAssociatedProteinsByTaxIdAndEc(String taxId, String ec, int limit);

    SearchResults getAssociatedProteinsByPathwayIdAndEc(String pathwayId, String ec, int limit);

    SearchResults getEnzymesByCompound(SearchParams searchParams) throws EnzymeFinderException;

    List<EnzymePortalDisease> findDiseases();

    void setSearchParams(SearchParams searchParams); //todo

    SearchResults computeEnzymeSummariesByOmimNumber(String omimNUmber);

    SearchResults computeEnzymeSummariesByEc(String ec);

    List<EnzymePortalPathways> findAllPathways();

    SearchResults computeEnzymeSummariesByPathwayName(String simplePathwayName);

    SearchResults computeEnzymeSummariesByPathwayId(String pathwayId);
}

package uk.ac.ebi.ep.core.search;

import java.util.List;
import uk.ac.ebi.ebeye.ResultOfGetNumberOfResults;
import uk.ac.ebi.ebeye.ResultOfGetReferencedEntriesSet;
import uk.ac.ebi.ebeye.ResultOfGetResultsIds;
import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.parameter.SearchParams;
import uk.ac.ebi.ep.search.result.EnzymeSearchResults;
import uk.ac.ebi.ep.search.result.EnzymeSummaryCollection;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public interface IEnzymeFinder extends IEnzyme {

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public EnzymeSearchResults getEnzymes(SearchParams searchInput)
            throws EnzymeFinderException;

    public List<ResultOfGetNumberOfResults> getNumberOfResults(
            SearchParams searchInput) throws EnzymeFinderException;

    public List<ResultOfGetResultsIds> getResultsIds(
            List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults)
            throws EnzymeFinderException;

    public List<ResultOfGetReferencedEntriesSet> getReferencedEntriesSet(
            List<ResultOfGetResultsIds> resultList) throws EnzymeFinderException;

    public List<String> getEnzymeUniprotIds(
            List<ResultOfGetResultsIds> resultsIdsList) throws EnzymeFinderException;

    public List<String> rankEnzymes(List<String> uniprotIds);

    public EnzymeSummaryCollection getUniprotEntries(List<String> uniprotIds);

}

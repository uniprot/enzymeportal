package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.ep.search.exception.EnzymeFinderException;
import uk.ac.ebi.ep.search.model.SearchParams;
import uk.ac.ebi.ep.search.model.SearchResults;

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

    public static final String DEFAULT_SPECIES = "HUMAN";
//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//
    public SearchResults getEnzymes(SearchParams searchInput)
            throws EnzymeFinderException;
/*
    public List<ResultOfGetNumberOfResults> getNumberOfResults(
            SearchParams searchInput) throws EnzymeFinderException;

    public List<ResultOfGetNumberOfResults> getNumberOfResults(
                                    List<ParamGetNumberOfResults> paramList)
                                                            throws MultiThreadingException;

    public List<ResultOfGetResultsIds> getResultsIds(
            List<ResultOfGetNumberOfResults> resultOfGetNumberOfResults)
            throws EnzymeFinderException;

    public List<ResultOfGetReferencedEntriesSet> getReferencedEntriesSet(
            List<ResultOfGetResultsIds> resultList) throws EnzymeFinderException;

    public List<String> getEnzymeUniprotIds(
            List<ResultOfGetResultsIds> resultsIdsList) throws EnzymeFinderException;

    public List<String> rankEnzymes(List<String> uniprotIds);

    public EnzymeSummaryCollection getUniprotEntries(List<String> uniprotIds);
*/
}

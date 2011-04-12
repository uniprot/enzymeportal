package uk.ac.ebi.ep.core.search;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ResultRanker {

//********************************* VARIABLES ********************************//
//Integer: rankingScore, Set<String>: list of ids (unique ids) grouped by rank
//protected SortedMap<Integer, Set<String>> resultsGroupedByRank;

//List of ids in ranked order
protected List<String> rankedResults;
protected List<String> resultsTobeRanked;


//******************************** CONSTRUCTORS ******************************//
    public ResultRanker(List<String> resultsToberanked) {
        this.resultsTobeRanked = resultsToberanked;
        this.rankedResults = new LinkedList<String>();
    }




//****************************** GETTER & SETTER *****************************//

    public List<String> getRankedResults() {
        return rankedResults;
    }

    public void setRankedResults(List<String> rankedResults) {
        this.rankedResults = rankedResults;
    }

    public List<String> getResultsTobeRanked() {
        return resultsTobeRanked;
    }

    public void setResultsTobeRanked(List<String> resultsTobeRanked) {
        this.resultsTobeRanked = resultsTobeRanked;
    }


//********************************** METHODS *********************************//

    
    public SortedMap<Integer, Set<String>> groupResultsByRank(
                                    SortedMap<String, Integer> rankingTable) {
        Set keySet = rankingTable.keySet();
        Iterator it = keySet.iterator();
        SortedMap<Integer, Set<String>> resultsGroupedByRank =
                new TreeMap<Integer, Set<String>>();
        while (it.hasNext()) {
            String id = (String) it.next();
            int score = rankingTable.get(id);
            Set<String> rankingGroup = resultsGroupedByRank.get(score);
            if (rankingGroup == null) {
                rankingGroup = new TreeSet<String>();
                rankingGroup.add(id);                
            } else {
                rankingGroup.add(id);                
            }
            resultsGroupedByRank.put(score, rankingGroup);
        }
        return resultsGroupedByRank;
    }

    /**
     * Add a new result set in the ranking table. 
     * @param resultToBeRanked
     * @return Ids associated with a ranking number.
     */
    public SortedMap<String,Integer> createRankingTable() {
        //List of ids (can be duplicated) and their ranking score
        SortedMap<String, Integer> rankingTable = new TreeMap<String,Integer>();
        Iterator it = this.resultsTobeRanked.iterator();
        while (it.hasNext()) {
            String id = (String)it.next();
            Integer rankingScore = (Integer)rankingTable.get(id);
            if (rankingScore!=null) {
                rankingTable.put(id, rankingScore++);
            }
            else {
                rankingTable.put(id, 1);
            }
        }
        return rankingTable;
    }

    public List<String> createRankedResults(
            SortedMap<Integer, Set<String>> resultsGroupedByRank) {
        List<String> results = new LinkedList<String>();
        Set keySet = resultsGroupedByRank.keySet();
        Iterator it = keySet.iterator();
        while(it.hasNext()) {
            Integer id = (Integer) it.next();
            TreeSet<String> ids = (TreeSet<String>)resultsGroupedByRank.get(id);
            results.addAll(ids);
        }
        return results;
    }

    public List<String> rankResults() {
        
        SortedMap<String, Integer> rankingTable = createRankingTable();

        SortedMap<Integer, Set<String>> resultsGroupedByRank
                =groupResultsByRank(rankingTable);
        this.rankedResults= createRankedResults(resultsGroupedByRank);
        return rankedResults;
    }


}

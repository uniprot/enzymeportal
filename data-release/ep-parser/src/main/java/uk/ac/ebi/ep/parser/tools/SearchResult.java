package uk.ac.ebi.ep.parser.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
//@ToString
public class SearchResult {

    private String searchType;
    private String searchCategory;
    private String searchTerm;
    private String date;
    private Long count;

    public SearchResult(String searchTerm, Long count) {
        this.searchTerm = searchTerm;
        this.count = count;
    }

    public SearchResult(String searchType, String searchCategory, String searchTerm, String date) {
        this.searchType = searchType;
        this.searchCategory = searchCategory;
        this.searchTerm = searchTerm;
        this.date = date;
    }
    
    

    @Override
    public String toString() {
        return  searchType + "  " + searchCategory + " " + searchTerm + " " + date;
    }

}

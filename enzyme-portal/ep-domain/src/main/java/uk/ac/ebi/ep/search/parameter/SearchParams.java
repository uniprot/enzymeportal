package uk.ac.ebi.ep.search.parameter;

import java.util.List;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class SearchParams {

    protected String keywords;
    protected int start;
    protected int size;
    protected String[] selectedSPecies;
    protected String[] selectedCompound;

    public SearchParams() {
    }

    
    public SearchParams(String keywords, int start, int size) {
        this.keywords = keywords;
        this.start = start;
        this.size = size;
    }

    public SearchParams(String keywords, int start, int size, String[] selectedSPecies, String[] selectedCompound) {
        this.keywords = keywords;
        this.start = start;
        this.size = size;
        this.selectedSPecies = selectedSPecies;
        this.selectedCompound = selectedCompound;
    }


    

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String[] getSelectedCompound() {
        return selectedCompound;
    }

    public void setSelectedCompound(String[] selectedCompound) {
        this.selectedCompound = selectedCompound;
    }

    public String[] getSelectedSPecies() {
        return selectedSPecies;
    }

    public void setSelectedSPecies(String[] selectedSPecies) {
        this.selectedSPecies = selectedSPecies;
    }


}

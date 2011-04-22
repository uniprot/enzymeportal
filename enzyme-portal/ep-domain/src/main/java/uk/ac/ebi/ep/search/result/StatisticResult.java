package uk.ac.ebi.ep.search.result;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class StatisticResult {

//********************************* VARIABLES ********************************//
protected String domain;
protected int totalFound;
protected int totalUniprotXrefs;
protected int totalEnzymeUniprotXrefs;
protected int totalUniqueEnzymeUniprotXrefs;

    public int getTotalEnzymeUniprotXrefs() {
        return totalEnzymeUniprotXrefs;
    }

    public void setTotalEnzymeUniprotXrefs(int totalEnzymeUniprotXrefs) {
        this.totalEnzymeUniprotXrefs = totalEnzymeUniprotXrefs;
    }

    public int getTotalFound() {
        return totalFound;
    }

    public void setTotalFound(int totalFound) {
        this.totalFound = totalFound;
    }

    public int getTotalUniprotXrefs() {
        return totalUniprotXrefs;
    }

    public void setTotalUniprotXrefs(int totalUniprotXrefs) {
        this.totalUniprotXrefs = totalUniprotXrefs;
    }

    public int getTotalUniqueEnzymeUniprotXrefs() {
        return totalUniqueEnzymeUniprotXrefs;
    }

    public void setTotalUniqueEnzymeUniprotXrefs(int totalUniqueEnzymeUniprotXrefs) {
        this.totalUniqueEnzymeUniprotXrefs = totalUniqueEnzymeUniprotXrefs;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    
}

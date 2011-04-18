package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.webservices.ebeye.ArrayOfString;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class ParamOfGetResultsIds {
//********************************* VARIABLES ********************************//

    protected String domain;
    protected String query;
    protected int start;
    protected int size;
    protected int totalResultFound;



//******************************** CONSTRUCTORS ******************************//
    public ParamOfGetResultsIds(String domain, String query, int start, int size) {
        this.domain = domain;
        this.query = query;
        this.start = start;
        this.size = size;
    }


//****************************** GETTER & SETTER *****************************//
    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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

    public int getTotalResultFound() {
        return totalResultFound;
    }

    public void setTotalResultFound(int totalResultFound) {
        this.totalResultFound = totalResultFound;
    }


    
//********************************** METHODS *********************************//

}

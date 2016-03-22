
package uk.ac.ebi.ep.xml.util;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum DatabaseName {
    UNIPROTKB("UNIPROTKB"),INTENZ("INTENZ");
    
    private DatabaseName(String name){
        this.dbName = name;
    }
    
    private final String dbName;

    public String getDbName() {
        return dbName;
    }
    
    
}

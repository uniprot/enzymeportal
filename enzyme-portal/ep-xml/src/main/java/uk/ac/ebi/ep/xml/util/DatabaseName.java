/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

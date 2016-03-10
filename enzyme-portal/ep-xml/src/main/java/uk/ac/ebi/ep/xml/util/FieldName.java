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
public enum FieldName {
    UNIPROT_NAME("uniprot_name"),PROTEIN_NAME("protein_name"),
    SCIENTIFIC_NAME("scientific_name"),SYNONYM("synonym"),STATUS("status"),SOURCE("source"),
    COMPOUND_NAME("compound_name"),DISEASE_NAME("disease_name");
    private FieldName(String name){
        this.name = name;
    }
    private final String name;

    public String getName() {
        return name;
    }
    
    
}

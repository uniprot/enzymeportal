/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web.utils;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public enum KeywordType {

    KEYWORD("KEYWORD"),
    DISEASE("DISEASE"),
    EC("EC"),
    TAXONOMY("TAXONOMY"),
    PATHWAYS("PATHWAYS");

    private final String keywordType;

    private KeywordType(String keywordType) {
        this.keywordType = keywordType;
    }

    public String getKeywordType() {
        return keywordType;
    }

}

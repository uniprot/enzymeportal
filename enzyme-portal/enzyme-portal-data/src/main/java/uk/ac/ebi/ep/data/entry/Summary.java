/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.entry;

/**
 *
 * @author joseph
 */
public class Summary {
    private String accession;
    private String commentText;

    public Summary(String accession, String commentText) {
        this.accession = accession;
        this.commentText = commentText;
    }

    public String getAccession() {
        return accession;
    }

    public void setAccession(String accession) {
        this.accession = accession;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
    
    
}

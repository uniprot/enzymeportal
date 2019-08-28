
package uk.ac.ebi.ep.model.dao;

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

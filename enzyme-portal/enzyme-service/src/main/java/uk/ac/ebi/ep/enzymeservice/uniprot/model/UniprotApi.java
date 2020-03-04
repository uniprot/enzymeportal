package uk.ac.ebi.ep.enzymeservice.uniprot.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)

/**
 *
 * @author joseph
 */
@Data
public class UniprotApi {

    @JsonProperty("accession")
    private String accession;
    @JsonProperty("id")
    private String id;

    @JsonProperty("comments")
    private List<Comment> comments;

    @JsonProperty("accession")
    public String getAccession() {
        return accession;
    }

    @JsonProperty("accession")
    public void setAccession(String accession) {
        this.accession = accession;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("comments")
    public List<Comment> getComments() {
        if (comments == null) {
            comments = new ArrayList<>();
        }
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}

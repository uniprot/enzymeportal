package uk.ac.ebi.ep.ebeye.autocomplete;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

/**
 *
 * @author joseph
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Suggestion implements Comparable<Suggestion>{
    @JsonProperty("suggestion")
    private String suggestedKeyword;

    private Suggestion(){}

    public Suggestion(String suggestedKeyword) {
        this.suggestedKeyword = suggestedKeyword;
    }

    public String getSuggestedKeyword() {
        return suggestedKeyword;
    }

    @Override
    public String toString() {
        return suggestedKeyword;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.suggestedKeyword);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Suggestion other = (Suggestion) obj;
        return Objects.equals(this.suggestedKeyword, other.suggestedKeyword);
    }

    @Override
    public int compareTo(Suggestion s) {
       return this.suggestedKeyword.compareToIgnoreCase(s.getSuggestedKeyword());
    }
}
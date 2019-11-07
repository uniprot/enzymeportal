
package uk.ac.ebi.ep.literatureservice.dto;

import java.util.EnumSet;
import java.util.Set;
import uk.ac.ebi.ep.literatureservice.model.Result;

/**
 *
 * @author joseph
 */
public class LabelledCitation implements Comparable<LabelledCitation> {

    private final Result citation;
    private final Set<CitationLabel> labels;

    public LabelledCitation(Result citation, CitationLabel label) {
        this.citation = citation;
        this.labels =  EnumSet.of(label);
    }

    public LabelledCitation(Result citation, Set<CitationLabel> labels) {
        this.citation = citation;
        this.labels = labels;
    }

    public Result getCitation() {
        return citation;
    }

    public Set<CitationLabel> getLabels() {
        return labels;
    }

    public void addLabel(CitationLabel label) {
        this.labels.add(label);
    }

    public void addLabels(Set<CitationLabel> labels) {
        this.labels.addAll(labels);
    }

    /**
     * Equals method based on bibliography identifier, namely PubMed ID.If
     * missing from both compared objects, the title is used.<br/>
     * This method does not take labels into account.
     *
     * @param o
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LabelledCitation)) {
            return false;
        }
        if (o == this) {
            return true;
        }
        LabelledCitation other = (LabelledCitation) o;
        String tcei = this.citation.getId();
        String ocei = other.citation.getId();

        if (tcei != null && ocei != null) {
            String tcds = this.citation.getSource();
            String ocds = other.citation.getSource();
            return tcei.equals(ocei) && tcds.equals(ocds);
        }
        if (tcei == null ^ ocei == null) {
            return false;
        }
        return this.citation.getTitle().equals(other.citation.getTitle());
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = hash * 17;
        if (citation.getSource() != null) {
            hash += citation.getSource().hashCode();
        }
        hash = hash * 17;
        if (citation.getId() != null) {
            hash += citation.getId().hashCode();
        }
        hash = hash * 17 + citation.getTitle().hashCode();
        return hash;
    }

    /**
     * By default, citations are sorted by date of creation (if not available,
     * year of publication).
     *
     * @param other
     */
    @Override
    public int compareTo(LabelledCitation other) {
        if (other == this) {
            return 0;
        }
        if (this.citation.getDateOfCreation() == null) {
            if (other.citation.getDateOfCreation() == null) {
                // Fall back to publication year:
                if (this.citation.getFirstPublicationDate() == null) {
                    if (other.citation.getFirstPublicationDate() == null) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (other.citation.getFirstPublicationDate() == null) {
                    return -1;
                }
                return Short.valueOf(this.citation.getFirstPublicationDate())
                        .compareTo(Short.valueOf(other.citation.getFirstPublicationDate()));
            } else {
                return 1;
            }
        } else if (other.citation.getDateOfCreation() == null) {
            return -1;
        }
        return -(this.citation.getDateOfCreation().compareTo(other.citation.getDateOfCreation()));
    }

}

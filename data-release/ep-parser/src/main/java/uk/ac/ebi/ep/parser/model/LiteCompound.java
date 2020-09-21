package uk.ac.ebi.ep.parser.model;

import java.util.Objects;

/**
 *
 * @author joseph
 */
public class LiteCompound {

    private String compoundId;

    private String compoundName;
    private String synonym;

    private String compoundSource;

    private String relationship;

    private String uniprotAccession;

    private String url;

    private String compoundRole;

    private String note;

    public String getCompoundId() {
        return compoundId;
    }

    public void setCompoundId(String compoundId) {
        this.compoundId = compoundId;
    }

    public String getCompoundName() {
        return compoundName;
    }

    public void setCompoundName(String compoundName) {
        this.compoundName = compoundName;
    }

    public String getCompoundSource() {
        return compoundSource;
    }

    public void setCompoundSource(String compoundSource) {
        this.compoundSource = compoundSource;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(String uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCompoundRole() {
        return compoundRole;
    }

    public void setCompoundRole(String compoundRole) {
        this.compoundRole = compoundRole;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSynonym() {
        return synonym;
    }

    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }

    @Override
    public String toString() {
        return compoundId + "\t" + compoundName + "\t" + synonym + "\t" + relationship + "\t" + uniprotAccession + "\t" + url + "\t" + compoundRole + "\t" + note;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.compoundId);
        hash = 79 * hash + Objects.hashCode(this.uniprotAccession);
        hash = 79 * hash + Objects.hashCode(this.compoundRole);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LiteCompound other = (LiteCompound) obj;
        if (!Objects.equals(this.compoundId, other.compoundId)) {
            return false;
        }
        if (!Objects.equals(this.uniprotAccession, other.uniprotAccession)) {
            return false;
        }
        return Objects.equals(this.compoundRole, other.compoundRole);
    }

}

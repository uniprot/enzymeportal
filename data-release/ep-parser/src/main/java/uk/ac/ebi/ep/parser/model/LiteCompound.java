package uk.ac.ebi.ep.parser.model;

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
        return "LiteCompound{" + "compoundId=" + compoundId + ", compoundName=" + compoundName + ", synonym=" + synonym + ", compoundSource=" + compoundSource + ", relationship=" + relationship + ", uniprotAccession=" + uniprotAccession + ", url=" + url + ", compoundRole=" + compoundRole + ", note=" + note + '}';
    }

}

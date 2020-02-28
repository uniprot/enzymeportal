package uk.ac.ebi.ep.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "WEB_STAT_XREF")
@Data
//@XmlRootElement
//@NamedQueries({
//    @NamedQuery(name = "WebStatXref.findAll", query = "SELECT w FROM WebStatXref w"),
//    @NamedQuery(name = "WebStatXref.findByInternalId", query = "SELECT w FROM WebStatXref w WHERE w.internalId = :internalId"),
//    @NamedQuery(name = "WebStatXref.findByUniprot", query = "SELECT w FROM WebStatXref w WHERE w.uniprot = :uniprot"),
//    @NamedQuery(name = "WebStatXref.findByIntenz", query = "SELECT w FROM WebStatXref w WHERE w.intenz = :intenz"),
//    @NamedQuery(name = "WebStatXref.findByReactome", query = "SELECT w FROM WebStatXref w WHERE w.reactome = :reactome"),
//    @NamedQuery(name = "WebStatXref.findByRhea", query = "SELECT w FROM WebStatXref w WHERE w.rhea = :rhea"),
//    @NamedQuery(name = "WebStatXref.findByKegg", query = "SELECT w FROM WebStatXref w WHERE w.kegg = :kegg"),
//    @NamedQuery(name = "WebStatXref.findByPdbe", query = "SELECT w FROM WebStatXref w WHERE w.pdbe = :pdbe"),
//    @NamedQuery(name = "WebStatXref.findByChembl", query = "SELECT w FROM WebStatXref w WHERE w.chembl = :chembl"),
//    @NamedQuery(name = "WebStatXref.findByChebi", query = "SELECT w FROM WebStatXref w WHERE w.chebi = :chebi"),
//    @NamedQuery(name = "WebStatXref.findByOmim", query = "SELECT w FROM WebStatXref w WHERE w.omim = :omim"),
//    @NamedQuery(name = "WebStatXref.findByMetabolights", query = "SELECT w FROM WebStatXref w WHERE w.metabolights = :metabolights"),
//    @NamedQuery(name = "WebStatXref.findByMcsa", query = "SELECT w FROM WebStatXref w WHERE w.mcsa = :mcsa"),
//    @NamedQuery(name = "WebStatXref.findByEuropepmc", query = "SELECT w FROM WebStatXref w WHERE w.europepmc = :europepmc"),
//    @NamedQuery(name = "WebStatXref.findByBrenda", query = "SELECT w FROM WebStatXref w WHERE w.brenda = :brenda"),
//    @NamedQuery(name = "WebStatXref.findByReleaseId", query = "SELECT w FROM WebStatXref w WHERE w.releaseId = :releaseId"),
//    @NamedQuery(name = "WebStatXref.findByReleaseDate", query = "SELECT w FROM WebStatXref w WHERE w.releaseDate = :releaseDate")})
public class WebStatXref implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "webStatXrefSeqGenerator", sequenceName = "seq_web_stat_xref")
    @GeneratedValue(generator = "webStatXrefSeqGenerator", strategy = GenerationType.SEQUENCE)
    private Long internalId;
    @Column(name = "UNIPROT")
    private long uniprot;
    @Column(name = "INTENZ")
    private long intenz;
    @Column(name = "REACTOME")
    private long reactome;
    @Column(name = "RHEA")
    private long rhea;
    @Column(name = "KEGG")
    private long kegg;
    @Column(name = "PDBE")
    private long pdbe;
    @Column(name = "CHEMBL")
    private long chembl;
    @Column(name = "CHEBI")
    private long chebi;
    @Column(name = "OMIM")
    private long omim;
    @Column(name = "METABOLIGHTS")
    private long metabolights;
    @Column(name = "MCSA")
    private long mcsa;
    @Column(name = "EUROPEPMC")
    private long europepmc;
    @Column(name = "BRENDA")
    private long brenda;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "RELEASE_ID")
    private String releaseId;
    @Column(name = "RELEASE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date releaseDate;

    public WebStatXref() {
    }

    public WebStatXref(Long internalId) {
        this.internalId = internalId;
    }

    public WebStatXref(Long internalId, String releaseId) {
        this.internalId = internalId;
        this.releaseId = releaseId;
    }

//    public Long getInternalId() {
//        return internalId;
//    }
//
//    public void setInternalId(Long internalId) {
//        this.internalId = internalId;
//    }
//
//    public BigInteger getUniprot() {
//        return uniprot;
//    }
//
//    public void setUniprot(BigInteger uniprot) {
//        this.uniprot = uniprot;
//    }
//
//    public BigInteger getIntenz() {
//        return intenz;
//    }
//
//    public void setIntenz(BigInteger intenz) {
//        this.intenz = intenz;
//    }
//
//    public BigInteger getReactome() {
//        return reactome;
//    }
//
//    public void setReactome(BigInteger reactome) {
//        this.reactome = reactome;
//    }
//
//    public BigInteger getRhea() {
//        return rhea;
//    }
//
//    public void setRhea(BigInteger rhea) {
//        this.rhea = rhea;
//    }
//
//    public BigInteger getKegg() {
//        return kegg;
//    }
//
//    public void setKegg(BigInteger kegg) {
//        this.kegg = kegg;
//    }
//
//    public BigInteger getPdbe() {
//        return pdbe;
//    }
//
//    public void setPdbe(BigInteger pdbe) {
//        this.pdbe = pdbe;
//    }
//
//    public BigInteger getChembl() {
//        return chembl;
//    }
//
//    public void setChembl(BigInteger chembl) {
//        this.chembl = chembl;
//    }
//
//    public BigInteger getChebi() {
//        return chebi;
//    }
//
//    public void setChebi(BigInteger chebi) {
//        this.chebi = chebi;
//    }
//
//    public BigInteger getOmim() {
//        return omim;
//    }
//
//    public void setOmim(BigInteger omim) {
//        this.omim = omim;
//    }
//
//    public BigInteger getMetabolights() {
//        return metabolights;
//    }
//
//    public void setMetabolights(BigInteger metabolights) {
//        this.metabolights = metabolights;
//    }
//
//    public BigInteger getMcsa() {
//        return mcsa;
//    }
//
//    public void setMcsa(BigInteger mcsa) {
//        this.mcsa = mcsa;
//    }
//
//    public BigInteger getEuropepmc() {
//        return europepmc;
//    }
//
//    public void setEuropepmc(BigInteger europepmc) {
//        this.europepmc = europepmc;
//    }
//
//    public Integer getBrenda() {
//        return brenda;
//    }
//
//    public void setBrenda(Integer brenda) {
//        this.brenda = brenda;
//    }
//
//    public String getReleaseId() {
//        return releaseId;
//    }
//
//    public void setReleaseId(String releaseId) {
//        this.releaseId = releaseId;
//    }
//
//    public Date getReleaseDate() {
//        return releaseDate;
//    }
//
//    public void setReleaseDate(Date releaseDate) {
//        this.releaseDate = releaseDate;
//    }
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (internalId != null ? internalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        if (!(object instanceof WebStatXref)) {
            return false;
        }
        WebStatXref other = (WebStatXref) object;
        return !((this.internalId == null && other.internalId != null) || (this.internalId != null && !this.internalId.equals(other.internalId)));
    }

//    @Override
//    public String toString() {
//        return "uk.ac.ebi.ep.model.WebStatXref[ internalId=" + internalId + " ]";
//    }
    @Override
    public String toString() {
        return "WebStatXref{" + "internalId=" + internalId + ", uniprot=" + uniprot + ", intenz=" + intenz + ", reactome=" + reactome + ", rhea=" + rhea + ", kegg=" + kegg + ", pdbe=" + pdbe + ", chembl=" + chembl + ", chebi=" + chebi + ", omim=" + omim + ", metabolights=" + metabolights + ", mcsa=" + mcsa + ", europepmc=" + europepmc + ", brenda=" + brenda + ", releaseId=" + releaseId + ", releaseDate=" + releaseDate + '}';
    }

}

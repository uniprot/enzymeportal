/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.mm.app;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "COMPOUNDS")
@NamedQueries({
    @NamedQuery(name = "Compounds.findAll", query = "SELECT c FROM Compounds c")})
public class Compounds implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @Column(name = "SOURCE")
    private String source;
    @Column(name = "MERGE_TYPE")
    private Character mergeType;
    @Basic(optional = false)
    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    @Basic(optional = false)
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "MODIFIED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedOn;
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;
    @Basic(optional = false)
    @Column(name = "CHEBI_ACCESSION")
    private String chebiAccession;
    @Column(name = "DEFINITION")
    private String definition;
    @Column(name = "ASCII_NAME")
    private String asciiName;
    @Column(name = "STAR")
    private BigInteger star;
    @OneToMany(mappedBy = "parentId")
    private Collection<Compounds> compoundsCollection;
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    @ManyToOne
    private Compounds parentId;
    

    public Compounds() {
    }
    
    

    public Compounds(Long id) {
        this.id = id;
    }

    public Compounds(String chebiAccession, String name) {
      
        this.chebiAccession = chebiAccession;
          this.name = name;
    }

    public Compounds(Long id, String name, String source, Date createdOn, String createdBy, String chebiAccession) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.createdOn = createdOn;
        this.createdBy = createdBy;
        this.chebiAccession = chebiAccession;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Character getMergeType() {
        return mergeType;
    }

    public void setMergeType(Character mergeType) {
        this.mergeType = mergeType;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getChebiAccession() {
        return chebiAccession;
    }

    public void setChebiAccession(String chebiAccession) {
        this.chebiAccession = chebiAccession;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getAsciiName() {
        return asciiName;
    }

    public void setAsciiName(String asciiName) {
        this.asciiName = asciiName;
    }

    public BigInteger getStar() {
        return star;
    }

    public void setStar(BigInteger star) {
        this.star = star;
    }

    public Collection<Compounds> getCompoundsCollection() {
        return compoundsCollection;
    }

    public void setCompoundsCollection(Collection<Compounds> compoundsCollection) {
        this.compoundsCollection = compoundsCollection;
    }

    public Compounds getParentId() {
        return parentId;
    }

    public void setParentId(Compounds parentId) {
        this.parentId = parentId;
    }



    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Compounds other = (Compounds) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.chebiAccession == null) ? (other.chebiAccession != null) : !this.chebiAccession.equals(other.chebiAccession)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + (this.chebiAccession != null ? this.chebiAccession.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Compounds{" + "name=" + name + '}';
    }


    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import uk.ac.ebi.ep.data.search.model.Compound;
import uk.ac.ebi.ep.data.search.model.Disease;
import uk.ac.ebi.ep.data.search.model.EnzymeAccession;
import uk.ac.ebi.ep.data.search.model.Species;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "RELATED_PROTEINS")
@XmlRootElement

@NamedEntityGraph(name = "RelatedProteins.graph", includeAllAttributes = true, attributeNodes = {
    @NamedAttributeNode("uniprotAccession"),
    @NamedAttributeNode(value = "uniprotAccession", subgraph = "enzymePortalDiseaseSet")
}
)

@NamedQueries({
    @NamedQuery(name = "RelatedProteins.findAll", query = "SELECT r FROM RelatedProteins r"),
    @NamedQuery(name = "RelatedProteins.findByRelProtInternalId", query = "SELECT r FROM RelatedProteins r WHERE r.relProtInternalId = :relProtInternalId"),
    @NamedQuery(name = "RelatedProteins.findByNamePrefix", query = "SELECT r FROM RelatedProteins r WHERE r.namePrefix = :namePrefix")})
public class RelatedProteins extends EnzymeAccession implements Serializable {
    
    @Column(name = "UNIPROT_NAME")
    private String uniprotName;
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @Column(name = "REL_PROT_INTERNAL_ID")
    @SequenceGenerator(allocationSize = 1, name = "seqGenerator", sequenceName = "SEQ_REL_PROT_ID")
    @GeneratedValue(generator = "seqGenerator", strategy = GenerationType.AUTO)
    private BigDecimal relProtInternalId;
    @Column(name = "NAME_PREFIX")
    private String namePrefix;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;
    
    public RelatedProteins() {
    }
    
    public RelatedProteins(BigDecimal relProtInternalId) {
        this.relProtInternalId = relProtInternalId;
    }
    
    public BigDecimal getRelProtInternalId() {
        return relProtInternalId;
    }
    
    public void setRelProtInternalId(BigDecimal relProtInternalId) {
        this.relProtInternalId = relProtInternalId;
    }
    
    public String getNamePrefix() {
        return namePrefix;
    }
    
    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }
    
    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }
    
    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (relProtInternalId != null ? relProtInternalId.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RelatedProteins)) {
            return false;
        }
        RelatedProteins other = (RelatedProteins) object;
        if ((this.relProtInternalId == null && other.relProtInternalId != null) || (this.relProtInternalId != null && !this.relProtInternalId.equals(other.relProtInternalId))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "uk.ac.ebi.ep.data.domain.RelatedProteins[ relProtInternalId=" + relProtInternalId + " ]";
    }
    
    @Override
    public List<String> getUniprotaccessions() {
        if (uniprotaccessions == null) {
            uniprotaccessions = new ArrayList<>();
        }
        
       // uniprotaccessions.add(uniprotAccession.getAccession());
        return this.uniprotaccessions;
    }
    
    @Override
    public List<String> getPdbeaccession() {
        
        return getPdbCodes(this.getUniprotAccession());
    }
    
    private List<String> getPdbCodes(UniprotEntry e) {
        List<String> pdbcodes = new ArrayList<>();
        e.getUniprotXrefSet().parallelStream().filter((xref) -> (xref.getSource().equalsIgnoreCase("PDB"))).forEach((xref) -> {
            pdbcodes.add(xref.getSourceId());
        });
        return pdbcodes;
    }
    
    @Override
    public Species getSpecies() {
        return uniprotAccession;
    }
    
    @Override
    public List<Compound> getCompounds() {
        
        return uniprotAccession.getEnzymePortalCompoundSet().stream().collect(Collectors.toList());
    }
    
    @Override
    public List<Disease> getDiseases() {
        
        return uniprotAccession.getEnzymePortalDiseaseSet().stream().collect(Collectors.toList());
    }
    
    public String getUniprotName() {
        return uniprotName;
    }
    
    public void setUniprotName(String uniprotName) {
        this.uniprotName = uniprotName;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author joseph
 */
@Entity
@Table(name = "ENZYME_PORTAL_SUMMARY")
@XmlRootElement


//@NamedEntityGraph(
//     name="summary.graph",
//     attributeNodes = {
//          //@NamedAttributeNode("uniprotAccession"),
//          @NamedAttributeNode(value = "uniprotAccession",subgraph = "enzymePortalDiseaseSet"),
//          @NamedAttributeNode(value = "uniprotAccession", subgraph = "enzymePortalCompoundSet"),
//          @NamedAttributeNode(value = "uniprotAccession", subgraph = "uniprotXrefSet"),
//          @NamedAttributeNode(value = "uniprotAccession", subgraph = "relatedProteinsId")
//     },
//     subgraphs ={
//       
//         @NamedSubgraph( name="uniprotAccession", attributeNodes = { @NamedAttributeNode("enzymePortalDiseaseSet") }),
//          @NamedSubgraph(name = "uniprotAccession",attributeNodes ={@NamedAttributeNode("enzymePortalCompoundSet")} ),
//          @NamedSubgraph(name = "uniprotAccession",attributeNodes ={@NamedAttributeNode("uniprotXrefSet")} ),
//          @NamedSubgraph(name = "uniprotAccession",attributeNodes ={@NamedAttributeNode("relatedProteinsId")} )
//          
//     }
//)



@NamedEntityGraph(name = "summary.graph", attributeNodes = {  
    @NamedAttributeNode("uniprotAccession")
})

@NamedQueries({
    @NamedQuery(name = "EnzymePortalSummary.findAll", query = "SELECT e FROM EnzymePortalSummary e"),
    @NamedQuery(name = "EnzymePortalSummary.findByEnzymeId", query = "SELECT e FROM EnzymePortalSummary e WHERE e.enzymeId = :enzymeId"),
    @NamedQuery(name = "EnzymePortalSummary.findByDbentryId", query = "SELECT e FROM EnzymePortalSummary e WHERE e.dbentryId = :dbentryId"),
    @NamedQuery(name = "EnzymePortalSummary.findByCommentType", query = "SELECT e FROM EnzymePortalSummary e WHERE e.commentType = :commentType")
    //@NamedQuery(name = "EnzymePortalSummary.findByCommentText", query = "SELECT e FROM EnzymePortalSummary e WHERE e.commentText = :commentText")
})
public class EnzymePortalSummary  implements Serializable {


    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "ENZYME_ID")
    private BigDecimal enzymeId;
    @Basic(optional = false)
    @Column(name = "DBENTRY_ID")
    private long dbentryId;
    @Column(name = "COMMENT_TYPE")
    private String commentType;
    @Column(name = "COMMENT_TEXT")
    private String commentText;

    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne(optional = false,fetch = FetchType.LAZY)
    private UniprotEntry uniprotAccession;

    public EnzymePortalSummary() {
    }

    public EnzymePortalSummary(BigDecimal enzymeId) {
        this.enzymeId = enzymeId;
    }

    public EnzymePortalSummary(BigDecimal enzymeId, long dbentryId) {
        this.enzymeId = enzymeId;
        this.dbentryId = dbentryId;
    }

    public BigDecimal getEnzymeId() {
        return enzymeId;
    }

    public void setEnzymeId(BigDecimal enzymeId) {
        this.enzymeId = enzymeId;
    }

    public long getDbentryId() {
        return dbentryId;
    }

    public void setDbentryId(long dbentryId) {
        this.dbentryId = dbentryId;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public UniprotEntry getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(UniprotEntry uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.uniprotAccession.getProteinName());
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
        final EnzymePortalSummary other = (EnzymePortalSummary) obj;
        if (!Objects.equals(this.uniprotAccession.getProteinName(), other.uniprotAccession.getProteinName())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EnzymePortalSummary{" + "enzymeId=" + enzymeId + ", dbentryId=" + dbentryId + ", commentType=" + commentType + ", commentText=" + commentText + '}';
    }





//
//    @Override
//    public List<String> getSynonym() {
//        if (synonym == null) {
//            synonym = new ArrayList<>();
//        }
//
//        String namesColumn = this.getUniprotAccession().getSynonymNames();
//
//        if (namesColumn != null && namesColumn.contains(";")) {
//            String[] syn = namesColumn.split(";");
//            for (String x : syn) {
//
//                synonym.addAll(parseNameSynonyms(x));
//            }
//        }
//
//        return this.synonym;
//        //return synonym;
//    }
//    
//        @Override
//    public List<String> getPdbeaccession() {
//
//        return getPdbCodes(this.getUniprotAccession());
//    }
//
//    private List<String> getPdbCodes(UniprotEntry e) {
//        List<String> pdbcodes = new ArrayList<>();
//        
////        e.getUniprotXrefSet().stream().filter((xref) -> (xref.getSource().equalsIgnoreCase("PDB"))).forEach((xref) -> {
////            pdbcodes.add(xref.getSourceId());
////        });
//
//        
//        
//        e.getUniprotXrefSet().stream().filter((x)->(x.getSource().equalsIgnoreCase("PDB"))).limit(2).collect(Collectors.toList()).stream().forEach((xref) -> {
//            pdbcodes.add(xref.getSourceId());
//        });
//        
//        
//        return pdbcodes;
//        
//
//    }
//
//    private List<String> parseNameSynonyms(String namesColumn) {
//        List<String> nameSynonyms = new ArrayList<>();
//        if (namesColumn != null) {
//            final int sepIndex = namesColumn.indexOf(" (");
//
//            //System.out.println("syn index "+ sepIndex);
//            if (sepIndex == -1) {
//                // no synonyms, just recommended name:
//
//                nameSynonyms.add(namesColumn);
//            } else {
//                // Recommended name:
//                nameSynonyms.add(namesColumn.substring(0, sepIndex));
//                // take out starting and ending parentheses
//                String[] synonyms = namesColumn.substring(sepIndex + 2, namesColumn.length() - 1).split("\\) \\(");
//                nameSynonyms.addAll(Arrays.asList(synonyms));
//            }
//            return nameSynonyms.stream().distinct().collect(Collectors.toList());
//        }
//        return nameSynonyms;
//    }
//
//    @Override
//    public String getName() {
//        return this.uniprotAccession.getProteinName();
//    }
//
//    @Override
//    public String getAccession() {
//        return this.uniprotAccession.getAccession();
//    }
//
//    @Override
//    public String getUniprotid() {
//        return this.uniprotAccession.getName();
//    }
//
//    @Override
//    public List<String> getEc() {
//        if (ec == null) {
//            ec = new ArrayList<>();
//        }
//
//        if (this.getCommentType().equalsIgnoreCase("EC_NUMBER")) {
//
//            ec.add(this.getCommentText());
//
//        }
//
//        return this.ec;
//    }
//
//    @Override
//    public String getFunction() {
//        if (this.getCommentType().equalsIgnoreCase("FUNCTION")) {
//
//            function = this.getCommentText();
//        }
//
//        return function;
//    }
//
//    @Override
//    public Species getSpecies() {
//        return uniprotAccession.getSpecies();
//    }
//
//    @Override
//    public List<Compound> getCompounds() {
//
//        return uniprotAccession.getEnzymePortalCompoundSet().stream().collect(Collectors.toList());
//    }
//
//    @Override
//    public List<Disease> getDiseases() {
//
//        return uniprotAccession.getEnzymePortalDiseaseSet().stream().distinct().collect(Collectors.toList());
//    }

//    public Date getLastUpdateTimestamp() {
//        return lastUpdateTimestamp;
//    }
//
//    public void setLastUpdateTimestamp(Date lastUpdateTimestamp) {
//        this.lastUpdateTimestamp = lastUpdateTimestamp;
//    }
//
//    public String getNamePrefix() {
//        return namePrefix;
//    }
//
//    public void setNamePrefix(String namePrefix) {
//        this.namePrefix = namePrefix;
//    }
    
    
}

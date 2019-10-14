
package uk.ac.ebi.ep.dataservice.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "ENTRY_TO_GENE_MAPPING")
@XmlRootElement

    @NamedQuery(name = "EntryToGeneMapping.findAll", query = "SELECT e FROM EntryToGeneMapping e")
    @NamedQuery(name = "EntryToGeneMapping.findByGeneInternalId", query = "SELECT e FROM EntryToGeneMapping e WHERE e.geneInternalId = :geneInternalId")
    @NamedQuery(name = "EntryToGeneMapping.findByGeneName", query = "SELECT e FROM EntryToGeneMapping e WHERE e.geneName = :geneName")
    @NamedQuery(name = "EntryToGeneMapping.findByUniprotGeneId", query = "SELECT e FROM EntryToGeneMapping e WHERE e.uniprotGeneId = :uniprotGeneId")
public class EntryToGeneMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "GENE_INTERNAL_ID")
    private BigDecimal geneInternalId;
    @Size(max = 60)
    @Column(name = "GENE_NAME")
    private String geneName;
    @Column(name = "UNIPROT_GENE_ID")
    private BigInteger uniprotGeneId;
    @JoinColumn(name = "UNIPROT_ACCESSION", referencedColumnName = "ACCESSION")
    @ManyToOne
    private UniprotEntry uniprotAccession;

    
}

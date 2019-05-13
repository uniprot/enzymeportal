package uk.ac.ebi.ep.xml.entity.protein;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import uk.ac.ebi.ep.xml.entity.Protein;
import uk.ac.ebi.ep.xml.transformer.ModelOrganisms;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Entity
@Table(name = "UNIPROT_ENTRY")
@XmlRootElement

public class UniprotEntry extends Protein implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final String PDB = "PDB";
    @JoinColumn(name = "PROTEIN_GROUP_ID", referencedColumnName = "PROTEIN_GROUP_ID")
    @ManyToOne
    private ProteinGroups proteinGroupId;
    @JoinColumn(name = "RELATED_PROTEINS_ID", referencedColumnName = "REL_PROT_INTERNAL_ID")
    @ManyToOne
    private RelatedProteins relatedProteinsId;

     @OneToMany(mappedBy = "uniprotAccession")
    //@OneToMany(mappedBy = "uniprotAccession", fetch = FetchType.LAZY)
   // @Fetch(FetchMode.JOIN)
    private Set<ProteinEcNumbers> enzymePortalEcNumbersSet ;

    public UniprotEntry() {
    }

    public String getUniprotId() {
        return name;
    }

    public List<String> getPdbeaccession() {

        return getPdbCodes(this);
    }

    private List<String> getPdbCodes(UniprotEntry e) {
        List<String> pdbcodes = new ArrayList<>();

        e.getUniprotXrefSet()
                .stream()
                .filter(x -> x.getSource().equalsIgnoreCase(PDB))
                .limit(1)
                .forEach(xref -> pdbcodes.add(xref.getSourceId()));

        return pdbcodes;

    }

    public ProteinGroups getProteinGroupId() {
        return proteinGroupId;
    }

    public void setProteinGroupId(ProteinGroups proteinGroupId) {
        this.proteinGroupId = proteinGroupId;
    }

    public RelatedProteins getRelatedProteinsId() {
        return relatedProteinsId;
    }

    public void setRelatedProteinsId(RelatedProteins relatedProteinsId) {
        this.relatedProteinsId = relatedProteinsId;
    }

    @XmlTransient
    public Set<ProteinEcNumbers> getEnzymePortalEcNumbersSet() {
        return enzymePortalEcNumbersSet;
    }

    public void setEnzymePortalEcNumbersSet(Set<ProteinEcNumbers> enzymePortalEcNumbersSet) {
        this.enzymePortalEcNumbersSet = enzymePortalEcNumbersSet;
    }

    public List<UniprotEntry> getRelatedspecies() {

        final Map<Integer, UniprotEntry> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);

        LinkedHashSet<UniprotEntry> relatedspecies = new LinkedHashSet<>();

        if (getRelatedProteinsId() != null) {

            getRelatedProteinsId().getUniprotEntrySet().stream().forEach((entry) -> {

                sortSpecies(entry, entry, priorityMapper, customKey, key);

                // relatedspecies.add(entry);
            });
        }

        priorityMapper.entrySet().stream().forEach(map -> {
            relatedspecies.add(map.getValue());
        });

        List<UniprotEntry> sortedSpecies = relatedspecies
                .stream()
                //.distinct()
                .sorted(Comparator.comparing(UniprotEntry::getExpEvidenceFlag)
                        .reversed())
                .collect(Collectors.toList());

        return sortedSpecies;

    }

    private void sortSpecies(UniprotEntry sp, UniprotEntry entry, Map<Integer, UniprotEntry> priorityMapper, AtomicInteger customKey, AtomicInteger key) {
        //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli, Rat,worm
        // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
        if (sp.getTaxId().equals(ModelOrganisms.HUMAN.getTaxId())) {

            priorityMapper.put(1, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE.getTaxId())) {

            priorityMapper.put(2, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.MOUSE_EAR_CRESS.getTaxId())) {

            priorityMapper.put(3, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.FRUIT_FLY.getTaxId())) {

            priorityMapper.put(4, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.ECOLI.getTaxId())) {

            priorityMapper.put(5, entry);
        } else if (sp.getTaxId().equals(ModelOrganisms.BAKER_YEAST.getTaxId())) {
            priorityMapper.put(6, entry);

        } else if (sp.getTaxId().equals(ModelOrganisms.RAT.getTaxId())) {
            priorityMapper.put(customKey.getAndIncrement(), entry);
        } else {
            priorityMapper.put(key.getAndIncrement(), entry);
        }
    }

//    @XmlTransient
//    public Set<EnzymePortalReaction> getEnzymePortalReactionSet() {
//        return enzymePortalReactionSet;
//    }
//
//    public void setEnzymePortalReactionSet(Set<EnzymePortalReaction> enzymePortalReactionSet) {
//        this.enzymePortalReactionSet = enzymePortalReactionSet;
//    }
//    
//        @XmlTransient
//    public Set<EnzymePortalReactant> getEnzymePortalReactantSet() {
//        return enzymePortalReactantSet;
//    }
//
//    public void setEnzymePortalReactantSet(Set<EnzymePortalReactant> enzymePortalReactantSet) {
//        this.enzymePortalReactantSet = enzymePortalReactantSet;
//    }
    }

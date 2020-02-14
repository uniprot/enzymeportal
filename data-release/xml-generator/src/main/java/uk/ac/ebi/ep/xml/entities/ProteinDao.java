package uk.ac.ebi.ep.xml.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author joseph
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProteinDao implements Protein {

    private long proteinXmlId;

    private long taxId;

    private int expEvidenceFlag;

    private int relatedProteinsId;

    private int primaryRelatedProteinsId;

    private String chebiCompoundId;

    private String chebiCompoundName;

    private String chebiCompoundRole;

    private String chebiSynonyms;

    //private static final long serialVersionUID = 1L;

    private String proteinGroupId;

    private String proteinName;

    private String accession;

    private String scientificName;

    private String commonName;

    private String synonymNames;

    private Short entryType;

    private String geneName;

    private String primaryAccession;

    private String omimNumber;

    private String diseaseName;

    private String compoundId;

    private String compoundName;

    private String compoundRole;

    private String compoundSource;

    private String reactantId;

    private String reactantName;

    private String reactantSource;

    private String ecNumber;

    private Short ecFamily;

    private String catalyticActivity;

    private String pathwayId;

    private String pathwayName;

    private String reactionId;

    private String reactionSource;

    private String familyGroupId;

    private String familyName;

    @Override
    public String getCommonName() {
        if (commonName == null) {
            commonName = scientificName;
        }
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }
}

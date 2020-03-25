package uk.ac.ebi.ep.dataservice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 *
 * @author joseph
 */
@Data
@ToString
@RequiredArgsConstructor
public class ProteinData implements ProteinView, Serializable {

    private String accession;

    private Long taxId;

    private String proteinName;

    private String scientificName;

    private String commonName;

    private Integer sequenceLength;

    private String function;

    private Short entryType;

    private BigInteger functionLength;

    private String synonymNames;

    private BigInteger expEvidenceFlag;
    private Long relatedProteinsId;
    private String proteinGroupId;

    public ProteinData(String accession, String proteinName, String scientificName, String commonName, String function, String synonymNames, Long taxId, Integer sequenceLength, Short entryType, BigInteger functionLength, BigInteger expEvidenceFlag, BigDecimal relatedProteinsId, String proteinGroupId) {

        this.accession = accession;
        this.proteinName = proteinName;
        this.scientificName = scientificName;
        this.commonName = commonName;
        this.function = function;
        this.synonymNames = synonymNames;
        this.taxId = taxId;
        this.sequenceLength = sequenceLength;
        this.entryType = entryType;
        this.functionLength = functionLength;
        this.expEvidenceFlag = expEvidenceFlag;

        this.relatedProteinsId = relatedProteinsId.longValue();
        this.proteinGroupId = proteinGroupId;
    }

    @Override
    public String getCommonName() {
        if (commonName == null) {
            commonName = scientificName;
        }

        return commonName;
    }

    @Override
    public Boolean getExpEvidence() {
        return getExpEvidenceFlag() == BigInteger.ONE;
    }

    @Override
    public Species getSpecies() {
        return new Species(getScientificName(), getCommonName(), getTaxId());
    }

    @Override
    public List<String> getSynonym() {
        Optional<String> synonymName = Optional.ofNullable(getSynonymNames());
        if (!StringUtils.isEmpty(getSynonymNames()) && synonymName.isPresent()) {
            return Stream.of(synonymName.get().split(";"))
                    .filter(otherName -> (!otherName.trim().equalsIgnoreCase(getProteinName().trim())))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}

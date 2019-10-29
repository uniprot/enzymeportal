package uk.ac.ebi.ep.dataservice.dto;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.util.StringUtils;

/**
 *
 * @author joseph
 */
public interface ProteinView {

    String getAccession();

    Long getTaxId();

    String getProteinName();

    String getScientificName();

    String getCommonName();

    Integer getSequenceLength();

    String getFunction();

    Short getEntryType();

    BigInteger getFunctionLength();

    String getSynonymNames();

    BigInteger getExpEvidenceFlag();

    default Boolean getExpEvidence() {
        return getExpEvidenceFlag() == BigInteger.ONE;
    }

    Long getRelatedProteinsId();

    default Species getSpecies() {
        return new Species(getScientificName(), getCommonName(), getTaxId());
    }

    default List<String> getSynonym() {
        Optional<String> synonymName = Optional.ofNullable(getSynonymNames());
        if (!StringUtils.isEmpty(getSynonymNames()) && synonymName.isPresent()) {
            return Stream.of(synonymName.get().split(";"))
                    .filter(otherName -> (!otherName.trim().equalsIgnoreCase(getProteinName().trim())))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();

    }

}

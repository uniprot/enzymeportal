package uk.ac.ebi.ep.restapi.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 *
 * @author joseph
 */
@Value
@Builder
public class EnzymeDisease {

    private String omimNumber;

    private String diseaseName;

    private String description;

    private String url;

    private List<String> diseaseEvidences;

}

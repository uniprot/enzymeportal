package uk.ac.ebi.ep.restapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

/**
 *
 * @author joseph
 */
@Value
@Builder
@Data
@ToString
public class PdbInfo {

    @Schema(description = "PDBe Accession", example = "1rkp")
    private String pdbAccession;
    @Schema(description = "Protein structure title", example = "Crystal structure of PDE5A1-IBMX")
    private String pdbName;
}

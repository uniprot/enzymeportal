package uk.ac.ebi.ep.ebeye.model.proteinGroup;

import java.util.List;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface ProteinView {

    String getProteinName();

    String getPrimaryAccession();

    String getPrimaryOrganism();

    String getEntryType();

    List<String> getGeneName();

    PdbImage getPrimaryImage();

    String getFunction();

    List<RelSpecies> getRelatedSpecies();

    List<String> getSynonym();

    List<String> getdiseases();

    List<String> getEc();

}

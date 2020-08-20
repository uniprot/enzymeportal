package uk.ac.ebi.ep.indexservice.model.protein;

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

    List<String> getCatalyticActivities();

    List<WithCofactor> getWithCofactor();

    List<WithTaxonomy> getWithTaxonomy();

    WithDisease getWithDisease();

    List<WithProteinFamily> getWithProteinFamily();

    List<WithPathway> getWithPathway();

    List<WithMetabolite> getWithMetabolite();

}

package uk.ac.ebi.ep.ebeye.model.proteinGroup;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public interface ProteinView {

    String getProteinName();

    String getPrimaryAccession();

    String getPrimaryOrganism();
    @Deprecated
    String getPrimarySpecie();
}

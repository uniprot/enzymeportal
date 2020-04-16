package uk.ac.ebi.ep.web.logging;

/**
 *
 * @author joseph
 */
public enum SeachCategory {

    FULLTEXT("Fulltext"), EC("EC"), DISEASES("Diseases"), TAXONOMY("Taxonomy"),
    PATHWAYS("Pathways"), FAMILIES("Families"), COFACTORS("cofactors"),
    METABOLITE("Metabolite"), CHEBI("CHEBI"), RHEA("RHEA"), UNIPROT_ACCESSION("UniprotAccession");

    private final String categoryName;

    private SeachCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

}

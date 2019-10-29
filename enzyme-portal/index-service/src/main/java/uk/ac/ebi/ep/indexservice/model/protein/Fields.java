package uk.ac.ebi.ep.indexservice.model.protein;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class Fields {

    @JsonProperty("id")
    private List<String> id;
    @JsonProperty("name")
    private List<String> name;
    @JsonProperty("common_name")
    private List<String> commonName;
    @JsonProperty("scientific_name")
    private List<String> scientificName;
    @JsonProperty("UNIPROTKB")
    private List<String> uniprotKB;
    @JsonProperty("primary_accession")
    private List<String> primaryAccession;
    @JsonProperty("primary_organism")
    private List<String> primaryOrganism;

    @JsonProperty("entry_type")
    private List<String> entryType;

    @JsonProperty("gene_name")
    private List<String> geneName;

    @JsonProperty("primary_image")
    private List<String> primaryImage;

    @JsonProperty("function")
    private List<String> function;

    @JsonProperty("related_species")
    private List<String> relatedSpecies;
    @JsonProperty("synonym")
    private List<String> synonym;
    @JsonProperty("disease_name")
    private List<String> diseaseName;

    @JsonProperty("ec")
    private List<String> ec;

    @JsonProperty("catalytic_activity")
    private List<String> catalyticActivities;

    @JsonProperty("with_cofactor")
    private List<String> withCofactor;

    @JsonProperty("with_taxonomy")
    private List<String> withTaxonomy;

    @JsonProperty("with_disease")
    private List<String> withDisease;

    @JsonProperty("with_protein_family")
    private List<String> withProteinFamily;

    @JsonProperty("with_pathway")
    private List<String> withPathway;

    @JsonProperty("with_metabolite")
    private List<String> withMetabolite;

    @JsonProperty("id")
    public List<String> getId() {
        if (id == null) {
            id = new ArrayList<>();
        }
        return id;
    }

    @JsonProperty("id")
    public void setId(List<String> id) {
        this.id = id;
    }

    @JsonProperty("name")
    public List<String> getName() {
        if (name == null) {
            name = new ArrayList<>();
        }
        return name;

    }

    @JsonProperty("name")
    public void setName(List<String> name) {
        this.name = name;
    }

    /**
     *
     * @return The commonName
     */
    @JsonProperty("common_name")
    public List<String> getCommonName() {
        if (commonName == null) {
            commonName = new ArrayList<>();
        }
        return commonName;
    }

    /**
     *
     * @param commonName The common_name
     */
    @JsonProperty("common_name")
    public void setCommonName(List<String> commonName) {
        this.commonName = commonName;
    }

    /**
     *
     * @return The scientificName
     */
    @JsonProperty("scientific_name")
    public List<String> getScientificName() {
        if (scientificName == null) {
            scientificName = new ArrayList<>();
        }
        return scientificName;
    }

    /**
     *
     * @param scientificName The scientific_name
     */
    @JsonProperty("scientific_name")
    public void setScientificName(List<String> scientificName) {
        this.scientificName = scientificName;
    }

    /**
     *
     * @return The uniprot accession
     */
    @JsonProperty("UNIPROTKB")
    public List<String> getUniprotKB() {
        if (uniprotKB == null) {
            uniprotKB = new ArrayList<>();
        }
        return uniprotKB;
    }

    /**
     *
     * @param uniprotKB The uniprot accession
     */
    @JsonProperty("UNIPROTKB")
    public void setUniprotKB(List<String> uniprotKB) {
        this.uniprotKB = uniprotKB;
    }

    @JsonProperty("primary_accession")
    public List<String> getPrimaryAccession() {
        if (primaryAccession == null) {
            primaryAccession = new ArrayList<>();
        }
        return primaryAccession;
    }

    @JsonProperty("primary_accession")
    public void setPrimaryAccession(List<String> primaryAccession) {
        this.primaryAccession = primaryAccession;
    }

    @JsonProperty("primary_organism")
    public List<String> getPrimaryOrganism() {
        if (primaryOrganism == null) {
            primaryOrganism = new ArrayList<>();
        }
        return primaryOrganism;
    }

    @JsonProperty("primary_organism")
    public void setPrimaryOrganism(List<String> primaryOrganism) {
        this.primaryOrganism = primaryOrganism;
    }

    @JsonProperty("entry_type")
    public List<String> getEntryType() {

        if (entryType == null) {
            entryType = Arrays.asList("7");//means some data were missing during indexing
        }
        return entryType;
    }

    @JsonProperty("entry_type")
    public void setEntryType(List<String> entryType) {
        this.entryType = entryType;
    }

    @JsonProperty("gene_name")
    public List<String> getGeneName() {
        if (geneName == null) {
            geneName = new ArrayList<>();
        }
        return geneName;
    }

    @JsonProperty("gene_name")
    public void setGeneName(List<String> geneName) {
        this.geneName = geneName;
    }

    @JsonProperty("primary_image")
    public List<String> getPrimaryImage() {
        if (primaryImage == null) {
            primaryImage = new ArrayList<>();
        }
        return primaryImage;
    }

    @JsonProperty("primary_image")
    public void setPrimaryImage(List<String> primaryImage) {
        this.primaryImage = primaryImage;
    }

    @JsonProperty("related_species")
    public List<String> getRelatedSpecies() {
        if (relatedSpecies == null) {
            relatedSpecies = new ArrayList<>();
        }
        return relatedSpecies;
    }

    @JsonProperty("related_species")
    public void setRelatedSpecies(List<String> relatedSpecies) {
        this.relatedSpecies = relatedSpecies;
    }

    @JsonProperty("function")
    public List<String> getFunction() {
        if (function == null) {
            function = new ArrayList<>();
        }
        return function;
    }

    @JsonProperty("function")
    public void setFunction(List<String> function) {
        this.function = function;
    }

    @JsonProperty("synonym")
    public List<String> getSynonym() {
        if (synonym == null) {
            synonym = new ArrayList<>();
        }
        return synonym;
    }

    @JsonProperty("synonym")
    public void setSynonym(List<String> synonym) {
        this.synonym = synonym;
    }

    @JsonProperty("disease_name")
    public List<String> getDiseaseName() {
        if (diseaseName == null) {
            diseaseName = new ArrayList<>();
        }
        return diseaseName;
    }

    @JsonProperty("disease_name")
    public void setDiseaseName(List<String> diseaseName) {
        this.diseaseName = diseaseName;
    }

    @JsonProperty("ec")
    public List<String> getEc() {
        if (ec == null) {
            ec = new ArrayList<>();
        }
        return ec;
    }

    @JsonProperty("ec")
    public void setEc(List<String> ec) {
        this.ec = ec;
    }

    @JsonProperty("catalytic_activity")
    public List<String> getCatalyticActivities() {
        if (catalyticActivities == null) {
            catalyticActivities = new ArrayList<>();
        }
        return catalyticActivities;
    }

    @JsonProperty("catalytic_activity")
    public void setCatalyticActivities(List<String> catalyticActivities) {
        this.catalyticActivities = catalyticActivities;
    }

    @JsonProperty("with_cofactor")
    public List<String> getWithCofactor() {
        if (withCofactor == null) {
            withCofactor = new ArrayList<>();
        }
        return withCofactor;
    }

    @JsonProperty("with_cofactor")
    public void setWithCofactor(List<String> withCofactor) {
        this.withCofactor = withCofactor;
    }

    @JsonProperty("with_taxonomy")
    public List<String> getWithTaxonomy() {
        if (withTaxonomy == null) {
            withTaxonomy = new ArrayList<>();
        }
        return withTaxonomy;
    }

    @JsonProperty("with_taxonomy")
    public void setWithTaxonomy(List<String> withTaxonomy) {
        this.withTaxonomy = withTaxonomy;
    }

    @JsonProperty("with_disease")
    public List<String> getWithDisease() {
        if (withDisease == null) {
            withDisease = new ArrayList<>();
        }
        return withDisease;
    }

    @JsonProperty("with_disease")
    public void setWithDisease(List<String> withDisease) {
        this.withDisease = withDisease;
    }

    @JsonProperty("with_protein_family")
    public List<String> getWithProteinFamily() {
        if (withProteinFamily == null) {
            withProteinFamily = new ArrayList<>();
        }
        return withProteinFamily;
    }

    @JsonProperty("with_protein_family")
    public void setWithProteinFamily(List<String> withProteinFamily) {
        this.withProteinFamily = withProteinFamily;
    }

    @JsonProperty("with_pathway")
    public List<String> getWithPathway() {
        if (withPathway == null) {
            withPathway = new ArrayList<>();
        }
        return withPathway;
    }

    @JsonProperty("with_pathway")
    public void setWithPathway(List<String> withPathway) {
        this.withPathway = withPathway;
    }

    @JsonProperty("with_metabolite")
    public List<String> getWithMetabolite() {
        if (withMetabolite == null) {
            withMetabolite = new ArrayList<>();
        }

        return withMetabolite;
    }

    @JsonProperty("with_metabolite")
    public void setWithMetabolite(List<String> withMetabolite) {
        this.withMetabolite = withMetabolite;
    }

}

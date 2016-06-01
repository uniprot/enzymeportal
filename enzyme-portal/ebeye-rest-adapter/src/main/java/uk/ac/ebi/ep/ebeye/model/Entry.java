/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Represents the result object from Enzyme Portal domain in EBI Search service
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "source",
    "fields"
})
public class Entry extends EnzymeView {

    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private Fields fields;

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    //private Set<Protein> proteins = null;

    //private PowerService powerService;
    public Entry() {

//        proteins = new HashSet<>();
//        EbeyeIndexUrl ebeyeIndexUrl = new EbeyeIndexUrl();
//        ebeyeIndexUrl.setChunkSize(10);
//        ebeyeIndexUrl.setMaxEbiSearchLimit(100);
//        ebeyeIndexUrl.setDefaultSearchIndexUrl("http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal");
//        // proteinService = new EbeyeProteinService(ebeyeIndexUrl, new RestTemplate());
//        powerService = new PowerService(ebeyeIndexUrl, new AsyncRestTemplate(), new RestTemplate());
//      
    }

    /**
     *
     * @return The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     *
     * @param id The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return The source
     */
    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source The source
     */
    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return The fields
     */
    @JsonProperty("fields")
    public Fields getFields() {
        return fields;
    }

    /**
     *
     * @param fields The fields
     */
    @JsonProperty("fields")
    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @Override
    public String getEc() {
        return id;
    }

    @Override
    public String getEnzymeName() {
        return fields.getName().stream().findFirst().orElse("");

    }

    @Override
    public String getEnzymeFamily() {
        return fields.getEnzymeFamily().stream().findFirst().orElse("");

    }

    @Override
    public List<String> getCatalyticActivities() {

        return fields.getDescription().stream().distinct().collect(Collectors.toList());

    }

//    @Override
//    public int getNumEnzymeHits() {
//        return fields.getProteinName().size();
//
//    }
    @Override
    public List<String> getSpecies() {
        Map<Integer, String> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(50);
        AtomicInteger customKey = new AtomicInteger(6);
        LinkedList<String> sortedSpecies = new LinkedList<>();

        List<String> species = fields.getCommonName();

        species.stream().forEach((name) -> {
            sortSpecies(name, priorityMapper, customKey, key);
        });

        priorityMapper.entrySet().stream().forEach(map -> {
            sortedSpecies.add(map.getValue());
        });
        //return fields.getCommonName();
        return sortedSpecies;

    }

    @Override
    public List<String> getProteins() {
        //System.out.println("calling protins name "+ id);
        return fields.getProteinName().stream().collect(Collectors.toList());

    }

//    @Override
//    public List<Protein> getProtein() {
//        System.out.println("calling protein .."+ id);
//       // List<Protein>  p = this.powerService.queryForUniqueProteins(id, 5);
//     
//        return super;
//
//    }
//    private List<Protein> buildProtein() {
//        List<String> accessions = fields.getUNIPROTKB().stream().collect(Collectors.toList());
//   
//
//        do {
//            if (proteins.size() == 5) {
//                break;
//            }
//            //for (String accession : accessions) {
//            String accession = "";
//            List<uk.ac.ebi.ep.ebeye.search.Entry> entries =null;// ps.getProteinView(id, accession).stream().distinct().collect(Collectors.toList());
//     
//            for (uk.ac.ebi.ep.ebeye.search.Entry entry : entries) {
//               String sc = entry.getScientificName().stream().findFirst().orElse("");
//                Protein protein = new Protein(entry.getUniprotAccession(), entry.getTitle(), sc);
//                proteins.add(protein);
//            }
//            //}
//        } while (proteins.size() <= 5);
//
//        return proteins.stream().collect(Collectors.toList());
//    }
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    private void sortSpecies(String specieName, Map<Integer, String> priorityMapper, AtomicInteger customKey, AtomicInteger key) {

        if (specieName.equalsIgnoreCase(ModelOrganisms.HUMAN.getCommonName())) {

            priorityMapper.put(1, specieName);
        } else if (specieName.equalsIgnoreCase(ModelOrganisms.MOUSE.getCommonName())) {

            priorityMapper.put(2, specieName);
        } else if (specieName.equalsIgnoreCase(ModelOrganisms.FRUIT_FLY.getCommonName())) {
            priorityMapper.put(3, specieName);

        } else if (specieName.equalsIgnoreCase(ModelOrganisms.RAT.getCommonName())) {
            priorityMapper.put(customKey.getAndIncrement(), specieName);

        } else {
            priorityMapper.put(key.getAndIncrement(), specieName);
        }
    }

    @Deprecated
    private void sortSpeciesDeprecated(String specieName, String entry, Map<Integer, String> priorityMapper, AtomicInteger customKey, AtomicInteger key) {
        //Human,Mouse, Mouse-ear cress, fruit fly, yeast, e.coli, Rat,worm
        // "Homo sapiens","Mus musculus","Rattus norvegicus", "Drosophila melanogaster","WORM","Saccharomyces cerevisiae","ECOLI"
        if (specieName.equalsIgnoreCase(ModelOrganisms.HUMAN.getCommonName())) {

            priorityMapper.put(1, entry);
        } else if (specieName.equalsIgnoreCase(ModelOrganisms.MOUSE.getCommonName())) {

            priorityMapper.put(2, entry);
        } //        else if (sp.getScientificname().equalsIgnoreCase(ModelOrganisms.MOUSE_EAR_CRESS.getScientificName())) {
        //
        //            priorityMapper.put(3, entry);
        //        } else if (sp.getScientificname().equalsIgnoreCase(ModelOrganisms.FRUIT_FLY.getScientificName())) {
        //
        //            priorityMapper.put(4, entry);
        //        } else if (sp.getScientificname().equalsIgnoreCase(ModelOrganisms.ECOLI.getScientificName())) {
        //
        //            priorityMapper.put(5, entry);
        //        } else if (sp.getScientificname().split("\\(")[0].trim().equalsIgnoreCase(ModelOrganisms.BAKER_YEAST.getScientificName())) {
        //            priorityMapper.put(6, entry);
        //
        //        } else if (sp.getScientificname().equalsIgnoreCase(ModelOrganisms.RAT.getScientificName())) {
        //            priorityMapper.put(customKey.getAndIncrement(), entry);
        //        } 
        else {
            priorityMapper.put(key.getAndIncrement(), entry);
        }
    }
}

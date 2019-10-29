package uk.ac.ebi.ep.indexservice.model.protein;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import lombok.ToString;
import org.springframework.util.StringUtils;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@ToString
public class ProteinGroupEntry implements ProteinView {

    private static final int REL_SP_LIMIT = 50;
    @JsonProperty("id")
    private String id;
    @JsonProperty("source")
    private String source;
    @JsonProperty("fields")
    private Fields fields;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    @JsonProperty("fields")
    public Fields getFields() {
        return fields;
    }

    @JsonProperty("fields")
    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public String getProteinGroupId() {
        return getId();
    }

    @Override
    public String getProteinName() {
        return getFields().getName().stream().findAny().orElse("");
    }

    @Override
    public String getPrimaryOrganism() {
        if (fields.getPrimaryOrganism().isEmpty()) {
            return getRelatedSpecies().stream().limit(1).findFirst()
                    .orElse(new RelSpecies()).getCommonName();
        }
        return fields.getPrimaryOrganism().stream().limit(1).findFirst().orElse("");

    }

    @Override
    public String getPrimaryAccession() {
        if (fields.getPrimaryAccession().isEmpty()) {
            return getRelatedSpecies().stream().limit(1).findFirst()
                    .orElse(new RelSpecies()).getAccession();
        }

        return fields.getPrimaryAccession().stream().limit(1).findFirst().orElse("");
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProteinGroupEntry other = (ProteinGroupEntry) obj;
        return Objects.equals(this.id, other.id);
    }

    @Override
    public String getEntryType() {
        return fields.getEntryType().stream().findAny().orElse("1");
    }

    @Override
    public List<String> getGeneName() {
        return fields.getGeneName();
    }

    @Override
    public PdbImage getPrimaryImage() {
        return fields.getPrimaryImage()
                .stream()
                .filter(Objects::nonNull)
                .map(m -> m.split("\\|"))
                .map(p -> new PdbImage(p[0], p[1], p[2]))
                .findAny()
                .orElse(new PdbImage());

    }

    @Override
    public String getFunction() {
        return fields.getFunction().stream().findAny().orElse("");
    }

    @Override
    public List<RelSpecies> getRelatedSpecies() {

        return buildRelSpecies();
    }

    private List<RelSpecies> buildRelSpecies() {
        // List<String> ph = Stream.of("O76074;Human;Homo sapiens | Q28156;Bovine;Bos taurus | Q8CG03;Mouse;Mus musculus | O54735;Rat;Rattus norvegicus | O77746;Dog;Canis lupus familiaris").collect(Collectors.toList());

        List<RelSpecies> species = fields.getRelatedSpecies()
                .stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(rel -> rel.replace("\\|", " \\|"))
                .map(s -> s.split("\\|"))
                .map(Arrays::asList)
                .flatMap(List::stream)
                .map(comma -> comma.split("\\;"))
                .map(specie -> new RelSpecies(specie[0], specie[1], specie[2], toInteger(specie[3]), specie[4]))
                .collect(Collectors.toList());

        return sortByModelOrganism(species);
    }

    private BigInteger toInteger(String data) {
        return new BigInteger(data.trim());
    }

    @Override
    public List<String> getSynonym() {
        return fields.getSynonym().stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<String> getdiseases() {
        return fields.getDiseaseName();
    }

    @Override
    public List<String> getEc() {
        return fields.getEc();
    }

    @Override
    public List<String> getCatalyticActivities() {
        return fields.getCatalyticActivities();
    }

    @Override
    public List<WithCofactor> getWithCofactor() {

        return fields.getWithCofactor()
                .stream()
                .filter(Objects::nonNull)
                .map(c -> c.split(";"))
                .map(p -> new WithCofactor(p[0], p[1], p[2], p[3]))
                .collect(Collectors.toList());
    }

    @Override
    public List<WithMetabolite> getWithMetabolite() {
        return fields.getWithMetabolite()
                .stream()
                .filter(Objects::nonNull)
                .map(c -> c.split(";"))
                .map(p -> new WithMetabolite(p[0], p[1], p[2], p[3]))
                .collect(Collectors.toList());
    }

    @Override
    public List<WithTaxonomy> getWithTaxonomy() {
        return fields.getWithTaxonomy()
                .stream()
                .filter(Objects::nonNull)
                .map(c -> c.split(";"))
                .map(p -> new WithTaxonomy(p[0], p[1], p[2], p[3]))
                .collect(Collectors.toList());
    }

    @Override
    public WithDisease getWithDisease() {
        return fields.getWithDisease()
                .stream()
                .filter(Objects::nonNull)
                .map(c -> c.split(";"))
                .filter(s -> !StringUtils.isEmpty(s))
                .map(p -> new WithDisease(p[0], StringUtils.isEmpty(p[1]) ? getPrimaryAccession() : p[1], StringUtils.isEmpty(p[1]) ? getPrimaryOrganism() : p[2], StringUtils.isEmpty(p[3]) ? getEntryType() : p[3]))
                //.sorted(Comparator.comparingInt(i -> -i.getEntryType()))//change when entrytype is indexed
                .findFirst().orElse(new WithDisease("", getPrimaryAccession(), getPrimaryOrganism(), getEntryType()));
    }

    @Override
    public List<WithProteinFamily> getWithProteinFamily() {
        return fields.getWithProteinFamily()
                .stream()
                .filter(Objects::nonNull)
                .map(c -> c.split(";"))
                .map(p -> new WithProteinFamily(p[0], p[1], p[2], p[3]))
                .collect(Collectors.toList());

    }

    @Override
    public List<WithPathway> getWithPathway() {
        return fields.getWithPathway()
                .stream()
                .filter(Objects::nonNull)
                .map(c -> c.split(";"))
                .map(p -> new WithPathway(p[0], p[1], p[2], p[3]))
                .collect(Collectors.toList());
    }

    private List<RelSpecies> sortByModelOrganism(List<RelSpecies> species) {
        Map<Integer, RelSpecies> priorityMapper = new TreeMap<>();
        AtomicInteger key = new AtomicInteger(REL_SP_LIMIT);
        AtomicInteger customKey = new AtomicInteger(7);
        LinkedList<RelSpecies> sortedSpecies = new LinkedList<>();

        species
                //.stream()
                .forEach(specie -> sortSpecies(specie, priorityMapper, customKey, key));

        priorityMapper.entrySet()
                .stream()
                .limit(REL_SP_LIMIT)
                .forEachOrdered(map -> sortedSpecies.add(map.getValue()));
        return sortedSpecies
                .stream()
                //.sorted(Comparator.comparing(RelSpecies::getExpEvidenceCode)
                // .reversed())
                //.sorted(Comparator.comparingInt(code -> code.getExpEvidenceCode()))
                //.sorted(Comparator.comparing(evidence -> evidence.getExpEvidenceCode()))
                .collect(Collectors.toList());

    }

    private void sortSpecies(RelSpecies specie, Map<Integer, RelSpecies> priorityMapper, AtomicInteger customKey, AtomicInteger key) {

        if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.HUMAN.getCommonName())) {

            priorityMapper.put(1, specie);
        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.MOUSE.getCommonName())) {

            priorityMapper.put(2, specie);
        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.MOUSE_EAR_CRESS.getCommonName())) {

            priorityMapper.put(3, specie);
        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.FRUIT_FLY.getCommonName())) {

            priorityMapper.put(4, specie);
        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.BAKER_YEAST.getCommonName())) {
            priorityMapper.put(5, specie);

        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.ECOLI.getCommonName())) {

            priorityMapper.put(6, specie);
        } else if (specie.getCommonName().equalsIgnoreCase(ModelOrganisms.RAT.getCommonName())) {
            priorityMapper.put(customKey.getAndIncrement(), specie);

        } else {
            priorityMapper.put(key.getAndIncrement(), specie);
        }
    }

}

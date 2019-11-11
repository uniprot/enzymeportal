package uk.ac.ebi.ep.parser.xmlparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.ac.ebi.ep.parser.model.Targets;

/**
 *
 * @author joseph
 */
@Service
@Slf4j
public class ChemblXmlParser {

    private static final String ACCESSION = "accession";
    private static final String COMPONENT_TYPE = "component_type";
    private final String targetXml;

    public ChemblXmlParser(String targetXml) {
        this.targetXml = targetXml;
    }

    private Database setupParser() throws JAXBException, FileNotFoundException {

        File file = new File(targetXml);
        if (!file.canRead()) {
            throw new FileNotFoundException(targetXml);
        }
        JAXBContext jaxbContext = JAXBContext.newInstance(Database.class);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Database database = (Database) jaxbUnmarshaller.unmarshal(file);

        return database;
    }

    public Set<Targets> parseChemblTargets() throws FileNotFoundException {

        Optional<Database> database = Optional.empty();
        try {
            database = Optional.ofNullable(setupParser());
        } catch (JAXBException ex) {
            log.error(ex.getMessage(), ex);
        }

        Set<Targets> targets = new HashSet<>();
        if (database.isPresent()) {

            Entries entries = database.get().getEntries();

            List< Entry> entry = entries.getEntry();

            entry.forEach(e -> processTargets(e, targets));
            return targets;
        }

        return targets;
    }

    private void processTargets(Entry e, Set<Targets> targets) {
        Targets target = new Targets();

        e.getCross_references()
                .stream()
                .forEach(cr -> cr.getRef()
                .stream()
                .forEach(ref -> target.getChemblId().add(ref.getDbKey())));

        e.getAdditional_fields()
                .stream()
                .forEach(af -> af.getField()
                .stream()
                .forEach(f -> processTargetFields(f, target)));

        targets.add(target);
    }

    private void processTargetFields(Field f, Targets target) {
        if (f.getName().equalsIgnoreCase(ACCESSION)) {

            target.setAccession(f.getValue());
        }
        if (f.getName().equalsIgnoreCase(COMPONENT_TYPE)) {

            target.setComponentType(f.getValue());
        }
    }

    @Deprecated
    public Map<String, List<String>> parseChemblTarget() throws FileNotFoundException {

        Optional<Database> database = Optional.empty();
        try {
            database = Optional.ofNullable(setupParser());
        } catch (JAXBException ex) {
            log.error(ex.getMessage(), ex);
        }

        Map<String, List<String>> accessionTargetMapping = new LinkedHashMap<>();

        List<String> targetIds = null;
        if (database.isPresent()) {

            Entries entries = database.get().getEntries();

            List< Entry> entry = entries.getEntry();

            for (Entry e : entry) {
                targetIds = new LinkedList<>();

                for (CrossReference cr : e.getCross_references()) {

                    for (Ref ref : cr.getRef()) {

                        targetIds.add(ref.getDbKey());
                    }
                }

                for (AdditionalFields ad : e.getAdditional_fields()) {

                    for (Field f : ad.getField()) {

                        if ("accession".equalsIgnoreCase(f.getName())) {

                            accessionTargetMapping.put(f.getValue(), targetIds);
                        }
                    }

                }

            }
            return accessionTargetMapping;
        }

        return accessionTargetMapping;
    }
}

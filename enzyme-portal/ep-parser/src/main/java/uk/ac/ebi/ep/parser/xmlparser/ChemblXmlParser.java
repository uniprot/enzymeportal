/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.xmlparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

/**
 *
 * @author joseph
 */
@Service
public class ChemblXmlParser {

    private final Logger LOGGER = org.apache.log4j.Logger.getLogger(ChemblXmlParser.class);
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

    public Map<String, List<String>> parseChemblTarget() throws FileNotFoundException {

        Optional<Database> database = Optional.empty();
        try {
            database = Optional.ofNullable(setupParser());
        } catch (JAXBException ex) {
            LOGGER.error(ex.getMessage(), ex);
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

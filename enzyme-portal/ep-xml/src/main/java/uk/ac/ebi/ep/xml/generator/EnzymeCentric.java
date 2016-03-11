/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.data.domain.IntenzEnzymes;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import uk.ac.ebi.ep.xml.model.AdditionalFields;
import uk.ac.ebi.ep.xml.model.CrossReferences;
import uk.ac.ebi.ep.xml.model.Database;
import uk.ac.ebi.ep.xml.model.Entries;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.StreamUtils;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class EnzymeCentric extends XmlGenerator {

    @Autowired
    private String enzymeCentricXmlDir;

    public EnzymeCentric(EnzymePortalService enzymePortalService, EnzymePortalXmlService xmlService) {
        super(enzymePortalService, xmlService);
    }

    @Override
    public void validateXML() {

        if (ebeyeXSDs == null || enzymeCentricXmlDir == null) {
            try {
                String msg = "Xsd files or XML directory cannot be Null. Please ensure that ep-xml-config.properties is in the classpath.";
                throw new FileNotFoundException(msg);
            } catch (FileNotFoundException ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            String[] xsdFiles = ebeyeXSDs.split(",");
            EnzymePortalXmlValidator.validateXml(enzymeCentricXmlDir, xsdFiles);
        }
    }

    @Override
    public void generateXmL() throws JAXBException {

        generateXmL(enzymeCentricXmlDir);
    }

    @Override
    public void generateXmL(String xmlFileLocation) throws JAXBException {
        List<Entry> entryList = new LinkedList<>();
        Entries entries = new Entries();
        List<IntenzEnzymes> enzymes = enzymePortalXmlService.findAllIntenzEnzymes().stream().sorted().collect(Collectors.toList());
        int entryCount = enzymes.size();
        logger.info("Number of Intenz enzymes ready to be processed : " + entryCount);

        Database database = buildDatabaseInfo(entryCount);

        enzymes.forEach((enzyme) -> {
            AdditionalFields additionalFields = new AdditionalFields();

            Set<Field> fields = new LinkedHashSet<>();
            Set<Ref> refs = new HashSet<>();
            Entry entry = new Entry();

            entry.setId(enzyme.getEcNumber());
            entry.setName(enzyme.getEnzymeName());
            entry.setDescription(enzyme.getCatalyticActivity());

            Iterable<UniprotEntry> iterableEntry = enzymePortalService.findSwissprotEnzymesByEcNumber(enzyme.getEcNumber());

            StreamUtils.stream(iterableEntry.iterator()).parallel().forEach((uniprotEntry) -> {
                addUniprotIdFields(uniprotEntry, fields);
                addProteinNameFields(uniprotEntry, fields);

                addScientificNameFields(uniprotEntry, fields);

                addSynonymFields(uniprotEntry, fields);
                addSource(enzyme, refs);
                addAccessionXrefs(uniprotEntry, refs);

                addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);

                addDiseaseFields(uniprotEntry, fields);

                CrossReferences cr = new CrossReferences();
                cr.setRef(refs);

                additionalFields.setField(fields);

                entry.setAdditionalFields(additionalFields);
                entry.setCrossReferences(cr);

            });

            entryList.add(entry);

        });

        entries.setEntry(entryList);
        database.setEntries(entries);

        // create JAXB context and instantiate marshaller
        JAXBContext context = JAXBContext.newInstance(Database.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        Path path = Paths.get(xmlFileLocation);
        try {
            Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            // Write to File
            m.marshal(database, writer);
            //m.marshal(database, new File(enzymeCentricXmlDir));
            m.marshal(database, System.out);
            logger.info("Done writing XML to this Dir :" + xmlFileLocation);
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    private void addSource(IntenzEnzymes enzyme, Set<Ref> refs) {
        if (!StringUtils.isEmpty(enzyme.getEcNumber())) {
            Ref xref = new Ref(enzyme.getEcNumber(), DatabaseName.INTENZ.getDbName());
            refs.add(xref);

        }
    }

}

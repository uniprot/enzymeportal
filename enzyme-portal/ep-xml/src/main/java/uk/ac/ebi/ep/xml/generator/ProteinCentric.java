/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import javax.xml.bind.JAXBException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.service.EnzymePortalService;
import uk.ac.ebi.ep.data.service.EnzymePortalXmlService;
import static uk.ac.ebi.ep.xml.generator.XmlGenerator.logger;
import uk.ac.ebi.ep.xml.model.AdditionalFields;
import uk.ac.ebi.ep.xml.model.CrossReferences;
import uk.ac.ebi.ep.xml.model.Database;
import uk.ac.ebi.ep.xml.model.Entries;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;
import uk.ac.ebi.ep.xml.validator.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinCentric extends XmlGenerator {

    @Autowired
    private String proteinCentricXmlDir;

//    @Autowired
//    private ItemReader<UniprotEntry> databaseItemReader;
//    @Autowired
//    private ItemReader<UniprotEntry> databaseItemReaderPageable;
    public ProteinCentric(EnzymePortalService enzymePortalService, EnzymePortalXmlService xmlService) {
        super(enzymePortalService, xmlService);
    }

    private void paginate(int page, List<UniprotEntry> enzymes) {

        Page<UniprotEntry> pages = enzymePortalService.findPageableUniprotEntries(new PageRequest(page, 10, Sort.Direction.ASC, "entryType"));
        Iterator<UniprotEntry> it = pages.iterator();
        if (it.hasNext()) {
            UniprotEntry entry = it.next();
            enzymes.add(entry);
        }

    }

    @Override
    public void generateXmL() throws JAXBException {

        generateXmL(proteinCentricXmlDir);
    }

    @Override
    public void generateXmL(String xmlFileLocation) throws JAXBException {

        //*******************************TODO*******
//        Pageable pageable = new PageRequest(5, 10, Sort.Direction.ASC, "entryType");
//        Page<UniprotEntry> page = enzymePortalService.findPageableUniprotEntries(pageable);
//
//        List<UniprotEntry> enzymes = new ArrayList<>();
//        int total = Long.valueOf(page.getTotalElements()).intValue();
//        IntStream.rangeClosed(0, total).parallel()
//                .forEach(i -> paginate(i, enzymes));
//
////           IntStream.rangeClosed(0, 4).parallel()
////               .forEach(i -> enzymePortalService.findPageableUniprotEntries(pageable));    
////       
////  
//        enzymes.stream().forEach((e) -> {
//            // System.out.println("ACC "+ e.getAccession() + " Name "+ e.getProteinName());
//        });
//**************************** 
        ForkJoinPool commonPool = ForkJoinPool.commonPool();
        //Returns the targeted parallelism level of the common pool.
        int numCores = commonPool.getParallelism();

        System.out.println("ForkJoinPool :: " + numCores);
        long startTime = System.nanoTime();
        logger.warn("About to query uniprot for all entries ..... with machine cores @ " + numCores);

        List<UniprotEntry> uniprotEntries = enzymePortalService.findUniprotEntries();
        //List<UniprotEntry> uniprotEntries = enzymePortalService.findSwissprotEnzymesByEc("6.1.2.1")
        //.stream().collect(Collectors.toList());

        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);

        int entryCount = uniprotEntries.size();
        logger.warn("Time taken to retrieve " + entryCount + " entries from database is : " + elapsedtime);

        logger.info("Number of entries found " + entryCount);

        Database database = buildDatabaseInfo(entryCount);

        List<Entry> entryList = new CopyOnWriteArrayList<>();
        Entries entries = new Entries();
        //uniprotEntries.stream().forEach((uniprotEntry) -> {
        uniprotEntries.parallelStream().forEach((uniprotEntry) -> {
            AdditionalFields additionalFields = new AdditionalFields();
            CopyOnWriteArraySet<Field> fields = new CopyOnWriteArraySet<>();
            CopyOnWriteArraySet<Ref> refs = new CopyOnWriteArraySet<>();

            Entry entry = new Entry();
            entry.setAcc(uniprotEntry.getAccession());
            entry.setId(uniprotEntry.getUniprotid());
            entry.setName(uniprotEntry.getProteinName());
            entry.setDescription("");
            entryList.add(entry);

            addStatus(uniprotEntry, fields);
            addScientificNameFields(uniprotEntry, fields);

            addSynonymFields(uniprotEntry, fields);
            addAccessionXrefs(uniprotEntry, refs);

            addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);

            addDiseaseFields(uniprotEntry, fields);
            addEcXrefs(uniprotEntry, refs);

            CrossReferences cr = new CrossReferences();
            cr.setRef(refs);

            additionalFields.setField(fields);

            entry.setAdditionalFields(additionalFields);
            entry.setCrossReferences(cr);
        });

        entries.setEntry(entryList);
        database.setEntries(entries);

        //write xml
        writeXml(database, xmlFileLocation);
    }

    @Override
    public void validateXML() {
        if (ebeyeXSDs == null || proteinCentricXmlDir == null) {
            try {
                String msg = "Xsd files or XML directory cannot be Null. Please ensure that ep-xml-config.properties is in the classpath.";
                throw new FileNotFoundException(msg);
            } catch (FileNotFoundException ex) {
                logger.error(ex.getMessage(), ex);
            }
        } else {
            String[] xsdFiles = ebeyeXSDs.split(",");
            EnzymePortalXmlValidator.validateXml(proteinCentricXmlDir, xsdFiles);
        }
    }

}

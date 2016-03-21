package uk.ac.ebi.ep.xml.generator.protein;

import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.xml.generator.XmlTransformer;
import uk.ac.ebi.ep.xml.model.*;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.batch.item.ItemProcessor;

/**
 * Converts a {@link UniprotEntry} into an {@link Entry} so that it can then be persisted into some form of storage.
 *
 * @author Ricardo Antunes
 */
public class UniProtEntryToEntryConverter extends XmlTransformer implements ItemProcessor<UniprotEntry, Entry> {
    @Override public Entry process(UniprotEntry uniprotEntry) throws Exception {
        AdditionalFields additionalFields = new AdditionalFields();
        Set<Field> fields = new CopyOnWriteArraySet<>();
        Set<Ref> refs = new CopyOnWriteArraySet<>();

        Entry entry = new Entry();
        entry.setAcc(uniprotEntry.getAccession());
        entry.setId(uniprotEntry.getUniprotid());
        entry.setName(uniprotEntry.getProteinName());
        entry.setDescription("");

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

        return entry;
    }
}
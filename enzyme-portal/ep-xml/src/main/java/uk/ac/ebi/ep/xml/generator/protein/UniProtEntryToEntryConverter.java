package uk.ac.ebi.ep.xml.generator.protein;

import java.util.HashSet;
import java.util.Set;
import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.generator.XmlTransformer;
import uk.ac.ebi.ep.xml.model.AdditionalFields;
import uk.ac.ebi.ep.xml.model.CrossReferences;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;

/**
 * Converts a {@link UniprotEntry} into an {@link Entry} so that it can then be
 * persisted into some form of storage.
 *
 * @author Ricardo Antunes
 */
public class UniProtEntryToEntryConverter extends XmlTransformer implements ItemProcessor<UniprotEntry, Entry> {

    public UniProtEntryToEntryConverter(XmlConfigParams xmlConfigParams) {
        super(xmlConfigParams);
    }

    @Override
    public Entry process(UniprotEntry uniprotEntry) throws Exception {
        AdditionalFields additionalFields = new AdditionalFields();
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();

        Entry entry = new Entry();
        entry.setAcc(uniprotEntry.getAccession());
        entry.setId(uniprotEntry.getUniprotid());
        entry.setName(uniprotEntry.getProteinName());
        entry.setDescription(uniprotEntry.getProteinName());

        addStatus(uniprotEntry, fields);
        addScientificNameFields(uniprotEntry, fields);

        addSynonymFields(uniprotEntry, fields);
        addAccessionXrefs(uniprotEntry, refs);

        addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);

        addDiseaseFields(uniprotEntry, fields);
        addEcXrefs(uniprotEntry, refs);
        addPathwaysXrefs(uniprotEntry, refs);
        addTaxonomyXrefs(uniprotEntry, refs);

        CrossReferences cr = new CrossReferences();
        cr.setRef(refs);

        additionalFields.setField(fields);

        entry.setAdditionalFields(additionalFields);
        entry.setCrossReferences(cr);

        return entry;
    }
}

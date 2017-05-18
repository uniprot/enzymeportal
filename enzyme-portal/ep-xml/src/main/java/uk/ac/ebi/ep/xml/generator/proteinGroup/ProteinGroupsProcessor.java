package uk.ac.ebi.ep.xml.generator.proteinGroup;

import java.util.HashSet;
import java.util.Set;
import uk.ac.ebi.ep.data.domain.ProteinGroups;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.xml.config.XmlConfigParams;
import uk.ac.ebi.ep.xml.model.AdditionalFields;
import uk.ac.ebi.ep.xml.model.CrossReferences;
import uk.ac.ebi.ep.xml.model.Entry;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinGroupsProcessor extends XmlProcessor<ProteinGroups, Entry> {

    public ProteinGroupsProcessor(XmlConfigParams xmlConfigParams) {
        super(xmlConfigParams);
    }

    @Override
    public Entry process(ProteinGroups proteinGroups) throws Exception {
        AdditionalFields additionalFields = new AdditionalFields();
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();

        Entry entry = new Entry();

        entry.setId(proteinGroups.getProteinGroupId());
        entry.setName(proteinGroups.getProteinName());
        entry.setDescription(proteinGroups.getProteinName());

        addPrimaryProteinField(proteinGroups, fields);
        proteinGroups.getUniprotEntryList()
                .stream()
                .parallel()
                .forEach(uniprotEntry -> addFieldsAndXRefs(uniprotEntry, fields, refs));

        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        CrossReferences cr = new CrossReferences();
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;
    }

    private void addFieldsAndXRefs(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {

        addUniprotIdFields(uniprotEntry, fields);

        addScientificNameFields(uniprotEntry, fields);
        addCommonNameFields(uniprotEntry, fields);
        addGeneNameFields(uniprotEntry, fields);

        addSynonymFields(uniprotEntry, fields);
        addAccessionXrefs(uniprotEntry, refs);

        addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);

        addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
        addEcXrefs(uniprotEntry, refs);
        addPathwaysXrefs(uniprotEntry, refs);
        addTaxonomyXrefs(uniprotEntry, refs);
    }
}

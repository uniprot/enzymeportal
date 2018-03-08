package uk.ac.ebi.ep.xml.transformer;

import java.util.HashSet;
import java.util.Set;
import org.springframework.batch.item.ItemProcessor;
import uk.ac.ebi.ep.xml.config.XmlFileProperties;
import uk.ac.ebi.ep.xml.entity.EnzymePortalUniqueEc;
import uk.ac.ebi.ep.xml.entity.UniprotEntry;
import uk.ac.ebi.ep.xml.schema.AdditionalFields;
import uk.ac.ebi.ep.xml.schema.CrossReferences;
import uk.ac.ebi.ep.xml.schema.Entry;
import uk.ac.ebi.ep.xml.schema.Field;
import uk.ac.ebi.ep.xml.schema.Ref;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
public class UniqueEcProcessor extends DataTransformer implements ItemProcessor<EnzymePortalUniqueEc, Entry> {

    public UniqueEcProcessor(XmlFileProperties xmlFileProperties) {
        super(xmlFileProperties);
    }

    @Override
    public Entry process(EnzymePortalUniqueEc enzyme) throws Exception {
        Set<Field> fields = new HashSet<>();
        Set<Ref> refs = new HashSet<>();
        Entry entry = new Entry();
        entry.setId(enzyme.getEcNumber());
        entry.setName(enzyme.getEnzymeName());
        entry.setDescription(enzyme.getCatalyticActivity());

        addEnzymeFamilyField(enzyme.getEcNumber(), fields);

        addCofactorsField(enzyme.getCofactor(), fields);

        enzyme.getEnzymePortalEcNumbersSet()
                .stream()
                .forEach(ec -> processUniprotEntry(ec.getUniprotAccession(), fields, refs));

        addAltNamesField(enzyme, fields);
        addEcSource(enzyme.getEcNumber(), refs);

        AdditionalFields additionalFields = new AdditionalFields();
        additionalFields.setField(fields);
        entry.setAdditionalFields(additionalFields);

        CrossReferences cr = new CrossReferences();
        cr.setRef(refs);
        entry.setCrossReferences(cr);

        return entry;

    }

    private void processUniprotEntry(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {
        addUniprotIdFields(uniprotEntry, fields);
        addProteinNameFields(uniprotEntry, fields);

        addScientificNameFields(uniprotEntry, fields);
        addCommonNameFields(uniprotEntry, fields);
        addGeneNameFields(uniprotEntry, fields);

        addSynonymFields(uniprotEntry, fields);
        //addSource(enzyme, refs);
        addAccessionXrefs(uniprotEntry, refs);
        addTaxonomyXrefs(uniprotEntry, refs);

        addCompoundFieldsAndXrefs(uniprotEntry, fields, refs);
        addDiseaseFieldsAndXrefs(uniprotEntry, fields, refs);
        addPathwaysXrefs(uniprotEntry, refs);
    }

    private void addAltNamesField(EnzymePortalUniqueEc enzyme, Set<Field> fields) {

        if (!enzyme.getIntenzAltNamesSet().isEmpty()) {

            enzyme.getIntenzAltNamesSet()
                    .stream()
                    .map(altName -> new Field(FieldName.INTENZ_ALT_NAMES.getName(), altName.getAltName()))
                    .forEach(field -> fields.add(field));

        }
    }

}

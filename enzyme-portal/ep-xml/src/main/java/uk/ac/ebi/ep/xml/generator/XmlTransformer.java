/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.xml.generator;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import static uk.ac.ebi.ep.xml.generator.XmlTransformer.enzymePortal;
import uk.ac.ebi.ep.xml.model.Database;
import uk.ac.ebi.ep.xml.model.Field;
import uk.ac.ebi.ep.xml.model.Ref;
import uk.ac.ebi.ep.xml.util.DatabaseName;
import uk.ac.ebi.ep.xml.util.FieldName;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class XmlTransformer {

    @Autowired
    protected String releaseNumber;
    protected static final String REVIEWED = "reviewed";
    protected static final String UNREVIEWED = "unreviewed";

    protected static final String enzymePortal = "Enzyme Portal";
    protected static final String enzymePortalDescription = "The Enzyme Portal integrates publicly available information about enzymes, such as small-molecule chemistry, biochemical pathways and drug compounds.";

    protected Database buildDatabaseInfo(int entryCount) {
        Database database = new Database();
        database.setName(enzymePortal);
        database.setDescription(enzymePortalDescription);
        database.setRelease(releaseNumber);
        LocalDate date = LocalDate.now();
        database.setRelease_date(date);
        database.setEntry_count(entryCount);
        return database;
    }

    protected void addUniprotIdFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotEntry.getUniprotid())) {
            Field field = new Field();
            field.setField(FieldName.UNIPROT_NAME.getName());
            field.setValue(uniprotEntry.getUniprotid());
            fields.add(field);
        }
    }

    protected void addStatus(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (uniprotEntry.getEntryType() != null) {
            int entryType = uniprotEntry.getEntryType().intValue();
            Field field = new Field();
            field.setField(FieldName.STATUS.getName());
            if (entryType == 0) {
                field.setValue(REVIEWED);
                fields.add(field);
            }
            if (entryType == 1) {
                field.setValue(UNREVIEWED);
                fields.add(field);
            }

        }
    }

    protected void addProteinNameFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotEntry.getProteinName())) {
            Field field = new Field();
            field.setField(FieldName.PROTEIN_NAME.getName());
            field.setValue(uniprotEntry.getProteinName());
            fields.add(field);

        }
    }

    protected void addScientificNameFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!StringUtils.isEmpty(uniprotEntry.getScientificName())) {
            Field field = new Field();
            field.setField(FieldName.SCIENTIFIC_NAME.getName());
            field.setValue(uniprotEntry.getScientificName());
            fields.add(field);

        }
    }

    private void computeSynonymsAndBuildFields(Optional<String> synonymName, String proteinName, Set<Field> fields) {
        if (synonymName.isPresent()) {
            Stream.of(synonymName.get().split(";")).distinct()
                    .filter(otherName -> (!otherName.trim().equalsIgnoreCase(proteinName.trim())))
                    .map((syn) -> {
                        Field field = new Field();
                        field.setField(FieldName.SYNONYM.getName());
                        field.setValue(syn);
                        return field;
                    }).forEach((field) -> {
                        fields.add(field);
                    });
        }
    }

    protected void addSynonymFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (uniprotEntry.getSynonymNames() != null && uniprotEntry.getProteinName() != null) {

            Optional<String> synonymName = Optional.ofNullable(uniprotEntry.getSynonymNames());
            computeSynonymsAndBuildFields(synonymName, uniprotEntry.getProteinName(), fields);

        }
    }

    protected void addAccessionXrefs(UniprotEntry uniprotEntry, Set<Ref> refs) {
        if (!StringUtils.isEmpty(uniprotEntry.getAccession())) {
            Ref xref = new Ref(uniprotEntry.getAccession(), DatabaseName.UNIPROTKB.getDbName());
            refs.add(xref);

        }
    }

    protected void addEcXrefs(UniprotEntry uniprotEntry, Set<Ref> refs) {

        if (!uniprotEntry.getEnzymePortalEcNumbersSet().isEmpty()) {
            uniprotEntry.getEnzymePortalEcNumbersSet()
                    .stream()
                    .map(ecNumbers -> new Ref(ecNumbers.getEcNumber(), DatabaseName.INTENZ.getDbName()))
                    .forEach(xref -> {
                        refs.add(xref);
                    });
        }
    }

    protected void addCompoundFieldsAndXrefs(UniprotEntry uniprotEntry, Set<Field> fields, Set<Ref> refs) {
        if (!uniprotEntry.getEnzymePortalCompoundSet().isEmpty()) {

            uniprotEntry.getEnzymePortalCompoundSet().stream().map(compound -> {
                Field field = new Field();
                field.setField(FieldName.COMPOUND_NAME.getName());
                field.setValue(compound.getCompoundName());
                fields.add(field);
                Ref xref = new Ref(compound.getCompoundId(), compound.getCompoundSource());
                return xref;
            }).forEach(xref -> {
                refs.add(xref);
            });
        }
    }

    protected void addDiseaseFields(UniprotEntry uniprotEntry, Set<Field> fields) {
        if (!uniprotEntry.getEnzymePortalDiseaseSet().isEmpty()) {
            uniprotEntry.getEnzymePortalDiseaseSet().stream().map(disease -> {
                Field field = new Field();
                field.setField(FieldName.DISEASE_NAME.getName());
                field.setValue(disease.getDiseaseName());
                return field;
            }).forEach(field -> {
                fields.add(field);
            });
        }
    }

}

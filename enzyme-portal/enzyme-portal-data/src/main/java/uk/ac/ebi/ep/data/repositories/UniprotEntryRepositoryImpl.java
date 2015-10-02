/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.Projections;
import com.mysema.query.types.expr.StringExpression;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.entry.Protein;
import uk.ac.ebi.ep.data.search.model.EnzymeSummary;
import uk.ac.ebi.ep.data.search.model.Species;
import uk.ac.ebi.ep.data.search.model.Taxonomy;

/**
 *
 * @author joseph
 */
public class UniprotEntryRepositoryImpl implements UniprotEntryRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private static final QUniprotEntry $ = QUniprotEntry.uniprotEntry;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> namePrefixes) {
        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        query.setHint("javax.persistence.query.timeout", 450);

        //StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        StringExpression idPrefix = $.relatedProteinsId.namePrefix;
        BooleanBuilder builder = new BooleanBuilder();
        namePrefixes.stream().forEach(prefix -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);

        return query.distinct().list($);
    }

    @Deprecated
    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {

        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "enzymePortalEcNumbersSet");
        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach(accession -> {

            builder.or($.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);
        return query.distinct().list($).stream().distinct().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymeByNamePrefix(String namePrefix) {

        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "enzymePortalEcNumbersSet");
        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);

        //StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        StringExpression idPrefix = $.relatedProteinsId.namePrefix;
        BooleanBuilder builder = new BooleanBuilder();

        builder.or(idPrefix.equalsIgnoreCase(namePrefix));

        query.from($).where(builder);
        return query.list($).stream().distinct().collect(Collectors.toList());

    }

    @Override
    public List<UniprotEntry> findEnzymesByAccession(String accession) {
        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "enzymePortalEcNumbersSet");
        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        List<UniprotEntry> enzymes = query.from($).where($.accession.equalsIgnoreCase(accession)).list($);
        return enzymes;

    }

    @Transactional(readOnly = true)
    @Override
    public List<Taxonomy> getCountForOrganisms(List<Long> taxids) {

        JPAQuery query = new JPAQuery(entityManager);
        // List<Tuple> tuple =    query.from($).groupBy($.taxId,$.scientificName,$.commonName).orderBy($.taxId.count().desc()).limit(11).list($.taxId,$.scientificName,$.commonName,$.taxId.count());

        List<Taxonomy> result = query.from($).where($.taxId.in(taxids)).distinct().groupBy($.taxId, $.scientificName, $.commonName).
                list(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName, $.taxId.count()));
        
//                List<Taxonomy> result = query.from($).where($.taxId.in(taxids)).distinct().groupBy($.taxId, $.scientificName, $.commonName).
//                list(Projections.constructor(Taxonomy.class, $.taxId, $.scientificName, $.commonName));
// 
//        String nativeQuery = "select distinct uniprotEntry.tax_Id, uniprotEntry.scientific_Name, uniprotEntry.common_Name, count(uniprotEntry.tax_Id) as numEnzymes from UNIPROT_ENTRY uniprotEntry where uniprotEntry.tax_Id in (:TAX_ID) group by uniprotEntry.tax_Id, uniprotEntry.scientific_Name, uniprotEntry.common_Name";
//        Query query = entityManager.createNativeQuery(nativeQuery, "browseTaxonomy");
//        List<Taxonomy> result = query.setParameter("TAX_ID", taxids).getResultList();

        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAccessionsByTaxId(Long taxId) {
        JPAQuery query = new JPAQuery(entityManager);
        List<String> result = query.from($).where($.taxId.eq(taxId)).distinct().list($.accession);
        return result;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymesByTaxId(Long taxId) {
        JPAQuery query = new JPAQuery(entityManager);
        List<UniprotEntry> result = query.from($).where($.taxId.eq(taxId)).distinct().list($);
        return result;

    }

    @Transactional(readOnly = true)
    @Override
    public Page<EnzymeSummary> findEnzymesByAccessions(List<String> accessions, Pageable pageable) {
        JPAQuery query = new JPAQuery(entityManager);
        //CollQueryFactory.
        List<EnzymeSummary> result = query.from($).where($.accession.in(accessions))
                .list(Projections.constructor(EnzymeSummary.class, $));

        return new PageImpl<>(result, pageable, result.size());

    }

    @Override
    @Transactional(readOnly = true)
    public List<Species> findSpeciesByTaxId(Long taxId) {
        JPAQuery query = new JPAQuery(entityManager);
        List<Species> result = query.from($).where($.taxId.eq(taxId)).distinct()
                .list(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId));

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Species> findSpeciesByScientificName(String sName) {
        JPAQuery query = new JPAQuery(entityManager);
        List<Species> result = query.from($).where($.scientificName.equalsIgnoreCase(sName)).distinct()
                .list(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId));

        return result;
    }

    @Override
    public List<UniprotEntry> findEnzymesByMeshId(String meshId) {
        JPAQuery query = new JPAQuery(entityManager);
        List<UniprotEntry> result = query.from($).where($.enzymePortalDiseaseSet.any().omimNumber.trim().equalsIgnoreCase(meshId)).list($);
        return result;
    }

    @Override
    public List<UniprotEntry> findEnzymesByPathwayId(String pathwayId) {
        JPAQuery query = new JPAQuery(entityManager);
        List<UniprotEntry> result = query.from($).where($.enzymePortalPathwaysSet.any().pathwayId.trim().equalsIgnoreCase(pathwayId)).list($);
        return result;
    }

    @Override
    public List<String> findEnzymesByCompound(String compoundId) {
        JPAQuery query = new JPAQuery(entityManager);
        return query.from($).where($.enzymePortalCompoundSet.any().compoundId.trim().equalsIgnoreCase(compoundId)).list($.accession);
    }

    @Override
    public List<UniprotEntry> findSummariesByAcc(List<String> accessions) {

        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        JPAQuery query = new JPAQuery(entityManager);
        
        query.setHint("javax.persistence.loadgraph", eGraph);

        List<UniprotEntry> result = query.from($).where($.accession.in(accessions)).distinct().list($);

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Protein> findProteinByEc(String ec) {
        JPAQuery query = new JPAQuery(entityManager);
        List<Protein> result = query.from($).where($.enzymePortalEcNumbersSet.any().ecNumber.equalsIgnoreCase(ec))
                .list(Projections.constructor(Protein.class, $.accession, $.proteinName));

        return result.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public List<Species> findSpeciesByEcNumber(String ecNumber) {
             JPAQuery query = new JPAQuery(entityManager);
        List<Species> result = query.from($).where($.enzymePortalEcNumbersSet.any().ecNumber.eq(ecNumber)).distinct()
                .list(Projections.constructor(Species.class, $.scientificName, $.commonName, $.taxId));

        return result;
    }

}

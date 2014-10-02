/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.StringExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QUniprotEntry;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

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

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymesByNamePrefixes(List<String> name_prefixes) {
        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");
        //EntityGraph eGraph =entityManager.createEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "relatedProteinsSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);

        StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();
        name_prefixes.stream().forEach((prefix) -> {

            builder.or(idPrefix.equalsIgnoreCase(prefix));

        });
        query.from($).where(builder);
        return query.list($).parallelStream().distinct().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymesByAccessions(List<String> accessions) {

        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "relatedProteinsSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.stream().forEach((accession) -> {

            builder.or($.accession.equalsIgnoreCase(accession));

        });
        query.from($).where(builder);
        return query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public UniprotEntry findByAccession(String accession) {

        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "relatedProteinsSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        UniprotEntry e = query.from($).where($.accession.equalsIgnoreCase(accession)).singleResult($);
        return e;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UniprotEntry> findEnzymeByNamePrefixAndProteinName(String namePrefix, String proteinName) {

        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "relatedProteinsSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);

        StringExpression idPrefix = $.name.substring(0, $.name.indexOf("_"));
        BooleanBuilder builder = new BooleanBuilder();

        builder.or(idPrefix.equalsIgnoreCase(namePrefix));

        query.from($).where(builder.and($.proteinName.equalsIgnoreCase(proteinName)));
        return query.distinct().list($).parallelStream().distinct().collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    @Override
    public List<String> filterEnzymesInAccessions(List<String> accessions) {

        JPAQuery query = new JPAQuery(entityManager);

        BooleanBuilder builder = new BooleanBuilder();
        accessions.parallelStream().forEach((accession) -> {

            builder.or($.accession.equalsIgnoreCase(accession));

        });
        List<UniprotEntry> enzymes = query.from($).where(builder).list($).stream().distinct().collect(Collectors.toList());
        List<String> accessionList = new ArrayList<>();
        enzymes.stream().forEach((entry) -> {
            accessionList.add(entry.getAccession());
        });
        return accessionList;

    }

    @Transactional(readOnly = true)
    @Override
    public List<String> filterEnzymesInNameprefixes(List<String> prefixes) {

        JPAQuery query = new JPAQuery(entityManager);

        BooleanBuilder builder = new BooleanBuilder();
        prefixes.parallelStream().forEach((prefix) -> {

            builder.or($.name.substring(0, $.name.indexOf("_")).equalsIgnoreCase(prefix));

        });
        List<UniprotEntry> enzymes = query.from($).where(builder).list($).stream().distinct().collect(Collectors.toList());
        List<String> prefixList = new ArrayList<>();
        enzymes.parallelStream().forEach((entry) -> {
            prefixList.add(entry.getName().substring(0, entry.getName().indexOf("_")));
        });
        return prefixList;

    }

    @Override
    public List<UniprotEntry> findEnzymeByAccessionsAndProteinName(List<String> accessions, String proteinName) {
        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "relatedProteinsSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);
        query.setHint("javax.persistence.fetchgraph", eGraph);

        BooleanBuilder builder = new BooleanBuilder();

        accessions.stream().forEach((accession) -> {

            builder.or($.accession.equalsIgnoreCase(accession));

        });

        query.from($).where(builder.and($.proteinName.equalsIgnoreCase(proteinName)));
        return query.distinct().list($).stream().distinct().collect(Collectors.toList());

    }

    @Override
    public List<UniprotEntry> findEnzymesByAccession(String accession) {
        EntityGraph eGraph = entityManager.getEntityGraph("UniprotEntryEntityGraph");

        eGraph.addAttributeNodes("enzymePortalPathwaysSet", "enzymePortalReactionSet",
                "enzymePortalSummarySet", "enzymePortalDiseaseSet", "enzymePortalCompoundSet",
                "uniprotXrefSet", "relatedProteinsSet", "enzymePortalEcNumbersSet");

        JPAQuery query = new JPAQuery(entityManager);

        query.setHint("javax.persistence.fetchgraph", eGraph);
        List<UniprotEntry> enzymes = query.from($).where($.accession.equalsIgnoreCase(accession)).list($);
        return enzymes;

    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.repositories;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.types.expr.BooleanExpression;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.QUniprotXref;
import uk.ac.ebi.ep.data.domain.UniprotXref;

/**
 *
 * @author joseph
 */
public class UniprotXrefRepositoryImpl implements UniprotXrefRepositoryCustom {
    
    @PersistenceContext
    private EntityManager entityManager;
    private static final QUniprotXref $ = QUniprotXref.uniprotXref;
    private static final String PDB = "PDB";
    
    @Override
    public List<UniprotXref> findPDBcodesByAccession(String accession) {
        JPAQuery query = new JPAQuery(entityManager);
        
        BooleanExpression isAccession = $.accession.accession.equalsIgnoreCase(accession);
        List<UniprotXref> pdb = query.from($).where($.source.equalsIgnoreCase(PDB).and($.sourceName.isNotNull().or($.sourceName.isNotEmpty())).and(isAccession)).list($);
          //List<UniprotXref> pdb = query.from($).where($.source.equalsIgnoreCase(PDB).and(isAccession)).list($);
        
        return pdb.stream().distinct().collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<String> findPdbCodesWithNoNames() {
        JPAQuery query = new JPAQuery(entityManager);
        List<String> result = query.from($).where($.source.equalsIgnoreCase(PDB)).list($.sourceId);
        return result;
    }
    
    @Transactional(readOnly = true)
    @Override
    public UniprotXref findPdbById(String pdbId) {
        JPAQuery query = new JPAQuery(entityManager);
        
        UniprotXref pdb = query.from($).where($.source.equalsIgnoreCase(PDB).and($.sourceId.equalsIgnoreCase(pdbId))).singleResult($);
        
        return pdb;
    }

    @Override
    public List<UniprotXref> findPDBcodes() {
             JPAQuery query = new JPAQuery(entityManager);
        List<UniprotXref> result = query.from($).where($.source.equalsIgnoreCase(PDB)).list($);
        return result;
    }
    
}

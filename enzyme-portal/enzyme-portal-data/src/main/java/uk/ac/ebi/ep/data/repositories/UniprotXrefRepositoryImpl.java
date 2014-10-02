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

    @Override
    public List<UniprotXref> findPDBcodesByAccession(String accession) {
        JPAQuery query = new JPAQuery(entityManager);

        System.out.println("UniprotXrefRepositoryImpl line 31 == " + $.accession.accession);
        System.out.println("UniprotXrefRepositoryImpl accession " + accession);

        BooleanExpression isAccession = $.accession.accession.equalsIgnoreCase(accession);
        List<UniprotXref> pdb = query.from($).where($.source.equalsIgnoreCase("PDB").and(isAccession)).list($);

        return pdb.stream().distinct().collect(Collectors.toList());
    }

}

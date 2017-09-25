///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package uk.ac.ebi.ep.data.repositories;
//
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.querydsl.QueryDslPredicateExecutor;
//import org.springframework.transaction.annotation.Transactional;
//import uk.ac.ebi.ep.data.domain.EnzymeCatalyticActivity;
//
///**
// *
// * @author joseph
// */
//public interface EnzymeCatalyticActivityRepository extends JpaRepository<EnzymeCatalyticActivity, Long>, QueryDslPredicateExecutor<EnzymeCatalyticActivity> {
//
//    @Transactional(readOnly = true)
//    @Query(value = "SELECT * FROM ENZYME_CATALYTIC_ACTIVITY WHERE ROWNUM <= 200", nativeQuery = true)
//    List<EnzymeCatalyticActivity> findAllEnzymeCatalyticActivity();
//}

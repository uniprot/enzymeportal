/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.repositories;

import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @author joseph
 */
@NoRepositoryBean
public interface DiseaseRepositoryCustom {
    
    void dropDiseaseDatabase();
}

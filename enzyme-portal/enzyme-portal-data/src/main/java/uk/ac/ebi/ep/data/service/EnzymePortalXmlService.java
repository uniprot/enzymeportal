/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.data.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.data.domain.EnzymePortalNames;
import uk.ac.ebi.ep.data.domain.EnzymeXmlStore;
import uk.ac.ebi.ep.data.repositories.EnzymePortalNamesRepository;
import uk.ac.ebi.ep.data.repositories.EnzymeXmlStoreRepository;

/**
 *
 * @author joseph
 */
@Transactional
@Service
public class EnzymePortalXmlService {

    @Autowired
    private EnzymePortalNamesRepository enzymePortalNamesRepository;
    @Autowired
    private EnzymeXmlStoreRepository enzymeXmlStoreRepository;

    @Transactional(readOnly = true)
    public List<EnzymePortalNames> findEnzymeNames() {

        return enzymePortalNamesRepository.findEnzymeNames();
    }

    @Transactional(readOnly = true)
    public List<EnzymeXmlStore> findEnzymeXmlEnteries() {

        return enzymeXmlStoreRepository.findEnzymeXmlEnteries();
    }
}

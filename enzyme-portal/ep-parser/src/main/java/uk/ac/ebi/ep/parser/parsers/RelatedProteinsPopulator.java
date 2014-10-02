/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.parser.parsers;

import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.data.domain.RelatedProteins;
import uk.ac.ebi.ep.data.domain.UniprotEntry;
import uk.ac.ebi.ep.data.service.RelatedProteinsService;
import uk.ac.ebi.ep.data.service.UniprotEntryService;

/**
 *
 * @author joseph
 */
public class RelatedProteinsPopulator {

    @Autowired
    private RelatedProteinsService relatedProteinsService;
    @Autowired
    private UniprotEntryService uniprotEntryService;

//    public RelatedProteinsPopulator(RelatedProteinsService repository, UniprotEntryService service) {
//        this.relatedProteinsService = repository;
//        this.uniprotEntryService = service;
//    }
    public void populateRelatedProteins() {
        List<RelatedProteins> relatedProteins = new LinkedList<RelatedProteins>();
        //obtain all entries

        List<UniprotEntry> proteins = uniprotEntryService.findUniprotEntries();
        for (UniprotEntry protein : proteins) {

            //System.out.println("proteins found " + protein.getName());
            RelatedProteins rp = new RelatedProteins();
            rp.setUniprotAccession(protein);

            rp.setNamePrefix(protein.getName().substring(0, protein.getName().indexOf("_")));
            //System.out.println("related prefix " + rp.getNamePrefix());
            relatedProteins.add(rp);
        }

        relatedProteinsService.addRelatedProteins(relatedProteins);
        System.out.println("finished populating "+ relatedProteins.size());
    }

}

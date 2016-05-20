/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.model;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.ep.ebeye.search.EbeyeSearchResult;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ProteinService {

    private final RestTemplate restTemplate = new RestTemplate();
    private List<Protein> proteins = null;

    public ProteinService() {
        proteins = new ArrayList<>();
    }

    public EbeyeSearchResult getEbeyeSearchResult(String ec, String accession) {
//http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?query=INTENZ:3.1.4.35AND%20UNIPROTKB:P52731&fields=id,name&size=7&format=json
        String url = "http://www.ebi.ac.uk/ebisearch/ws/rest/enzymeportal?query=INTENZ:" + ec + "AND UNIPROTKB:" + accession + "&fields=id,name,scientific_name&size=7&format=json";

        EbeyeSearchResult results = restTemplate.getForObject(url.trim(), EbeyeSearchResult.class);
        return results;
    }

    public List<uk.ac.ebi.ep.ebeye.search.Entry> getProteinView(String ec, String accession) {

        return getEbeyeSearchResult(ec, accession).getEntries();
    }

    public List<Protein> getProteins(String ec, String accession) {
        List<uk.ac.ebi.ep.ebeye.search.Entry> entries = getProteinView(ec, accession);
        for (uk.ac.ebi.ep.ebeye.search.Entry entry : entries) {
            Protein protein = new Protein(entry.getUniprotAccession(), entry.getTitle());
            proteins.add(protein);
        }
        return proteins;
    }
}

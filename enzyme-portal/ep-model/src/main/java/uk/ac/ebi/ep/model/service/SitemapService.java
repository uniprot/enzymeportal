package uk.ac.ebi.ep.model.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.repositories.UniprotEntryRepository;

/**
 *
 * @author <a href="mailto:joseph@ebi.ac.uk">Joseph</a>
 */
@Service
public class SitemapService {

    @Autowired
    private UniprotEntryRepository uniprotEntryRepository;

    @Transactional(readOnly = true)
    public List<String> findUniprotAccessions() {
        return uniprotEntryRepository.findAccessions();
    }
}

package uk.ac.ebi.ep.parser.parsers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.ac.ebi.ep.model.EnzymeReactionInfo;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.model.Rhea2kegg;
import uk.ac.ebi.ep.parser.tools.FileUtil;

/**
 *
 * @author joseph
 */
@Service("rheaReaction")
@Slf4j
public class RheaReaction {

    private static final String RHEA_XREF = "RHEA";
    private static final String RHEA_2_KEGG_WEB_PAGE = "ftp://ftp.ebi.ac.uk/pub/databases/rhea/tsv/rhea2kegg_reaction.tsv";

    @Autowired
    private EnzymePortalParserService enzymePortalParserService;

    /**
     *
     * @param fileLocation if null defaults to ${user.home}/data/RHEA
     */
    @Transactional(readOnly = true)
    public void parseAndLoadRheaReactions(String fileLocation) {
        log.warn("About to disable Accession-Reaction contraints");
        enzymePortalParserService.disableAccessionReactionContraints();
        log.warn("About to stream, parser and process reactionInfo for RHEA reactions");
        processReactionInfo();
        log.warn("Done loading RHEA Reaction to database.");
        log.warn("About to download and map kegg data to Rhea reaction ...");
        updateReactionWithKeggId(fileLocation);
        log.warn("Done updating RHEA Reaction with KEGG data.");
        enzymePortalParserService.deleteNonEnzymesReactions();
        log.warn("About to enable Accession-Reaction contraints");

        enzymePortalParserService.enableAccessionReactionContraints();
        log.warn("Done Parsing and Loading Rhea Data.");
    }

    private void loadRheaReaction(String rheaId, String uniprotAccession) {
        String url = "https://www.rhea-db.org/reaction?id=" + rheaId;
        enzymePortalParserService.addRheaReaction(rheaId, rheaId, RHEA_XREF, null, uniprotAccession, url, null);
    }

    private void updateRheaReaction(String keggId, String rheaId) {
        enzymePortalParserService.updateRheaReaction(keggId, rheaId);
    }

    private void updateReactionWithKeggId(String fileLocation) {
        Set<Rhea2kegg> rhea2Kegg = downloadAndProcessKeggData(fileLocation);
        log.warn("Done downloading and processing kegg data os size : " + rhea2Kegg.size());
        log.warn("About to update RHEA reactions with KEGG data ..");
       
        rhea2Kegg.forEach(r2k -> updateRheaReaction(r2k.getKeggId(), "RHEA:"+r2k.getRheaId()));
    }

    private void processReactionInfo() {
        try (Stream<EnzymeReactionInfo> rheaInfoStream = enzymePortalParserService.findAllReactionInfoByXrefTypeAndStream(RHEA_XREF)) {

            rheaInfoStream.forEach(rhea -> {
                loadRheaReaction(rhea.getXref(), rhea.getUniprotAccession());
            });

        }
    }

    private Set<Rhea2kegg> downloadAndProcessKeggData(String fileLocation) {
        Path path = Paths.get(System.getProperty("user.home"), "data", "RHEA");
        if (fileLocation != null) {
            path = Paths.get(fileLocation);
        }

        String permission = "rwxr-x---";
        try {
            Path directory = FileUtil.createDirectoriesWithPermission(path, permission);

            Path rhea2kegg = directory.resolve("rhea2kegg_reaction.tsv");
            FileUtil.downloadFile(RHEA_2_KEGG_WEB_PAGE, rhea2kegg);

            return processRhea2Kegg(rhea2kegg);

        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);

        }
        return new HashSet<>();
    }

    private Set<Rhea2kegg> buildRhea2Kegg(String rheaId, String keggId, Set<Rhea2kegg> r2kSet) {
        Rhea2kegg rhea2kegg = new Rhea2kegg(rheaId, keggId);
        r2kSet.add(rhea2kegg);
        return r2kSet;
    }

    public Set<Rhea2kegg> processRhea2Kegg(Path fileToRead) throws IOException {

        List<String> files = Files.readAllLines(fileToRead, StandardCharsets.UTF_8);
        Set<Rhea2kegg> rheaKeggList = new HashSet<>();

        files.stream()
                .skip(1)
                .map(s -> s.split("\t")).filter(l -> l.length > 3)
                .forEach(data -> buildRhea2Kegg(data[2], data[3], rheaKeggList));

        return rheaKeggList;
    }

}

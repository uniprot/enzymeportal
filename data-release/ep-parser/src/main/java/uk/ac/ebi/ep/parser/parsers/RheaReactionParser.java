package uk.ac.ebi.ep.parser.parsers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import uk.ac.ebi.ep.model.service.EnzymePortalParserService;
import uk.ac.ebi.ep.parser.model.Accession2Rhea;
import uk.ac.ebi.ep.parser.model.Rhea2kegg;

/**
 *
 * @author Joseph
 */
@Slf4j
public class RheaReactionParser {

    //private static final Logger logger = Logger.getLogger(EnzymePortalPDBeParser.class);
    private static final String rhea2keggWebPage = "ftp://ftp.ebi.ac.uk/pub/databases/rhea/tsv/rhea2kegg_reaction.tsv";
    private static final String rhea2uniprotWebPage = "ftp://ftp.ebi.ac.uk/pub/databases/rhea/tsv/rhea2uniprot.tsv";
    @Autowired
    private EnzymePortalParserService enzymePortalParserService;

    /**
     *
     * @param fileLocation if null defaults to ${user.home}/data/RHEA
     */
    public void parseAndLoadRheaReactions(String fileLocation) {
        Path path = Paths.get(System.getProperty("user.home"), "data", "RHEA");
        if (fileLocation != null) {
            path = Paths.get(fileLocation);
        }

        String permission = "rwxr-x---";
        try {
            Path directory = createDirectoriesWithPermission(path, permission);

            Path rhea2kegg = directory.resolve("rhea2kegg_reaction.tsv");
            Path rhea2uniprot = directory.resolve("rhea2uniprot.tsv");
            downloadFile(rhea2keggWebPage, rhea2kegg);
            downloadFile(rhea2uniprotWebPage, rhea2uniprot);

            Set<Rhea2kegg> r2kSet = processRhea2Kegg(rhea2kegg);

            List<Accession2Rhea> acc2RheaList = processAcc2Rhea(rhea2uniprot, r2kSet);

            loadReactionToDB(acc2RheaList);

        } catch (IOException ex) {
            log.error(ex.getMessage(),ex);

        }

    }

    private void downloadFile(String webPage, Path path) throws MalformedURLException, IOException {

        URI u = URI.create(webPage);
        try (InputStream in = u.toURL().openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        }

    }

    private FileAttribute<Set<PosixFilePermission>> fileAttributes(String permission) {
        Set<PosixFilePermission> perms
                = PosixFilePermissions.fromString(permission);

        return PosixFilePermissions.asFileAttribute(perms);
    }

    private Path createDirectoriesWithPermission(Path directories, String permission) throws IOException {

        FileAttribute<Set<PosixFilePermission>> attr = fileAttributes(permission);

        return Files.createDirectories(directories, attr);
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

    public List<Accession2Rhea> processAcc2Rhea(Path fileToRead, Set<Rhea2kegg> r2kSet) throws IOException {

        List<String> files = Files.readAllLines(fileToRead, StandardCharsets.UTF_8);

        List<String[]> data = files.stream()
                .skip(1)
                .map(s -> s.split("\t"))
                .filter(l -> l.length > 3)
                .collect(Collectors.toList());

        List<Accession2Rhea> uniprotRhea = new ArrayList<>();

        r2kSet.stream().forEach(rhea2kegg -> {
            data.stream().filter(d -> (rhea2kegg.getRheaId().trim().equalsIgnoreCase(d[2].trim())))
                    .map((d) -> {
                        Accession2Rhea accession2Rhea = new Accession2Rhea();
                        accession2Rhea.setAccession(d[3]);
                        return accession2Rhea;
                    }).map(accession2Rhea -> {
                        accession2Rhea.getRhea().add(rhea2kegg);
                        return accession2Rhea;
                    }).forEach(accession2Rhea -> {
                        uniprotRhea.add(accession2Rhea);
                    });
        });

        return uniprotRhea;

    }

    private void loadReactionToDB(List<Accession2Rhea> acc2RheaList) {

       
        acc2RheaList.stream().forEach(accession2Rhea -> {

            accession2Rhea.getRhea().forEach(rhea -> {
                String url = "https://www.rhea-db.org/reaction?id=" + rhea.getRheaId();
                System.out.println("Track data insert : "+ rhea.getRheaId() + " :: Acc "+ accession2Rhea.getAccession());
                log.error("Track data insert : "+ rhea.getRheaId() + " :: Acc "+ accession2Rhea.getAccession());
                enzymePortalParserService.addRheaReaction(rhea.getRheaId(), null, "RHEA", null, accession2Rhea.getAccession(), url, rhea.getKeggId());
            });
        });

       
    }
}

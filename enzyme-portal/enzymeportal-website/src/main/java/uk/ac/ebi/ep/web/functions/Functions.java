package uk.ac.ebi.ep.web.functions;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import uk.ac.ebi.ep.indexservice.model.protein.WithCofactor;
import uk.ac.ebi.ep.indexservice.model.protein.WithMetabolite;
import uk.ac.ebi.ep.indexservice.model.protein.WithPathway;
import uk.ac.ebi.ep.indexservice.model.protein.WithProteinFamily;
import uk.ac.ebi.ep.indexservice.model.protein.WithTaxonomy;

/**
 * Due to no similar functionality in JSTL, this function was designed to help
 * various operation in a Collection.
 *
 * @author joseph
 */
@Slf4j
public final class Functions {

    private Functions() {
        throw new IllegalStateException("Utility class");
    }

    public static WithMetabolite withMetabolite(List<WithMetabolite> metabolites, String metaboliteId, String accession, String commonName, String entryType) {
        String transformedId = metaboliteId.replace("MTBLC", "");
        return metabolites
                .stream()
                .filter(metabolite -> metabolite.getMetaboliteId().equalsIgnoreCase(transformedId))
                .findFirst().orElse(new WithMetabolite(metaboliteId, accession, commonName, entryType));

    }

    public static WithCofactor withCofactor(List<WithCofactor> cofactors, String cofactorId, String accession, String commonName, String entryType) {
        String cid = cofactorId.replaceAll("\"", "").replace("CHEBI:", "");

        return cofactors
                .stream()
                .filter(cofactor -> cofactor.getCofactorId().equalsIgnoreCase(cid))
                .sorted(Comparator.comparing(WithCofactor::getEntryType))
                .findFirst().orElse(new WithCofactor(cid, accession, commonName, entryType));

    }

    public static WithProteinFamily withProteinFamily(List<WithProteinFamily> proteinFamilies, String familyId, String accession, String commonName, String entryType) {

        return proteinFamilies
                .stream()
                .filter(family -> family.getFamilyId().equalsIgnoreCase(familyId))
                .sorted(Comparator.comparing(WithProteinFamily::getEntryType))
                .findFirst().orElse(new WithProteinFamily(familyId, accession, commonName, entryType));

    }

    public static WithTaxonomy withTaxonomy(List<WithTaxonomy> organisms, String taxId, String accession, String commonName, String entryType) {

        return organisms
                .stream()
                .filter(organism -> organism.getTaxonomyId().equalsIgnoreCase(taxId))
                .findFirst().orElse(new WithTaxonomy(taxId, accession, commonName, entryType));

    }

    public static WithPathway withPathway(List<WithPathway> pathways, String pathwayId, String accession, String commonName, String entryType) {

        String pid = pathwayId.replace("R-", "");
        return pathways
                .stream()
                .filter(p -> p.getPathwayId().contains(pid))//TODO
                //.filter(pathway -> pathway.getPathwayId().equalsIgnoreCase(pathwayId))
                .sorted(Comparator.comparing(WithPathway::getEntryType))
                .findFirst().orElse(new WithPathway(pathwayId, accession, commonName, entryType));

    }

    public static boolean startsWithDigit(String data) {
        return Character.isDigit(data.charAt(0));
    }

    /**
     * This function is to enable using capital letter case in checking if a
     * string starts with the letter
     *
     * @param data the original string
     * @param letter the first letter
     * @return true if the string starts with the first letter
     */
    public static boolean startsWithLowerCase(String data, String letter) {
        String current = data;
        if (startsWithDigit(data)) {
            current = data.replaceAll("(-)?\\d+(\\-\\d*)?", "").trim();

        }
        boolean lCase = current.startsWith(letter.toLowerCase());
        boolean nCase = current.startsWith(letter);
        return current.startsWith(letter) ? nCase : lCase;
    }

    /**
     *
     * @param collection
     * @param item
     * @return true if the item is contained in the collection
     */
    public static boolean contains(Collection collection, Object item) {
        return collection.contains(item);
    }

    /**
     *
     * @param collection list of items
     * @param last the last item in the list
     * @return true if the item is the last in the list
     */
    public static boolean lastInList(List<Object> collection, Object last) {
        boolean eval = false;
        LinkedList<Object> list = new LinkedList<>(collection);
        if (last != null && isEqualsToLastInList(list, last)) {

            eval = true;

        }

        return eval;
    }

    private static boolean isEqualsToLastInList(LinkedList<Object> list, Object last) {

        return last.equals(list.getLast());
    }

    public static String removeSlash(String url) {
        String link = url;
        if (url.contains("/")) {
            link = url.replace("/", "");

        }

        return "#omim" + link;
    }

    public static String formatRheaId(String rheaId) {
        if (rheaId.toUpperCase().contains("RHEA:")) {
            return rheaId.replace("RHEA:", "").trim();
        }
        return rheaId;
    }

    /**
     * split with = and - to return only the search term
     *
     * @param text of this format searchparams.text=cathepsin-1.1.1.1 for
     * example
     * @return index[1] - cathepsin
     */
    public static String splitAndGetValue(String text) {
        String result = "";

        if (text == null || "".equals(text)) {
            return result;
        } else {

            String[] data = text.split("searchKey=");

            if (data.length > 1) {
                String[] keyword = data[1].split("-");
                result = keyword[0];
            }
        }
        return result;
    }

}

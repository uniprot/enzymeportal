package uk.ac.ebi.ep.adapter.uniprot;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Molecule;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.FieldType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
import uk.ac.ebi.kraken.model.uniprot.comments.DiseaseCommentImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.EnzymeRegulationCommentImpl;
import uk.ac.ebi.kraken.model.uniprot.comments.FunctionCommentImpl;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class Transformer {

    static final String COMMON_REGEX =
            "|\\b[Cc]ompletely\\b|\\b[Pp]artially\\b"
            + "|\\s?\\band\\b\\s?|;|\\bor\\b|\\b[Pp]otentially\\b|\\b[Aa]lso\\b"
            + "|\\b[Bb]y\\b|\\b[Ss]pecifically\\b|\\b[Aa]llosterically\\b"
            + "|\\b[Cc]compound[s\\s]\\b|\\(|\\)|\\b[Ss]imilarity\\b|\\b[Mm]ost\\b|\\b[Pp]otently\\b"
            + "|\\b[Ss]trongly\\b|\\b[Mm]oderately\\b|\\b[Ss]lightly\\b|[^(\\w\\d\\+\\-(\\sacid)]";
    static final String INHIBITOR_REGEX = "\\b[Ii]n(hibited|activated) by\\b";
    static final String ACTIVATOR_REGEX = "\\b[Aa]ctivated by\\b";

    public static String getCommentText(List<Comment> commentList) {
        StringBuilder sb = new StringBuilder();
        for (Comment comment : commentList) {
            FunctionCommentImpl castedComment = (FunctionCommentImpl) comment;
            String commentText = castedComment.getValue();
            sb.append(commentText);
            if (commentList.size() > 1) {
                sb.append("\n");
            }
            //comment.getEvidenceIds();
        }
        return sb.toString();
    }

    public static List<Disease> getDiseases(List<Comment> commentList) {
        List<Disease> diseases = new ArrayList<Disease>();
        for (Comment comment : commentList) {
            Disease disease = new Disease();
            DiseaseCommentImpl castedComment = (DiseaseCommentImpl) comment;
            //Work around to delete Note= from the comment
            String commentText = castedComment.getValue().replace("Note=", "");
            String id = String.valueOf(castedComment.getId());
            disease.setId(id);
            disease.setDescription(commentText);
            diseases.add(disease);
        }
        return diseases;
    }

    /**
     * gets a list of small molecules mentioned in enzyme regulation comments.
     * @param commentList
     * @return
     */
    public static String getMoleculeComments(List<Comment> commentList) {
        StringBuilder comments = new StringBuilder();
        for (Comment comment : commentList) {
            EnzymeRegulationCommentImpl castedComment = (EnzymeRegulationCommentImpl) comment;
            String commentText = castedComment.getValue();
            comments.append(commentText);
        }
        return comments.toString();
    }

    /**
     * parses a free text to get a list of molecules and groups these molecules into
     * inhibitor, activator. <br/>
     * Eg.: "Completely inhibited by ADP and ADP-glucose, and partially inhibited by ATP and NADH"
     * will be grouped as inhibitor->[ADP, ADP-glucose, ATP, NADH]
     */
    public static List<Molecule> parseTextForInhibitors(String text) {
        List<Molecule> inhibitors = parseText(text, INHIBITOR_REGEX + COMMON_REGEX);
        return inhibitors;
    }

    public static List<Molecule> parseTextForActivators(String text) {
        List<Molecule> inhibitors = parseText(text, ACTIVATOR_REGEX + COMMON_REGEX);
        return inhibitors;
    }
    static final Comparator<Molecule> SORT_MOLECULES = new Comparator<Molecule>() {

        public int compare(Molecule m1, Molecule m2) {
            int compare_specie = m1.getName().compareToIgnoreCase(m2.getName());
            if (compare_specie != 0) {
                return compare_specie;
            }

            return m1.getName().equalsIgnoreCase(m2.getName()) ? 0 : 1;
        }
    };

    public static List<Molecule> parseText(String text, String regex) {

        LinkedList<Molecule> molecules = new LinkedList<Molecule>();
        Set<Molecule> theResult = new TreeSet<Molecule>(SORT_MOLECULES);
        String cleanedText = text.replaceAll(regex, ",");
        StringTokenizer stringTokenizer = new StringTokenizer(cleanedText, ",");
        LinkedList<Molecule> results = new LinkedList<Molecule>();

        while (stringTokenizer.hasMoreTokens()) {
            String token = stringTokenizer.nextToken().trim();
            Molecule molecule = new Molecule();
            if (!token.equals("")) {

                if (token.matches("3\\+") || token.matches("2\\+") || token.matches("\\+") || token.matches("\\-") || token.matches("\\-+") || token.matches("\\--")) {

                    String lastToken = token;
                    Molecule duplicate = null;
                    if (results.size() > 0) {
                        duplicate = results.peek();
                        lastToken = results.poll().getName().concat(token);
                    }

                    molecule.setName(lastToken);
                    if (!results.contains(molecule)) {
                        results.add(molecule);
                    }
                    if (!theResult.containsAll(results)) {
                        theResult.addAll(results);
                    }

                    if (duplicate != null && theResult.contains(duplicate)) {
                        theResult.remove(duplicate);
                    }

                    if (token.endsWith("+")) {

                        results.clear();
                    }

                } else {
                    molecule.setName(token);
                    if (!results.contains(molecule)) {
                        results.add(molecule);
                        theResult.addAll(results);
                    }
                }

            }

        }

        molecules.addAll(theResult);
        return molecules;
    }

    public static String getFullName(Name name) {
        List<Field> fields = name.getFieldsByType(FieldType.FULL);
        String nameValue = null;
        for (Field field : fields) {
            nameValue = field.getValue();
            break;
        }
        return nameValue;
    }

    public static List<String> getNames(Name name) {
        List<Field> fields = name.getFields();
        List<String> names = new ArrayList<String>();
        String nameValue = null;
        for (Field field : fields) {
            nameValue = field.getValue();
            names.add(nameValue);
        }
        return names;
    }

    public static List<String> getAltNames(List<Name> altNames) {
        List<String> synonyms = new ArrayList<String>();
        for (Name altName : altNames) {
            synonyms.addAll(getNames(altName));
        }
        return synonyms;
    }

    public static String getMessageTemplate(String tplName, Object[] messageArguments) {
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle_ognl");
        //Object[] messageArguments = {tplName};
        MessageFormat formatter = new MessageFormat("");
        formatter.applyPattern(messages.getString(tplName));
        String message = formatter.format(messageArguments);
        return message;
    }

    public static String getMessageTemplate(String tplName) {
        ResourceBundle messages = ResourceBundle.getBundle("MessageBundle_ognl");
        return messages.getString(tplName);
    }
}

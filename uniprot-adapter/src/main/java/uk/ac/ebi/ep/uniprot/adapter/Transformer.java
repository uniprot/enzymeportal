package uk.ac.ebi.ep.uniprot.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.FieldType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
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

//********************************* VARIABLES ********************************//


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

    public static String getFunction(List<Comment> commentList) {
        Iterator it = commentList.iterator();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            FunctionCommentImpl comment = (FunctionCommentImpl)it.next();
            String commentText = comment.getValue();
            sb.append(commentText);
            sb.append(" ");
            //comment.getEvidenceIds();
        }
        return sb.toString();

    }
/*
    public static List<String> transformNames(List<Name> altName) {
        Iterator it = altName.iterator();
        List<String> synonyms = new ArrayList<String>();
        while (it.hasNext()) {
            Name name = (Name)it.next();
            String nameValue = getFullName(name.getFields());
            FieldType.
            synonyms.add(name.toString());
        }
        return synonyms;
    }
    */

    public static String getFullName(Name name) {
        List<Field> fields = name.getFieldsByType(FieldType.FULL);
        String nameValue = null;
        for (Field field:fields) {
            nameValue = field.getValue();
            break;
        }
        return nameValue;
    }
    public static  List<String> getNames(Name name) {
        List<Field> fields = name.getFields();
        List<String> names = new ArrayList<String>();
        String nameValue = null;
        for (Field field:fields) {
            nameValue = field.getValue();
            names.add(nameValue);
        }
        return names;
    }
    public static List<String> getAltNames (List<Name> altNames) {
        List<String> synonyms = new ArrayList<String>();
        for (Name altName: altNames) {   
            synonyms.addAll(getNames(altName));
        }
        return synonyms;
    }
}

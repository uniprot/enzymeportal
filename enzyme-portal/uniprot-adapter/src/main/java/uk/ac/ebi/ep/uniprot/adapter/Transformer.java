package uk.ac.ebi.ep.uniprot.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.FieldType;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;

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

    public static String transformToFunction(List<Comment> commentList) {
        Iterator it = commentList.iterator();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Comment comment = (Comment)it.next();
            String commentText = comment.toString();
            sb.append(commentText);
            //comment.getEvidenceIds();
        }
        return sb.toString();

    }

    public static List<String> transformNames(List<Name> altName) {
        Iterator it = altName.iterator();
        List<String> synonyms = new ArrayList<String>();
        while (it.hasNext()) {
            Name name = (Name)it.next();
            String nameValue = transformToName(name.getFields());
            synonyms.add(name.toString());
        }
        return synonyms;
    }

    public static String transformToName(List<Field> fields) {
        Iterator it = fields.iterator();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Field field = (Field)it.next();
            FieldType fieldType = field.getType();
            if (!fieldType.equals(FieldType.EC))
            return field.getValue();
        }
        return sb.toString();
    }
}

package uk.ac.ebi.ep.adapter.intenz.util;

import java.text.MessageFormat;

import uk.ac.ebi.ep.adapter.intenz.IntenzConfig;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class IntenzUtil {

    public static final String INTENZ_FTP_ADD =
            "ftp://ftp.ebi.ac.uk/pub/databases/intenz/xml/ASCII";
    public static final String EC_PREFIX = "EC_";

    /**
     * @deprecated Use {@link #createIntenzEntryUrl(String, String)} instead.
     * @param ecNumber
     * @return
     */
    public static String createIntenzEntryUrl(String ecNumber) {
        String[] ecNumberFragments = ecNumber.split("\\.");
        StringBuffer sb = new StringBuffer();
        sb.append(INTENZ_FTP_ADD);
        String ecSuperClassNr = "";
        for (String ecNumberFragment: ecNumberFragments) {
            sb.append("/");
            sb.append(EC_PREFIX);
            String currentNr = "";
            if (ecSuperClassNr.equals("")) {
                currentNr = ecNumberFragment;
            }
            else {
                currentNr = ecSuperClassNr + "." + ecNumberFragment;
            }
            sb.append(currentNr);
             ecSuperClassNr = currentNr;
        }
        sb.append(".xml");
        return sb.toString();
    }
    
    /**
     * Generates a valid URL to retrieve IntEnzXML.
     * @param format the URL format as defined in
     * {@link IntenzConfig#setIntenzXmlUrl(String)}.
     * @param ecNumber
     * @return
     */
    public static String createIntenzEntryUrl(String format, String ecNumber){
    	String[] ecs = ecNumber.split("\\.");
    	return MessageFormat.format(format, ecs[0], ecs[1], ecs[2], ecs[3]);
    }

}

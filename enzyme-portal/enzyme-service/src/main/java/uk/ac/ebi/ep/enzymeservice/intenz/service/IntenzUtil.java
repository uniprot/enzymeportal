package uk.ac.ebi.ep.enzymeservices.util;

import java.text.MessageFormat;



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

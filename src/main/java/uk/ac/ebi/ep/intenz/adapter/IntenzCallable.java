package uk.ac.ebi.ep.intenz.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.log4j.Logger;
import uk.ac.ebi.intenz.adapter.exception.SynonymException;
import uk.ac.ebi.intenz.xml.jaxb.EnzymeNameType;
import uk.ac.ebi.intenz.xml.jaxb.Intenz;
import uk.ac.ebi.intenz.xml.jaxb.Synonyms;

/**
 *
 * @since   1.0
 * @version $LastChangedRevision$ <br/>
 *          $LastChangedDate$ <br/>
 *          $Author$
 * @author  $Author$
 */
public class IntenzCallable {

//********************************* VARIABLES ********************************//
    public static final String INTENZ_PACKAGE = "uk.ac.ebi.intenz.xml.jaxb";


//******************************** CONSTRUCTORS ******************************//


//****************************** GETTER & SETTER *****************************//


//********************************** METHODS *********************************//

//******************************** INNER CLASS *******************************//

public static class GetSynonymsCaller implements Callable<Set<String>> {
    private static Logger log = Logger.getLogger(GetSynonymsCaller.class);
    protected String ecUrl;

    public GetSynonymsCaller(String ecUrl) {
        this.ecUrl = ecUrl;
    }




    public Set<String> call() throws Exception {
        return getSynonyms();
    }

    public Set<String> getSynonyms() throws SynonymException {
        URL url = null;
            try {
                url = new URL(ecUrl);
            } catch (MalformedURLException ex) {
                throw new SynonymException(IintenzAdapter.FAILED_MSG + "EC number is not valid!"
                        , ex);
            }

        URLConnection con = null;
            try {
                con = url.openConnection();
            } catch (IOException ex) {
                throw new SynonymException(IintenzAdapter.FAILED_MSG + "Unable to connection to Intenz server!"
                        , ex);
            }

        InputStream is = null;
            try {
                is = con.getInputStream();
            } catch (IOException ex) {
                /*If there is an error of this type write in the log rather than
                 * throwing a exception so that the processing can continue.
                 */
                log.error(IintenzAdapter.FAILED_MSG + "Unable download the xml file from "
                        +this.ecUrl +"!", ex);
                /*
                throw new SynonymException(IintenzAdapter.FAILED_MSG + "Unable download the xml file from "
                        +this.ecUrl +"!", ex);
                 * *
                 */
            }

        JAXBContext jaxbContext = null;
            try {
                jaxbContext = JAXBContext.newInstance(INTENZ_PACKAGE);
            } catch (JAXBException ex) {
                throw new SynonymException(IintenzAdapter.FAILED_MSG + "Unable to find the package "
                        +INTENZ_PACKAGE  +" to map the intenz xml file!"
                        , ex);
            }
        Unmarshaller unmarshaller = null;
            try {
                unmarshaller = jaxbContext.createUnmarshaller();
            } catch (JAXBException ex) {
                throw new SynonymException(IintenzAdapter.FAILED_MSG + "Failed to create JAXB Context!"
                        , ex);
            }
        Intenz intenz = null;
            try {
                if (is != null) {
                    intenz = (Intenz) unmarshaller.unmarshal(is);
                }
            } catch (JAXBException ex) {
                 throw new SynonymException(IintenzAdapter.FAILED_MSG + "unmarshal Intenz xml file " +
                         "to the object model failed!", ex);
            }
            finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException ex) {
                    log.warn("Failed to close inputstream of the intenz xml file! ");
                }
            }

         Set<String> synonyms = new LinkedHashSet<String>();
         if (intenz != null) {
             synonyms.addAll(getSynonyms(intenz));
         }
        return synonyms;
      }

        public Set<String> getSynonyms(Intenz intenz) {
            Set<String> names = new LinkedHashSet<String>();
            Synonyms synonymsType =
                    intenz.getEcClass().get(0).getEcSubclass().get(0)
                    .getEcSubSubclass().get(0).getEnzyme().get(0).getSynonyms();
            if (synonymsType != null) {
                List<EnzymeNameType> synonyms = synonymsType.getSynonym();
                if (synonyms.size() > 0) {
                    for (EnzymeNameType synonym : synonyms) {
                        List<Object> objList = synonym.getContent();
                        String name = (String) objList.get(0);
                        names.add(name);
                    }

                }

            }
            return names;
        }
    }

}

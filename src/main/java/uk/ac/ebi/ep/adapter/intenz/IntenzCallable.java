package uk.ac.ebi.ep.adapter.intenz;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import uk.ac.ebi.ep.enzyme.model.EcClass;
import uk.ac.ebi.ep.enzyme.model.EnzymeHierarchy;
import uk.ac.ebi.intenz.xml.jaxb.EcClassType;
import uk.ac.ebi.intenz.xml.jaxb.EcSubclassType;
import uk.ac.ebi.intenz.xml.jaxb.EcSubsubclassType;
import uk.ac.ebi.intenz.xml.jaxb.EntryType;
import uk.ac.ebi.intenz.xml.jaxb.EnzymeNameType;
import uk.ac.ebi.intenz.xml.jaxb.Intenz;
import uk.ac.ebi.intenz.xml.jaxb.Synonyms;
import uk.ac.ebi.intenz.xml.jaxb.XmlContentType;

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
    private static final Logger LOGGER = Logger.getLogger(IntenzCallable.class);
    private static JAXBContext jaxbContext = null;
    static {
    	try {
	        jaxbContext = JAXBContext.newInstance(INTENZ_PACKAGE);
	    } catch (JAXBException ex) {
	        LOGGER.error(IintenzAdapter.FAILED_MSG + "Unable to find the package "
	                + INTENZ_PACKAGE + " to map the intenz xml file!", ex);
	    }
    }

    public static class GetIntenzCaller implements Callable<Intenz> {

        protected String ecUrl;

        public GetIntenzCaller(String ecUrl) {
            this.ecUrl = ecUrl;
        }

        public GetIntenzCaller() {
        }

        public Intenz call() throws Exception {
            return getData();
        }

        public Intenz getData() {
            Intenz intenz = null;
            URL url = null;
            URLConnection con = null;
            InputStream is = null;
            try {
                url = new URL(ecUrl);
//                LOGGER.debug("SEARCH before openConnection");
                con = url.openConnection(Proxy.NO_PROXY);
                con.connect();
//                LOGGER.debug("SEARCH before getInputStream");
                is = con.getInputStream();
//                LOGGER.debug("SEARCH before creating unmarshaller");
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                if (is != null) {
//                    LOGGER.debug("SEARCH before unmarshal");
                    intenz = (Intenz) unmarshaller.unmarshal(is);
//                    LOGGER.debug("SEARCH after unmarshal");
                }
            } catch (MalformedURLException ex) {
                LOGGER.error(IintenzAdapter.FAILED_MSG
                        + "ec url is invalid: " + this.ecUrl, ex);
            } catch (IOException ex) {
                LOGGER.error(IintenzAdapter.FAILED_MSG + "Unable to connect to Intenz server!", ex);
            } catch (JAXBException ex) {
                LOGGER.error(IintenzAdapter.FAILED_MSG + "Failed to create/use unmarshaller!", ex);
            } finally {
                try {
                    if (is != null) is.close();
                } catch (IOException ex) {
                    LOGGER.warn("Failed to close inputstream of the intenz xml file! ");
                }
            }
            return intenz;
        }

        public EcClass setEnzymeName(XmlContentType contentType, String ecNumber) {
            EcClass ecClass = new EcClass();
            String name = null;
            if (contentType != null) {
                List<Object> nameObject = contentType.getContent();
                name = (String) nameObject.get(0);
            } else {
                name = ecNumber;
            }

            ecClass.setEc(ecNumber);
            ecClass.setName(name);
            return ecClass;
        }

        public List<EcClass> getEcClass(Intenz intenz) {
            List<EcClass> ecClasseList = new ArrayList<EcClass>();
            EcClassType levelOne = intenz.getEcClass().get(0);
            String levelOneEc = levelOne.getEc1().toString();
            EcClass ecClass = setEnzymeName(levelOne.getName(), levelOneEc);
            ecClasseList.add(ecClass);

            EcSubclassType levelTwo =
            		(EcSubclassType) levelOne.getEcSubclass().get(0);
            String levelTwoEc = levelOneEc +"." +levelTwo.getEc2().toString();
            EcClass ecClass2 = setEnzymeName(levelTwo.getName(), levelTwoEc);
            ecClasseList.add(ecClass2);

            EcSubsubclassType levelThree =
            		(EcSubsubclassType) levelTwo.getEcSubSubclass().get(0);
            String levelThreeEc =
            		levelTwoEc+"." + levelThree.getEc3().toString();
            EcClass ecClass3 =
            		setEnzymeName(levelThree.getName(), levelThreeEc);
            ecClasseList.add(ecClass3);

            EntryType levelFour = (EntryType) levelThree.getEnzyme().get(0);
//            String levelFourEc = levelThreeEc +"." +levelFour.getEc4().toString();
            List<EnzymeNameType> acceptedNames = levelFour.getAcceptedName();
            String enzymeName = null;
            if (acceptedNames == null || acceptedNames.isEmpty()){
            	// transferred or deleted entry?
            	if (levelFour.getTransferred() != null){
            		enzymeName = levelFour.getTransferred().getNote();
            	} else if (levelFour.getDeleted() != null){
            		enzymeName = levelFour.getDeleted().getNote();
            	}
            } else {
                enzymeName = (String) acceptedNames.get(0).getContent().get(0);
            }
            final String ecNumber = levelFour.getEc().replace("EC ", "");
			EcClass ecClass4 = new EcClass().withEc(ecNumber).withName(enzymeName);
            ecClasseList.add(ecClass4);

            return ecClasseList;
        }
    }

    public static class GetSynonymsCaller implements Callable<Set<String>> {

        protected GetIntenzCaller intenzCaller;

        public GetSynonymsCaller(String ecUrl) {
            intenzCaller = new GetIntenzCaller(ecUrl);
        }

        public GetSynonymsCaller() {
        }

        public Set<String> call() throws Exception {
            return getSynonyms();
        }

        public Set<String> getSynonyms() {
            Intenz intenz = intenzCaller.getData();
            Set<String> synonyms = new LinkedHashSet<String>();
            if (intenz != null) {
                synonyms.addAll(getSynonyms(intenz));
            }
            return synonyms;
        }

        public Set<String> getSynonyms(Intenz intenz) {
            Set<String> names = new LinkedHashSet<String>();
            Synonyms synonymsType =
                    intenz.getEcClass().get(0).getEcSubclass().get(0).getEcSubSubclass().get(0).getEnzyme().get(0).getSynonyms();
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

    public static class GetEcHierarchyCaller implements Callable<EnzymeHierarchy> {

        protected GetIntenzCaller intenzCaller;

        public GetEcHierarchyCaller(String ecUrl) {
            intenzCaller = new GetIntenzCaller(ecUrl);
        }

        public GetEcHierarchyCaller() {
            intenzCaller = new GetIntenzCaller();
        }

        public EnzymeHierarchy call() throws Exception {
            Intenz intenz = intenzCaller.getData();
            return getEcHierarchy(intenz);
        }

        public EnzymeHierarchy getEcHierarchy(Intenz intenz) {
            EnzymeHierarchy enzymeHierarchy = new EnzymeHierarchy();
            List<EcClass> ecClassList = intenzCaller.getEcClass(intenz);
            enzymeHierarchy.setEcclass(ecClassList);
            return enzymeHierarchy;
        }
    }
}

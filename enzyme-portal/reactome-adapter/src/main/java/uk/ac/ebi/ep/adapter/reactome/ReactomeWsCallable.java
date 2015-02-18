package uk.ac.ebi.ep.adapter.reactome;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.concurrent.Callable;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import uk.ac.ebi.biobabel.util.xml.XPathSAXHandler;
import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.rhea.domain.Database;

/**
 * Class to retrieve reactions and pathways from Reactome REST web services.
 * @author rafa
 */
public class ReactomeWsCallable implements Callable<Pathway> {

    private static final Logger LOGGER =
    		Logger.getLogger(ReactomeWsCallable.class);
    
    public static final String WS_BASE_URL =
            "http://reactomews.oicr.on.ca:8080/ReactomeRESTfulAPI/RESTfulWS/queryById/";
    // Hrefs within descriptions have to be made absolute:
    private static final String AHREF_PATTERN = "<a href='/";
    private static final String AHREF_REPLACEMENT =
    		"<a href='http://www.reactome.org/";
    
    // XPaths for the SAX parser:
    private static final String DISPLAYNAME = "//{reactomeClass}/displayName";
    private static final String FIGURE_DISPLAYNAME =
    		"//{reactomeClass}/figure/displayName";
    private static final String SUMMATION_DBID =
    		"//{reactomeClass}/summation/dbId";
    private static final String SUMMATION_TEXT = "//summation/text";

    public enum ReactomeClass {
        Reaction, Pathway, Summation
    }
    
    private ReactomeConfig config;
    private String pathwayId;

    /**
     * Constructs a new callable.
     * @param config
     * @param pathwayId a Reactome stable pathway ID (may be <code>null</code>,
     * 		if this object is not to be used as Callable).
     */
    public ReactomeWsCallable(ReactomeConfig config, String pathwayId) {
        this.config = config;
        this.pathwayId = pathwayId;
    }

    public Pathway call() throws Exception {
    	return getPathway(pathwayId);
    }

    /**
     * Gets a reaction (complete with description and xref to Reactome).
     * @param reactionId a Reactome reaction stable ID.
     * @return an EnzymeReaction object with name, URL and description (if
     * 		available).
     * @throws ReactomeConnectionException in case of problem getting the XML
     * 		from Reactome.
     * @throws ReactomeFetchDataException in case of problem parsing the
     * 		XML from Reactome.
     */
    public EnzymeReaction getReaction(String reactionId)
	throws ReactomeConnectionException, ReactomeFetchDataException {
		EnzymeReaction reaction = new EnzymeReaction();
		reaction.setId(reactionId);
		reaction.setUrl(Database.REACTOME.getEntryUrl(reactionId));
        StringBuilder sb = new StringBuilder(config.getWsBaseUrl())
				.append(ReactomeClass.Reaction.name()).append('/')
				.append(reactionId);
        InputStream is = null;
        final String nameXpath = DISPLAYNAME.replace(
        		"{reactomeClass}", ReactomeClass.Reaction.name().toLowerCase());
        final String summIdXpath = SUMMATION_DBID.replace(
        		"{reactomeClass}", ReactomeClass.Reaction.name().toLowerCase());
    	try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            XPathSAXHandler handler =
            		new XPathSAXHandler(nameXpath, summIdXpath);
            xr.setContentHandler(handler);    		
            URL url = new URL(sb.toString());
            URLConnection urlCon = config.getUseProxy()
                    ? url.openConnection()
                    : url.openConnection(Proxy.NO_PROXY);
            urlCon.setRequestProperty("Accept", "application/xml");
            is = urlCon.getInputStream();
            InputSource inputSource = new InputSource(is);
            xr.parse(inputSource);
            if (handler.getResults().get(nameXpath) != null){
            	reaction.setName(handler.getResults()
            			.get(nameXpath).iterator().next());
            }
            if (handler.getResults().get(summIdXpath) != null){
                final String summId =
                		handler.getResults().get(summIdXpath).iterator().next();
				reaction.setDescription(getDescription(summId));
            }
        } catch (MalformedURLException e) {
            throw new ReactomeConnectionException(sb.toString(), e);
        } catch (IOException e) {
            throw new ReactomeConnectionException(sb.toString(), e);
        } catch (SAXException e) {
            throw new ReactomeFetchDataException(e);
		} finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
		return reaction;
    }

    /**
     * Retrieves one pathway from the Reactome web services.
     * @param pathwayId a <b>stable</b> Reactome ID <b>without version</b>
     * 		(for example, "REACT_21342", not "REACT_21342.1").
     * @return a Pathway with name, description, URL and image (if available).
     * @throws ReactomeConnectionException in case of problem getting the XML
     * 		from Reactome.
     * @throws ReactomeFetchDataException in case of problem parsing the
     * 		XML from Reactome.
     */
    public Pathway getPathway(String pathwayId)
	throws ReactomeConnectionException, ReactomeFetchDataException{
    	Pathway pathway = new Pathway();
    	pathway.setId(pathwayId);
    	pathway.setUrl(Database.REACTOME.getEntryUrl(pathwayId));
        StringBuilder sb = new StringBuilder(config.getWsBaseUrl())
				.append(ReactomeClass.Pathway.name()).append('/')
				.append(pathwayId);
        InputStream is = null;
        final String nameXpath = DISPLAYNAME.replace(
        		"{reactomeClass}", ReactomeClass.Pathway.name().toLowerCase());
        final String summIdXpath = SUMMATION_DBID.replace(
        		"{reactomeClass}", ReactomeClass.Pathway.name().toLowerCase());
        final String figureXpath = FIGURE_DISPLAYNAME.replace(
        		"{reactomeClass}", ReactomeClass.Pathway.name().toLowerCase());
    	try {
            XMLReader xr = XMLReaderFactory.createXMLReader();
            XPathSAXHandler handler =
            		new XPathSAXHandler(nameXpath, summIdXpath, figureXpath);
            xr.setContentHandler(handler);    		
            URL url = new URL(sb.toString());
            URLConnection urlCon = config.getUseProxy()
                    ? url.openConnection()
                    : url.openConnection(Proxy.NO_PROXY);
            urlCon.setRequestProperty("Accept", "application/xml");
            is = urlCon.getInputStream();
            InputSource inputSource = new InputSource(is);
            xr.parse(inputSource);
            if (handler.getResults().get(nameXpath) != null){
            	pathway.setName(handler.getResults()
            			.get(nameXpath).iterator().next());
            }
            if (handler.getResults().get(summIdXpath) != null){
                final String summId =
                		handler.getResults().get(summIdXpath).iterator().next();
				pathway.setDescription(getDescription(summId));
            }
            if (handler.getResults().get(figureXpath) != null){
            	pathway.setImage(handler.getResults()
            			.get(figureXpath).iterator().next());
            }
        } catch (MalformedURLException e) {
            throw new ReactomeConnectionException(sb.toString(), e);
        } catch (IOException e) {
            throw new ReactomeConnectionException(sb.toString(), e);
        } catch (SAXException e) {
            throw new ReactomeFetchDataException(e);
		} finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
    	return pathway;
    }

    /**
     * Gets pathways related to a reaction.
     * @param reactionId a Reactome reaction stable ID
     * @return a collection of pathways in which the reaction can be involved,
     * 		or <code>null</code> if none found.
     */
    public Collection<Pathway> getPathways(String reactionId) {
        throw new NotImplementedException();
    }

	/**
     * Retrieves a description of a reaction or pathway.
     * @param summaryId A Reactome summary ID.
     * @return a description.
     * @throws ReactomeConnectionException in case of problem getting the XML
     * 		from Reactome.
     * @throws ReactomeFetchDataException in case of problem parsing the
     * 		XML from Reactome.
     */
    protected String getDescription(String summaryId)
	throws ReactomeConnectionException, ReactomeFetchDataException{
    	String desc = null;
		StringBuilder sb = new StringBuilder(config.getWsBaseUrl())
				.append(ReactomeClass.Summation.name()).append('/')
				.append(summaryId);
		InputStream is = null;
		try {
			URL url = new URL(sb.toString());
			URLConnection urlCon = config.getUseProxy()?
					url.openConnection():
			        url.openConnection(Proxy.NO_PROXY);
	        urlCon.setRequestProperty("Accept", "application/xml");
	        is = urlCon.getInputStream();
	        InputSource inputSource = new InputSource(is);
	        XMLReader xr = XMLReaderFactory.createXMLReader();
	        XPathSAXHandler handler = new XPathSAXHandler(SUMMATION_TEXT);
	        xr.setContentHandler(handler);    		
	        xr.parse(inputSource);
	        Collection<String> summations =
	        		handler.getResults().get(SUMMATION_TEXT);
			if (summations != null){
				desc = summations.iterator().next()
						.replaceAll(AHREF_PATTERN, AHREF_REPLACEMENT);
			}
        } catch (MalformedURLException e) {
            throw new ReactomeConnectionException(sb.toString(), e);
        } catch (IOException e) {
            throw new ReactomeConnectionException(sb.toString(), e);
        } catch (SAXException e) {
            throw new ReactomeFetchDataException(e);
		} finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOGGER.error(e);
                }
            }
        }
		return desc;
    }

	/**
     * Retrieves the description for a Reactome object (reaction or pathway).
     * @param reactomeId the Reactome <b>stable</b> ID
     * @return a description for the object.
     * @throws ReactomeServiceException if there is any problem with the web
     * 		service.
     */
    public String getDescription(ReactomeClass reactomeClass, String reactomeId)
            throws ReactomeServiceException {
        String desc = null;
    	switch (reactomeClass) {
		case Reaction:
			desc = getReaction(reactomeId).getDescription();
			break;
		case Pathway:
			desc = getPathway(reactomeId).getDescription();
			break;
		default:
			throw new IllegalArgumentException(
					"Descriptions only for reactions and pathways");
		}
        return desc;
    }
}

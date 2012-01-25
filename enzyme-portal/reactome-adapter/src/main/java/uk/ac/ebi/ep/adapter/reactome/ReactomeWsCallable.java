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

import uk.ac.ebi.ep.enzyme.model.EnzymeReaction;
import uk.ac.ebi.ep.enzyme.model.Pathway;
import uk.ac.ebi.ep.enzyme.model.ReactionPathway;

/**
 * Class to retrieve reactions and pathways from Reactome REST web services.
 * @author rafa
 *
 */
public class ReactomeWsCallable implements Callable<ReactionPathway>{

	private static final Logger LOGGER = Logger.getLogger(ReactomeWsCallable.class);
	
	public static final String WS_BASE_URL =
			"http://www.reactome.org:8080/ReactomeRESTfulAPI/RESTfulWS/queryById/";
	
	public enum ReactomeClass { Reaction, Pathway, Summation }
	
	private ReactomeConfig config;
	
	public ReactomeWsCallable(ReactomeConfig config){
		this.config = config;
	}
	
	public ReactionPathway call() throws Exception {
		throw new NotImplementedException();
	}

	/**
	 * Gets a reaction (complete with description and xref to Reactome).
	 * @param reactionId a Reactome reaction stable ID.
	 * @return an EnzymeReaction object.
	 */
	public EnzymeReaction getReaction(String reactionId){
//		EnzymeReaction reaction = null;
//		
//		return reaction;
		throw new NotImplementedException();
	}
	
	/**
	 * Gets pathways related to a reaction.
	 * @param reactionId a Reactome reaction stable ID
	 * @return a collection of pathways in which the reaction can be involved,
	 * 		or <code>null</code> if none found.
	 */
	public Collection<Pathway> getPathways(String reactionId){
//		Collection<Pathway> pathway = null;
//		
//		return pathway;
		throw new NotImplementedException();
	}
	
	/**
	 * Retrieves the description for a Reactome object (reaction or pathway).
	 * @param reactomeId the Reactome stable ID
	 * @return a description for the object.
	 * @throws ReactomeServiceException if there is any problem with the web
	 * 		service.
	 */
	public String getDescription(ReactomeClass reactomeClass, String reactomeId)
	throws ReactomeServiceException{
		String desc = null;
		StringBuilder sb = new StringBuilder(config.getWsBaseUrl())
				.append(reactomeClass.name()).append('/')
				.append(reactomeId);
		InputStream is = null;
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			ReactomeWsHandler handler = new ReactomeWsHandler();
			xr.setContentHandler(handler);
			// First we get the reaction or pathway:
			URL url = new URL(sb.toString());
			URLConnection urlCon = config.getUseProxy()?
					url.openConnection():
					url.openConnection(Proxy.NO_PROXY);
			urlCon.setRequestProperty("Accept", "application/xml");
			is = urlCon.getInputStream();
			InputSource inputSource = new InputSource(is);
			xr.parse(inputSource);
			// The handler contains now the summation db id:
			sb = new StringBuilder(config.getWsBaseUrl())
					.append(ReactomeClass.Summation.name()).append('/')
					.append(handler.getSummationDbid());
			url = new URL(sb.toString());
			urlCon = config.getUseProxy()?
					url.openConnection():
					url.openConnection(Proxy.NO_PROXY);
			urlCon.setRequestProperty("Accept", "application/xml");
			is = urlCon.getInputStream();
			inputSource = new InputSource(is);
			xr.parse(inputSource);
			desc = handler.getSummationText();
		} catch (MalformedURLException e) {
			throw new ReactomeConnectionException(sb.toString(), e);
		} catch (IOException e) {
			throw new ReactomeConnectionException(sb.toString(), e);
		} catch (SAXException e) {
			throw new ReactomeFetchDataException(e);
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
		return desc;
	}
	
}

package uk.ac.ebi.ep.adapter.bioportal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import uk.ac.ebi.biobabel.util.xml.XPathSAXHandler;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Entity;

public class BioportalWsAdapter implements IBioportalAdapter {

	private static final Logger LOGGER =
			Logger.getLogger(BioportalWsAdapter.class);
	
	public static enum BioportalOntology {
		EFO("1136"),
		OMIM("1348");
		private String id;
		private BioportalOntology(String id){ this.id = id; }
		public String getId(){ return id; }
	}

	/* Interesting XPaths from the search */
	
	private static final String SEARCH_PREFERREDNAME =
			"//success/data/page/contents/searchResultList/searchBean/preferredName";
	private static final String SEARCH_CONCEPTID =
			"//success/data/page/contents/searchResultList/searchBean/conceptId";
	private static final String SEARCH_CONCEPTIDSHORT =
			"//success/data/page/contents/searchResultList/searchBean/conceptIdShort";
	private static final String SEARCH_ONTOLOGYVERSIONID =
			"//success/data/page/contents/searchResultList/searchBean/ontologyVersionId";

	/* Interesting XPaths from the get */
	
	private static final String GET_LABEL = "/success/data/classBean/label";
	private static final String GET_DEFINITION =
			"//success/data/classBean/definitions/string";
	private static final String GET_FULLID = "/success/data/classBean/fullId";
	
	private BioportalConfig config;

    public BioportalWsAdapter() {
        config = new BioportalConfig();
    }
        
        
	
	/**
	 * Searches BioPortal for a concept.
	 * @param ontology The ontology to be searched in BioPortal.
	 * @param query The text to search (a name, concept ID without ontology
	 * 		prefix...).
	 * @param clazz The type of the returned entity.
	 * @param complete query BioPortal again with the concept ID in order to get
	 * 		additional info? If <code>true</code>, the entity is completed (see
	 * 		{@link #completeEntity(Entity, String, String)}, otherwise it will
	 * 		contain just ID, name and URL.
	 * @return an Entity object of the specified type and the level of detail,
	 * 		or <code>null</code> if not found. Note that if the search returns
	 * 		more than one result, only the first one is considered (this fact
	 * 		will be logged, though).
	 * @throws BioportalAdapterException
	 */
	public Entity searchConcept(BioportalOntology ontology, String query,
			Class<? extends Entity> clazz, boolean complete)
	throws BioportalAdapterException{
		Entity entity = null;
		InputStream is = null;
		String urlString = null;
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			
			urlString = MessageFormat.format(
					config.getSearchUrl(), ontology.getId(),
					URLEncoder.encode(query, "UTF-8"), 1);
			URL url = new URL(urlString);
			URLConnection urlCon = config.getUseProxy()?
					url.openConnection():
					url.openConnection(Proxy.NO_PROXY);
			is = urlCon.getInputStream();
			InputSource inputSource = new InputSource(is);
			XPathSAXHandler handler = new XPathSAXHandler(
					SEARCH_CONCEPTIDSHORT,
					SEARCH_PREFERREDNAME,
					SEARCH_CONCEPTID,
					SEARCH_ONTOLOGYVERSIONID);
			xr.setContentHandler(handler);
			xr.parse(inputSource);
			
			Collection<String> conceptIds =
					handler.getResults().get(SEARCH_CONCEPTIDSHORT);
			if (conceptIds != null){
				if (conceptIds.size() > 1){
					LOGGER.warn("More than one concept found for: "
							+ query + " " + conceptIds);
				}
				final String conceptId = conceptIds.iterator().next();
 				entity = clazz.newInstance();
 				// Remove the ontology prefix:
 				entity.setId(conceptId.replaceFirst("^\\w+:", ""));
 				entity.setName(handler.getResults().get(SEARCH_PREFERREDNAME)
 						.iterator().next());
 				entity.setUrl(handler.getResults().get(SEARCH_CONCEPTID)
 						.iterator().next());

 				if (complete){
 	 				String ontologyVersionId = handler.getResults()
 							.get(SEARCH_ONTOLOGYVERSIONID).iterator().next();
 					completeEntity(entity, conceptId, ontologyVersionId);
 				}
			}
		} catch (MalformedURLException e) {
			throw new BioportalAdapterException(query, e);
		} catch (IOException e) {
			throw new BioportalAdapterException(query, e);
		} catch (SAXException e) {
			throw new BioportalAdapterException(query, e);
		} catch (InstantiationException e) {
			throw new BioportalAdapterException(query, e);
		} catch (IllegalAccessException e) {
			throw new BioportalAdapterException(query, e);
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
		return entity;
	}

	/**
	 * Completes an entity as much as possible by making another request to
	 * BioPortal in order to get additional info (currently, only definition).
	 * @param entity the Entity to complete.
	 * @param conceptId the ID of the concept in BioPortal (including the
	 * 		ontology prefix).
	 * @param ontologyVersionId the ontology version ID in BioPortal.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 */
	private void completeEntity(Entity entity, final String conceptId,
			String ontologyVersionId)
	throws IOException, SAXException {
		InputStream is = null;
		try {
			URL url = new URL(MessageFormat.format(config.getGetUrl(),
					ontologyVersionId, conceptId, 1));
			URLConnection urlCon = config.getUseProxy()?
					url.openConnection():
					url.openConnection(Proxy.NO_PROXY);
			is = urlCon.getInputStream();
			InputSource inputSource = new InputSource(is);
			XPathSAXHandler handler = new XPathSAXHandler(
					GET_LABEL, GET_DEFINITION, GET_FULLID);
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);
			xr.parse(inputSource);
			
			Collection<String> definitions =
					handler.getResults().get(GET_DEFINITION);
			if (definitions != null){
				entity.setDescription(definitions.iterator().next()); // FIXME: ALL OF THEM?
			}
		} finally {
			if (is != null){
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
			}
		}
	}
	
	public Disease getDiseaseByName(String name)
	throws BioportalAdapterException {
		return (Disease) searchConcept(BioportalOntology.EFO, name,
				Disease.class, true);
	}

	public void setConfig(BioportalConfig config) {
		this.config = config;
	}
	
}

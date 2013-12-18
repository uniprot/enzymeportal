package uk.ac.ebi.ep.adapter.bioportal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import uk.ac.ebi.biobabel.util.xml.XPathSAXHandler;
import uk.ac.ebi.ep.enzyme.model.Disease;
import uk.ac.ebi.ep.enzyme.model.Entity;

/**
 * Implementation of {@link IBioportalAdapter} using the
 * <a href="http://www.bioontology.org/wiki/index.php/NCBO_REST_services">NCBO
 * Bioportal web services</a> (XML).
 * @author rafa
 *
 */
public class BioportalWsAdapter implements IBioportalAdapter {

	private static final Logger LOGGER =
			Logger.getLogger(BioportalWsAdapter.class);

	/* Interesting XPaths from the search */
	
	private static final String SEARCH_CONCEPT_PREFNAME =
			"//class/collection/class/prefLabel";
	private static final String SEARCH_CONCEPT_ID =
	        "//class/collection/class/id";
	private static final String SEARCH_CONCEPT_URL =
	        "//class/collection/class/linksCollection/links/ui/@href";
	private static final String SEARCH_CONCEPT_ONTOLOGY =
	        "//class/collection/class/linksCollection/links/ontology/@href";
	
	/* Interesting XPaths from the get */
	
	private static final String GET_DEFINITION =
			"//class/definitionCollection/definition";
	
	private BioportalConfig config;

	/**
	 * Creates an instance of this proxy with a default configuration.
	 * @deprecated A default configuration lacks of api key, use the
	 * {@link #BioportalWsAdapter(BioportalConfig) other constructor} instead.
	 */
    public BioportalWsAdapter() {
        this(new BioportalConfig());
    }

    public BioportalWsAdapter(BioportalConfig config){
        this.config = config;
    }
    
    /**
     * Prepares a list of ontologies IDs as one parameter for BioPortal.
     * @param ontologies the ontologies to search.
     * @return a comma-separated String containing the ontologies IDs.
     * @deprecated The BioPortal WS v4.0 does not use ontologies IDs any more.
     */
    private String getOntologiesIds(BioportalOntology[] ontologies){
        StringBuilder sb = new StringBuilder();
        for (BioportalOntology ontology : ontologies) {
            if (sb.length() > 0) sb.append(',');
            sb.append(ontology.getId());
        }
        return sb.toString();
    }
    
    /**
     * Prepares a list of ontologies names as one parameter for BioPortal.
     * @param ontologies the ontologies to search.
     * @return a comma-separated String containing the ontologies names.
     */
    private String getOntologiesNames(BioportalOntology[] ontologies){
        StringBuilder sb = new StringBuilder();
        for (BioportalOntology ontology : ontologies) {
            if (sb.length() > 0) sb.append(',');
            sb.append(ontology.name());
        }
        return sb.toString();
    }

    /**
	 * Searches BioPortal for a concept in one ontology.
	 * @param ontology The ontology to be searched in BioPortal.
	 * @param query The text to search (a name, concept ID without ontology
	 * 		prefix...).
	 * @param clazz The type of the returned entity.
	 * @param complete query BioPortal again with the concept ID in order to get
	 * 		additional info? If <code>true</code>, the entity is completed (see
	 * 		{@link #completeEntity(Entity, String, String)}, otherwise it will
	 * 		contain just ID, name and URL.
	 * @return an Entity object of the specified type and the level of detail
     *      matching exactly the query,
	 * 		or <code>null</code> if not found. Note that if the search returns
	 * 		more than one result, only the first one is considered (this fact
	 * 		will be logged, though).
	 * @throws BioportalAdapterException
     */
    public Entity searchConcept(BioportalOntology ontology, String query,
			Class<? extends Entity> clazz, boolean complete)
	throws BioportalAdapterException{
        Entity entity = null;
        Collection<Entity> entities = searchConcept(
                new BioportalOntology[]{ ontology }, query, clazz, complete);
        if (entities != null) {
            entity = entities.iterator().next();
            if (entities.size() > 1){
                List<String> ids = new ArrayList<String>();
                for (Entity e : entities) {
                    ids.add(e.getId());
                }
                LOGGER.warn("More than one concept found for " + query
                        + " in " + ontology + ": " + ids);
            }
        }
        return entity;
    }

    /**
	 * Searches BioPortal for a concept in several ontologies.
	 * @param ontologies The ontologies to be searched in BioPortal.
	 * @param query The text to search (a name).
	 * @param clazz The type of the returned entity.
	 * @param complete query BioPortal again with the concept ID in order to get
	 * 		additional info? If <code>true</code>, the entity is completed (see
	 * 		{@link #completeEntity(Entity, String, String)}, otherwise it will
	 * 		contain just ID, name and URL.
	 * @return a collection of Entity objects of the specified type and the
     *      level of detail matching exactly the query, or <code>null</code>
     *      if none found.
	 * @throws BioportalAdapterException
	 */
	public Set<Entity> searchConcept(BioportalOntology[] ontologies,
            String query, Class<? extends Entity> clazz, boolean complete)
	throws BioportalAdapterException{
		Set<Entity> entities = null;
		InputStream is = null;
		String urlString = null;
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			
			urlString = MessageFormat.format(config.getSearchUrl(),
                    getOntologiesNames(ontologies),
                    URLEncoder.encode(query, "UTF-8"), config.getApiKey());
			LOGGER.debug("[BIOPORTAL URL] " + urlString);
			URLConnection urlCon = new URL(urlString).openConnection();
			urlCon.setReadTimeout(config.getTimeout());
			urlCon.setRequestProperty("Accept",
			        "application/rdf+xml,application/xml");
			is = urlCon.getInputStream();
			InputSource inputSource = new InputSource(is);
			XPathSAXHandler handler = new XPathSAXHandler(
                    SEARCH_CONCEPT_ID,
					SEARCH_CONCEPT_PREFNAME,
					SEARCH_CONCEPT_URL,
					SEARCH_CONCEPT_ONTOLOGY);
			xr.setContentHandler(handler);
			xr.parse(inputSource);
			
			Collection<String> conceptIds = handler.getResults()
                    .get(SEARCH_CONCEPT_ID);
			if (conceptIds != null){
                // the internal implementation of these collections in the
                // handler are Lists, so we can rely on their iterators:
                final Iterator<String> preferredNames = handler.getResults()
                        .get(SEARCH_CONCEPT_PREFNAME).iterator();
                final Iterator<String> urls = handler.getResults()
                        .get(SEARCH_CONCEPT_URL).iterator();
                final Iterator<String> ontoUrls = handler.getResults()
                        .get(SEARCH_CONCEPT_ONTOLOGY).iterator();
                for (String conceptId : conceptIds) {
                    String preferredName = preferredNames.next();
                    String url = urls.next();
                    String ontoUrl = ontoUrls.next(); // a complete URI
                    // Workaround for obsolete EFO entries:
                    if (conceptId.contains("rdfns#pat_id_")) {
                        continue;
                    }
                    Entity entity = clazz.newInstance();
                    // Remove the ontology prefix:
                    entity.setId(conceptId.substring(
                            conceptId.lastIndexOf('/') + 1));
                    entity.setName(preferredName);
                    entity.setUrl(url);
                    if (complete) {
                        completeEntity(entity, conceptId, ontoUrl.substring(
                                ontoUrl.lastIndexOf('/') + 1));
                    }
                    if (entities == null) {
                        entities = new HashSet<Entity>();//new ArrayList<Entity>();
                    }
                    entities.add(entity);
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
		return entities;
	}

	/**
	 * Completes an entity as much as possible by making another request to
	 * BioPortal in order to get additional info (currently, only definition).
	 * @param entity the Entity to complete.
	 * @param conceptId the ID of the concept in BioPortal (whole URI).
	 * @param ontologyName the ontology name in BioPortal.
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SAXException
	 */
	private void completeEntity(Entity entity, final String conceptId,
			String ontologyName)
	throws IOException, SAXException {
		InputStream is = null;
		try {
			URL url = new URL(MessageFormat.format(config.getGetUrl(),
					ontologyName, URLEncoder.encode(conceptId, "UTF-8"),
					config.getApiKey()));
			LOGGER.debug("[BIOPORTAL URL] " + url);
			URLConnection urlCon = url.openConnection();
			urlCon.setReadTimeout(config.getTimeout());
            urlCon.setRequestProperty("Accept",
                    "application/rdf+xml,application/xml");
			urlCon.connect();
			is = urlCon.getInputStream();
			InputSource inputSource = new InputSource(is);
			XPathSAXHandler handler = new XPathSAXHandler(GET_DEFINITION);
			XMLReader xr = XMLReaderFactory.createXMLReader();
			xr.setContentHandler(handler);
			xr.parse(inputSource);
			
			Collection<String> definitions =
					handler.getResults().get(GET_DEFINITION);
			if (definitions != null){
				entity.setDescription(definitions.iterator().next());
                // XXX: get all definitions?
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

    public Disease getDisease(String nameOrId) throws BioportalAdapterException{
        Entity disease = null;
        Set<Entity> diseases = searchConcept(
                BioportalOntology.FOR_DISEASES, nameOrId, Disease.class, true);
        if (diseases != null){
            String others = null;
            for (Entity e : diseases) {
                if (disease == null) disease = e;
                else others += " ["+e.getId()+"]";
            }
            if (others != null){
                LOGGER.warn("More than one disease found for " + nameOrId
                        + ": " + others);
            }
            disease = diseases.iterator().next();
        }
        return (Disease) disease;
    }
    
	public void setConfig(BioportalConfig config) {
		this.config = config;
	}
	
}

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
import java.util.Iterator;
import java.util.List;
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
 * Bioportal web services</a>.
 * @author rafa
 *
 */
public class BioportalWsAdapter implements IBioportalAdapter {

	private static final Logger LOGGER =
			Logger.getLogger(BioportalWsAdapter.class);

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
     * Prepares a list of ontologies IDs as one parameter for BioPortal.
     * @param ontologies the ontologies to search.
     * @return a comma-separated String containing the ontologies IDs.
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
	 * @param query The text to search (a name, concept ID without ontology
	 * 		prefix...).
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
	public Collection<Entity> searchConcept(BioportalOntology[] ontologies,
            String query, Class<? extends Entity> clazz, boolean complete)
	throws BioportalAdapterException{
		Collection<Entity> entities = null;
		InputStream is = null;
		String urlString = null;
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			
			urlString = MessageFormat.format(config.getSearchUrl(),
                    getOntologiesIds(ontologies),
                    URLEncoder.encode(query, "UTF-8"), config.getApiKey(), 1);
			LOGGER.debug("[BIOPORTAL] URL=" + urlString);
			URL url = new URL(urlString);
			URLConnection urlCon = url.openConnection();
			urlCon.setReadTimeout(config.getTimeout());
			is = urlCon.getInputStream();
			InputSource inputSource = new InputSource(is);
			XPathSAXHandler handler = new XPathSAXHandler(
					SEARCH_CONCEPTIDSHORT,
					SEARCH_PREFERREDNAME,
					SEARCH_CONCEPTID,
					SEARCH_ONTOLOGYVERSIONID);
			xr.setContentHandler(handler);
			xr.parse(inputSource);
			
			Collection<String> conceptIds = handler.getResults()
                    .get(SEARCH_CONCEPTIDSHORT);
			if (conceptIds != null){
                entities = new ArrayList<Entity>();
                // the internal implementation of these collections in the
                // handler are Lists, so we can rely on their iterators:
                final Iterator<String> preferredNames = handler.getResults()
                        .get(SEARCH_PREFERREDNAME).iterator();
                final Iterator<String> urls = handler.getResults()
                        .get(SEARCH_CONCEPTID).iterator();
                final Iterator<String> versionIds = handler.getResults()
                                 .get(SEARCH_ONTOLOGYVERSIONID).iterator();
                for (String conceptId : conceptIds) {
                    Entity entity = clazz.newInstance();
                    // Remove the ontology prefix:
                    entity.setId(conceptId.replaceFirst("^\\w+:", ""));
                    entity.setName(preferredNames.next());
                    entity.setUrl(urls.next());
                    if (complete){
                        String ontologyVersionId = versionIds.next();
                        completeEntity(entity, conceptId, ontologyVersionId);
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
					ontologyVersionId, conceptId, config.getApiKey()));
			LOGGER.debug("[BIOPORTAL URL] " + url);
			URLConnection urlCon = url.openConnection();
			urlCon.setReadTimeout(config.getTimeout());
			urlCon.connect();
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
        Collection<Entity> diseases = searchConcept(
                BioportalOntology.FOR_DISEASES, nameOrId, Disease.class, true);
        if (diseases != null){
            String others = null;
            for (Entity e : diseases) {
                if (disease == null) disease = e;
                else others += " ["+e.getId()+"]";
            }
            if (others != null){
                LOGGER.warn("More than one disease found for " + nameOrId
                        + others);
            }
            disease = diseases.iterator().next();
        }
        return (Disease) disease;
    }

	public void setConfig(BioportalConfig config) {
		this.config = config;
	}
	
}

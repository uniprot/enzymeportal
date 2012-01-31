package uk.ac.ebi.ep.adapter.bioportal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
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

public class BioportalWsAdapter implements IBioportalAdapter {

	private static final Logger LOGGER =
			Logger.getLogger(BioportalWsAdapter.class);
	
	private static final String EFO_ID = "1136";

	/* Interesting XPaths from the search */
	
	private static final String SEARCH_PREFERREDNAME =
			"//success/data/page/contents/searchResultList/searchBean/preferredName";
	private static final String SEARCH_CONCEPTID =
			"//success/data/page/contents/searchResultList/searchBean/conceptId";
	private static final String SEARCH_CONCEPTIDSHORT =
			"//success/data/page/contents/searchResultList/searchBean/conceptIdShort";
	
	private BioportalConfig config;

	public Disease getDiseaseByName(String name)
	throws BioportalAdapterException {
		Disease disease = null;
		final String urlString = MessageFormat.format(
				config.getSearchUrl(), EFO_ID, name, 1);
		InputStream is = null;
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			XPathSAXHandler handler = new XPathSAXHandler(
					SEARCH_CONCEPTIDSHORT,
					SEARCH_CONCEPTID,
					SEARCH_PREFERREDNAME);
			xr.setContentHandler(handler);
			
			URL url = new URL(urlString);
			URLConnection urlCon = config.getUseProxy()?
					url.openConnection():
					url.openConnection(Proxy.NO_PROXY);
			is = urlCon.getInputStream();
			InputSource inputSource = new InputSource(is);
			xr.parse(inputSource);
			
			Collection<String> efoIds =
					handler.getResults().get(SEARCH_CONCEPTIDSHORT);
			if (efoIds != null){
				if (efoIds.size() > 1){
					LOGGER.warn("More than one disease found by name: "
							+ name + " " + efoIds);
				}
				disease = new Disease();
				// Remove the efo: prefix:
				disease.setId(efoIds.iterator().next().replace("efo:", ""));
				disease.setName(handler.getResults().get(SEARCH_PREFERREDNAME)
						.iterator().next());
				// XXX: xref to EFO as whole URL
				disease.withXrefs(Collections.singletonList(handler.getResults()
						.get(SEARCH_CONCEPTID).iterator().next()));
			}
		} catch (MalformedURLException e) {
			throw new BioportalAdapterException(urlString, e);
		} catch (IOException e) {
			throw new BioportalAdapterException(urlString, e);
		} catch (SAXException e) {
			throw new BioportalAdapterException(urlString, e);
		} finally {
			if (is != null)
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error(e);
				}
		}
		return disease;
	}

	public void setConfig(BioportalConfig config) {
		this.config = config;
	}
	
}

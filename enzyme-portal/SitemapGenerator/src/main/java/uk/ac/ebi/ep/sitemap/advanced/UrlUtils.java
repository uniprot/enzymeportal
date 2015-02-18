package uk.ac.ebi.ep.sitemap.advanced;

import java.util.HashMap;

public class UrlUtils {

	public static void checkUrl(String url, String baseUrl) {
		// Is there a better test to use here?
		if (!url.startsWith(baseUrl)) {
			throw new RuntimeException("Url " + url + " doesn't start with base URL " + baseUrl);
                    
	}
	}

	public static <K,V> HashMap<K,V> newHashMap() {
		return new HashMap<K,V>();
	}
	
}

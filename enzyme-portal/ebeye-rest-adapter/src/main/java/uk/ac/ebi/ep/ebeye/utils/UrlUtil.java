/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ebeye.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public final class UrlUtil {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UrlUtil.class);
    private static final String ENCODING = "UTF8";

    private UrlUtil() {
    }

    /**
     * Translates a string into {@code application/x-www-form-urlencoded} format
     * using a specific encoding scheme. This method uses the supplied encoding
     * scheme to obtain the bytes for unsafe characters.
     * <p>
     * <em><strong>Note:</strong> The <a href=
     * "http://www.w3.org/TR/html40/appendix/notes.html#non-ascii-chars">
     * World Wide Web Consortium Recommendation</a> states that UTF-8 should be
     * used. Not doing so may introduce incompatibilities.</em>
     *
     * @param term {@code String} to be translated. This uses the supported
     * <a href="../lang/package-summary.html#charenc">character encoding</a>.
     * @return the translated {@code String}.
     * @see URLDecoder#decode(java.lang.String, java.lang.String)
     * @since 1.0.2
     */
    public static String encode(String term) {

        String encodedData = term;
        try {
            encodedData = URLEncoder.encode(term, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
        }

        return encodedData;
    }

}

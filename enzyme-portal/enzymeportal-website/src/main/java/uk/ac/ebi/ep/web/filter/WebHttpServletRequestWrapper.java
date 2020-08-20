package uk.ac.ebi.ep.web.filter;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/**
 *
 * @author joseph
 */
@Slf4j
public class WebHttpServletRequestWrapper extends HttpServletRequestWrapper {
    
    public WebHttpServletRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }
    
    @Override
    public String[] getParameterValues(String parameter) {
        
        String[] values = super.getParameterValues(parameter);
        if (Objects.isNull(values)) {
            return null;
        }
        
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }
    
    @Override
    public String getParameter(String parameter) {
        
        String param = super.getParameter(parameter);
        if (Objects.isNull(param)) {
            return null;
        }
        
        return cleanXSS(param);
    }
    
    @Override
    public String getHeader(String name) {
        
        String header = super.getHeader(name);
        if (Objects.isNull(header)) {
            return null;
        }
        
        return cleanXSS(header);
    }
    
    private String cleanXSS(String text) {
        log.debug("Clean and escape text: " + text);
        return Jsoup.clean(StringEscapeUtils
                .escapeHtml4(text), Whitelist.basic());
        
    }
}

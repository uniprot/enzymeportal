package uk.ac.ebi.ep.adapter.reactome;
import java.util.Arrays;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @deprecated use XPathSAXHandler (biobabel-core-0.9.0) instead.
 * @author rafa
 *
 */
class ReactomeWsHandler extends DefaultHandler {
	
	private static final String SUMMATION_DBID = "/summation/dbId";
	private static final String SUMMATION_TEXT = "//summation/text";
	
	private Stack<String> currentContext = new Stack<String>();
	
	private boolean isSummationDbid;
	private boolean isSummationText;
	private String summationDbid;
	private String summationText;
	
	private StringBuilder currentChars = new StringBuilder();

	protected String getCurrentXpath() {
		StringBuilder xpath = new StringBuilder("/");
		for (String string : currentContext) {
			xpath.append('/').append(string);
		}
		return xpath.toString();
	}

	@Override
	public void startDocument() throws SAXException {
		currentContext.clear();
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		currentContext.push(localName);
		String currentXpath = getCurrentXpath();
		if (currentXpath.endsWith(SUMMATION_DBID)){
			isSummationDbid = true;
		} else if (currentXpath.equals(SUMMATION_TEXT)){
			isSummationText = true;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (isSummationDbid || isSummationText){
			currentChars.append(Arrays.copyOfRange(ch, start, start+length));
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (isSummationDbid){
			summationDbid = currentChars.toString();
		} else if (isSummationText){
			summationText = currentChars.toString();
		}
		currentContext.pop();
		isSummationDbid = false;
		isSummationText = false;
		if (currentChars.length() > 0){
			currentChars.delete(0, currentChars.length());
		}
	}

	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}

	public String getSummationDbid() {
		return summationDbid;
	}

	public String getSummationText() {
		return summationText;
	}

}

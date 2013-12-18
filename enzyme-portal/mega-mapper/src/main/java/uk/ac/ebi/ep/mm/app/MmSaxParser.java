package uk.ac.ebi.ep.mm.app;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import uk.ac.ebi.biobabel.util.db.OracleDatabaseInstance;
import uk.ac.ebi.ep.mm.MegaJdbcMapper;
import uk.ac.ebi.ep.mm.MegaLuceneMapper;
import uk.ac.ebi.ep.mm.MegaMapper;

/**
 * An abstract class with common implementation for MmParsers.
 * @author rafa
 * @since 1.0.16
 */
public abstract class MmSaxParser extends DefaultHandler implements MmParser {

    private static final Logger LOGGER = Logger.getLogger(MmSaxParser.class);

    /**
     * The current element (tree path) being parsed.
     */
    protected Stack<String> currentContext = new Stack<String>();
    /**
     * The text value of the current element being parsed.
     */
    protected StringBuilder currentChars = new StringBuilder();

    protected MegaMapper mm;

    /**
     * Method executed by main method of subclasses.
     * @param cl the command line options from a main method.
     * @param parser the actual implementation of parser to use.
     * @throws IOException in case of problem closing or rolling-back the
     *      mega-map.
     * @throws SQLException in case of problem with the database connection.
     */
    protected static void mainParse(CommandLine cl, MmParser parser)
    throws IOException, SQLException {
        MegaMapper mm = null;
        Connection con = null;
        try {
            if (cl.hasOption("indexDir")){
                mm = new MegaLuceneMapper(cl.getOptionValue("indexDir"));
            } else {
                final String dbConfig = cl.getOptionValue("dbConfig");
                con = OracleDatabaseInstance
                        .getInstance(dbConfig).getConnection();
                con.setAutoCommit(false);
                mm = new MegaJdbcMapper(con);
            }
            parser.setWriter(mm);
            parser.parse(cl.getOptionValue("file"));
            mm.commit();
        } catch (Exception e){
            if (mm != null) mm.rollback();
            LOGGER.error("Unable to parse", e);
        } finally {
            if (mm != null) mm.closeMap();
            if (con != null) con.close();
        }
    }

    public void setWriter(MegaMapper mmWriter){
        this.mm = mmWriter;
    }

    /**
     * Parses a XML file and indexes/stores cross-references in a mega-map.<br>
     * This method is not thread safe.
     * @param xmlFilePath the XML file to parse
     * @throws java.io.FileNotFoundException if the UniProt XML file is not found
     * 		or not readable.
     * @throws org.xml.sax.SAXException if no default XMLReader can be found or
     * 		instantiated, or exception during parsing.
     * @throws java.io.IOException if the lucene index cannot be opened/created,
     * 		or from the parser.
     */
    public void parse(String xmlFilePath) throws Exception {
        File xmlFile = new File(xmlFilePath);
        parse(new FileInputStream(xmlFile));
    }

    public void parse(InputStream is) throws IOException, SAXException {
        if (mm == null){
            // Don't go ahead:
            throw new NullPointerException("A MegaMapper must be configured");
        }
        LOGGER.info("Mega-map open to import entries/xrefs");
        try {
            mm.openMap();
            XMLReader xr = XMLReaderFactory.createXMLReader();
            xr.setContentHandler(this);
            xr.setErrorHandler(this);
            InputSource source = new InputSource(is);
            LOGGER.info("Parsing start");
            xr.parse(source);
            LOGGER.info("Parsing end");
            mm.closeMap();
            LOGGER.info("Map closed");
        } catch (IOException e){
            LOGGER.error("During parsing", e);
            mm.handleError();
            throw e;
        } catch (SAXException e){
            LOGGER.error("During parsing", e);
            mm.handleError();
            throw e;
        }
    }

    protected String getCurrentXpath() {
        StringBuilder xpath = new StringBuilder("/");
        for (String string : currentContext) {
            xpath.append('/').append(string);
        }
        return xpath.toString();
    }
}

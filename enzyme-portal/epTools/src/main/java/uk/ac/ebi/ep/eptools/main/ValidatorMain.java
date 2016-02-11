/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.eptools.main;

//import static uk.ac.ebi.ep.eptools.xml.EnzymePortalXmlValidator.validateXmlAgainstXsds;
import static java.lang.System.out;
import java.util.Arrays;
import uk.ac.ebi.ep.eptools.xml.EnzymePortalXmlValidator;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
public class ValidatorMain {

    /**
     * Validates provided XML against provided XSD.
     *
     * @param arguments XML file to be validated and XSD files against which it the XML should be validated.
     */
    public static void main(final String[] arguments) {

        if (arguments.length < 2) {
            out.println("\nUSAGE: EnzymePortalXmlValidator <xmlFile> <xsdFile1> ... <xsdFileN>\n");
            out.println("\tNOte: place XSDs according to their dependencies ...");
           
            System.exit(-1);
        }

        final String[] schemas = Arrays.copyOfRange(arguments, 1, arguments.length);
        EnzymePortalXmlValidator.validateXml(arguments[0], schemas);

    }
}

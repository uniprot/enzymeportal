/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.core.search;

import uk.ac.ebi.xchars.SpecialCharacters;
import uk.ac.ebi.xchars.domain.EncodingType;

/**
 *
 * @author joseph
 */
public class SpecialCharacterImpl {
    
  

	public static void main(String[] argsa){
            
            String data = "3'5'-cyclic GMP(1&#8722;)";
             //String[] acc = data_raw.split(",");
        //String data = acc[0].concat("_%");

     //String data = data_raw.replaceAll(",", "");
 
            
            
            
           
//		if (args.length == 0){
//			System.err.println("Nothing to translate. Bye!");
//			System.exit(1);
//		}
		SpecialCharacters xchars = SpecialCharacters.getInstance(null);
		EncodingType[] encodings = {
				EncodingType.CHEBI_CODE,
				EncodingType.COMPOSED,
				EncodingType.EXTENDED_HTML,
				EncodingType.GIF,
				EncodingType.HTML,
				EncodingType.HTML_CODE,
				EncodingType.JPG,
				EncodingType.SWISSPROT_CODE,
				EncodingType.UNICODE
		};
		System.out.println("Original String: " + data);
		if (!xchars.validate(data)){
			System.err.println("This is not a valid xchars string!");
			System.exit(2);
		}

		System.out.println("Default encoding:\t" + xchars.xml2Display(data));

		for (int i = 0; i < encodings.length; i++) {
			System.out.println(encodings[i].toString());
			System.out.println('\t' + xchars.xml2Display(data, encodings[i]));
		}
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web.utils;

import org.springframework.stereotype.Service;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Service
public class SearchUtil {

    private static final String VALID_EC_REGEX = "[1-6](\\.(\\-|\\d{1,2})){3}n*";

    public boolean validateEc(String ec) {
        boolean isValid = false;
        if (ec.matches(VALID_EC_REGEX)) {

            String[] digits = ec.split("\\.");
            boolean invalid = digits[1].equals("-") && !digits[2].equals("-") && !digits[3].equals("-")
                    || digits[2].equals("-") && !digits[3].equals("-");

            isValid = !invalid;
        }
        return isValid;

    }

    public String transformIncompleteEc(String ec) {

        String[] digits = ec.split("\\.");
   
        if (digits[0].equals("-")) {
            return ec;
        } else if (digits[1].equals("-")) {
           
            return ec;
        } else if (digits[2].equals("-")) {
            ec = digits[0] + "." + digits[1] + ".*";
            return ec;
        } else if (digits[3].equals("-")) {
            ec = digits[0] + "." + digits[1] + "." + digits[2] + ".*";
            return ec;
        }

        return ec;

    }
//
//    public static void main(String[] args) {
//        System.out.println("ok ");
//
//        String ec = "1.1.1.1";
//        boolean valid = validateEc(ec);
//        System.out.println("VALID " + valid);
//
//        boolean valid2 = validateEc("6.2.-.-");
//        System.out.println("VALID2 " + valid2);
//        String t = transformIncompleteEc("1.1.1.1");
//        System.out.println("T " + t);
//
//        String t1 = transformIncompleteEc("1.1.1.-");
//        System.out.println("T1 " + t1);
//
//        String t2 = transformIncompleteEc("1.1.-.-");
//        System.out.println("T2 " + t2);
//
//        String t3 = transformIncompleteEc("1.-.-.-");
//        System.out.println("T3 " + t3);
//
//        String t4 = transformIncompleteEc("-.-.-.-");
//        System.out.println("T4 " + t4);
//    }
}

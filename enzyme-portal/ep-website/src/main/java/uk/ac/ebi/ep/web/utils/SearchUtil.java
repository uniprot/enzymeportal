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
}

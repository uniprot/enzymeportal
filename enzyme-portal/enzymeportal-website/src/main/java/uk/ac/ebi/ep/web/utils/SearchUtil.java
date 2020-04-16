package uk.ac.ebi.ep.web.utils;

import org.springframework.stereotype.Service;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@Service
public final class SearchUtil {

    private SearchUtil() {
    }

    //private static final String VALID_EC_REGEX = "[1-7](\\.(\\-|\\d{1,4})){3}n*";
    private static final String VALID_EC_REGEX = "([1-7](\\.n?[\\d\\-]+){3})";

    public static String escape(String term) {
        return "\"" + term + "\"";
    }

    public static boolean validateEc(String ec) {
        boolean isValid = false;

        if (ec.matches(VALID_EC_REGEX)) {

            String[] digits = ec.split("\\.");
            boolean invalid = digits[1].equals("-") && !digits[2].equals("-") && !digits[3].equals("-")
                    || digits[2].equals("-") && !digits[3].equals("-");

            isValid = !invalid;
        }
        return isValid;

    }

    public static String transformIncompleteEc(String ec) {

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

    public static String capitalizeFirstLetter(String original) {
        if (original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }

}

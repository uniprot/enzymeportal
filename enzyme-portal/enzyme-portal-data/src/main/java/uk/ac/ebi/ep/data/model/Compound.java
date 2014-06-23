/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.ep.data.model;

/**
 *
 * @author joseph
 */
public class Compound {
        protected Compound.Role role;
    protected boolean selected;
    
    
        public enum Role {

        SUBSTRATE_OR_PRODUCT,
        COFACTOR,
        ACTIVATOR,
        INHIBITOR,
        DRUG,
        BIOACTIVE;

        public String value() {
            return name();
        }

        public static Compound.Role fromValue(String v) {
            return valueOf(v);
        }

    }
}

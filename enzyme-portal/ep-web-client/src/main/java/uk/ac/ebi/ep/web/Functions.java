/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.web;

import java.util.List;

/**
 *Due to no similar  functionality in JSTL, this function was designed to enable in checking if an item is contained in a list.
 * @author joseph
 */


public final class Functions {

    private Functions() {
        // Hiden constructor.
    }

    public static boolean contains(List collection, Object item) {
        return collection.contains(item);
    }
    public static boolean alphaOmegaIsNotNull(Object alpha, Object omega){
        boolean eval = true;
        if( alpha == null | omega == null){
            
                    eval = false;
        }
 
        return eval;
        
    }
    
    public static boolean omegaIsNull(Object alpha, Object omega){
        boolean eval = false;
        if(alpha == null){
         
            eval = false;
        }
        
        if(alpha != null && omega == null){
          
            eval = true;
        }
        
        if((alpha != null & !alpha.equals("") & !alpha.equals(" ")) && (omega == null || omega.equals("") || omega.equals(" "))){
          
            eval = true;
        }
        
   
        
        return eval;
    }
    
     

}
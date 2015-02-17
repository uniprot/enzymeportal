/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uk.ac.ebi.ep.pdbeadapter.PDB;
import uk.ac.ebi.ep.pdbeadapter.PdbService;

/**
 *
 * @author joseph
 */
@Controller
public class PDBeRestController {
    
    private final Logger LOGGER = Logger.getLogger(PDBeRestController.class);

    @Autowired
    private PdbService pdbService;
    
    @RequestMapping(value = "/ajax/pdbe/{pdbId}")
    protected  String getStructure(Model model, @PathVariable String pdbId) {
        String retValue = "pdb";
      
     
        PDB structure = pdbService.computeProteinStructure(pdbId.toLowerCase());
        
        
         model.addAttribute("proteinStructure", structure);


        return retValue;
    }
    
    

}



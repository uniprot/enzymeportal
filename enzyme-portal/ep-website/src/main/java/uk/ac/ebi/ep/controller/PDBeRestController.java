package uk.ac.ebi.ep.controller;

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

    @Autowired
    private PdbService pdbService;

    @RequestMapping(value = "/ajax/pdbe/{pdbId}")
    protected String getStructure(Model model, @PathVariable String pdbId) {
        String retValue = "pdbe";
        // PDB structure = pdbService.computeProteinStructure(pdbId.toLowerCase());

        PDB structure = pdbService.findProteinStructure(pdbId.toLowerCase());
        model.addAttribute("proteinStructure", structure);

        return retValue;
    }

//    public static void main(String[] args) {
//        ApplicationContext context = new AnnotationConfigApplicationContext(PDBeRestController.class);
//
//        PdbService pdbService = context.getBean(PdbService.class);
//        String pdbId = "5hht";
//        PDB structure = pdbService.findProteinStructure(pdbId.toLowerCase());
//        System.out.println("All "+ structure);
//        System.out.println("DATA " + structure.getCofactors());
//        System.out.println("DATAAAA " + structure.getLigands());
//    }
}

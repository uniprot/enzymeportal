/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 *
 * @author joseph
 */
@Controller
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private static final String HOME = "/index";

//    @RequestMapping(value = {HOME, "/"}, method = RequestMethod.GET)
//    public String showHomePage(Model model) {
//        LOGGER.debug("Rendering Home page");
//
//        System.out.println("home page called ");
//
//        return HOME;
//    }

}

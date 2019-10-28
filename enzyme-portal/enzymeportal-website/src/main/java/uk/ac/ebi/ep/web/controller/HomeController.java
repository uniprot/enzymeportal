package uk.ac.ebi.ep.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author joseph
 */
@Controller
public class HomeController {

    @GetMapping("/")
    private String home() {
        return "index";
    }

    @GetMapping("/about")
    private String about() {
        return "about";
    }

    @GetMapping("/faq")
    private String faq() {
        return "faq";
    }

}

package uk.ac.ebi.ep.web.controller;

import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import uk.ac.ebi.ep.web.utils.ServerProperties;

/**
 *
 * @author joseph
 */
@Controller
public class ApiController {

    private static final String API = "api";
    private final ServerProperties serverProperties;

    @Autowired
    public ApiController(@NotNull ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @GetMapping("/api")
    public String api(Model model) {
        model.addAttribute("apiUrl", serverProperties.getServerUrl());
        return API;
    }

}

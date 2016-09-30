/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
@Controller
public class StatusController extends AbstractController {
 private static final Logger logger = Logger.getLogger(StatusController.class);

    private static final String STATUS = "status";
    private static final String STAT = "stat";

    @RequestMapping(value = "/service/status", method = RequestMethod.GET)
    public String formattedStatCheck(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);
        stopWatch.stop();
        logger.info("Status Check took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");
        if (entry == null) {
            status = "DOWN";
        }

        model.addAttribute("status", status);
        request.setAttribute("status", status);

        return STATUS;
    }

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    public String statusCheck(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);

        stopWatch.stop();
        logger.info("Status Check took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");
        if (entry == null) {
            status = "DOWN";
        }

        model.addAttribute("status", status);
        request.setAttribute("status", status);

        return STAT;
    }

    @ResponseBody
    @RequestMapping(value = "/status/json", method = RequestMethod.GET)
    public String statusCheckJson(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);

        stopWatch.stop();
        logger.info("Status Check .json took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");

        if (entry == null) {
            status = "DOWN";
        }

        model.addAttribute("status", status);
        request.setAttribute("status", status);

        return status;
    }

    @RequestMapping(value = "/status/text", method = RequestMethod.GET)
    public void statusCheckTextFile(HttpServletResponse response, Model model, HttpServletRequest request, RedirectAttributes attributes)
            throws IOException {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);
        stopWatch.stop();
        logger.info("Status Check .txt took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");

        if (entry == null) {
            status = "DOWN";
        }

        model.addAttribute("status", status);
        request.setAttribute("status", status);

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=status.txt");
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println(status);
            out.flush();
        }
    }
}

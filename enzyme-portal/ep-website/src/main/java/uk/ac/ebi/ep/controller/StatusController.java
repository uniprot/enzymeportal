/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import static uk.ac.ebi.ep.controller.AbstractController.LOGGER;
import uk.ac.ebi.ep.data.domain.UniprotEntry;

/**
 *
 * @author joseph
 */
@Controller
public class StatusController extends AbstractController {

    private static final String STATUS = "status";
    private static final String STAT = "stat";

    @RequestMapping(value = "/service/status", method = RequestMethod.GET)
    public String formattedStatCheck(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        long startTime = System.nanoTime();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
        LOGGER.info("Status Check took :  (" + elapsedtime + " sec)");
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
        long startTime = System.nanoTime();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
        LOGGER.info("Status Check took :  (" + elapsedtime + " sec)");
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
        long startTime = System.nanoTime();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
        LOGGER.info("Status Check .json took :  (" + elapsedtime + " sec)");

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
        long startTime = System.nanoTime();

        String accession = "O76074";
        UniprotEntry entry = enzymePortalService.findByUniprotAccession(accession);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        long elapsedtime = TimeUnit.SECONDS.convert(duration, TimeUnit.NANOSECONDS);
        LOGGER.info("Status Check .txt took :  (" + elapsedtime + " sec)");

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

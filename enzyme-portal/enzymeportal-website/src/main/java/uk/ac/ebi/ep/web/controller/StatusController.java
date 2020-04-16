package uk.ac.ebi.ep.web.controller;

import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uk.ac.ebi.ep.dataservice.dto.ProteinView;
import uk.ac.ebi.ep.dataservice.service.DataService;

/**
 *
 * @author joseph
 */
@Slf4j
@Controller
public class StatusController {

    private static final String STATUS = "status";
    private static final String STAT = "stat";
    private static final String ACCESSION = "O76074";

    private final DataService dataService;

    @Autowired
    public StatusController(DataService dataService) {
        this.dataService = dataService;
    }

    private void processDataForView(ProteinView entry, Model model, HttpServletRequest request, String status) {
        if (entry == null) {
            status = "DOWN";
        }
        model.addAttribute(STATUS, status);
        request.setAttribute(STATUS, status);
    }

    @GetMapping(value = "/service/status")
    public String formattedStatCheck(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ProteinView entry = dataService.findProteinViewByAccession(ACCESSION);
        stopWatch.stop();
        log.debug("formattedStatCheck took :  (" + stopWatch.getTotalTimeSeconds() + " seconds)");
        processDataForView(entry, model, request, status);

        return STATUS;
    }

    @GetMapping(value = "/status")
    public String statusCheck(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ProteinView entry = dataService.findProteinViewByAccession(ACCESSION);

        stopWatch.stop();
        log.debug("statusCheck took :  (" + stopWatch.getTotalTimeSeconds() + " secs)");
        processDataForView(entry, model, request, status);

        return STAT;
    }

    @ResponseBody
    @GetMapping(value = "/status/json")
    public String statusCheckJson(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ProteinView entry = dataService.findProteinViewByAccession(ACCESSION);

        stopWatch.stop();
        log.debug("statusCheckJson took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");

        processDataForView(entry, model, request, status);

        return status;
    }

    @GetMapping(value = "/status/text")
    public void statusCheckTextFile(HttpServletResponse response, Model model, HttpServletRequest request, RedirectAttributes attributes)
            throws IOException {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        ProteinView entry = dataService.findProteinViewByAccession(ACCESSION);
        stopWatch.stop();
        log.debug("statusCheckTextFile took :  (" + stopWatch.getTotalTimeSeconds() + " seconds)");

        processDataForView(entry, model, request, status);

        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=status.txt");
        try (ServletOutputStream out = response.getOutputStream()) {
            out.println(status);
            out.flush();
        }
    }
}

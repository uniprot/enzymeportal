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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    private final DataService dataService;

    @Autowired
    public StatusController(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping(value = "/service/status", method = RequestMethod.GET)
    public String formattedStatCheck(Model model, HttpServletRequest request, RedirectAttributes attributes) {

        String status = "UP";
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String accession = "O76074";
        ProteinView entry = dataService.findProteinViewByAccession(accession);
        stopWatch.stop();
        log.debug("Status Check took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");
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
        ProteinView entry = dataService.findProteinViewByAccession(accession);

        stopWatch.stop();
        log.debug("Status Check took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");
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
        ProteinView entry = dataService.findProteinViewByAccession(accession);

        stopWatch.stop();
        log.debug("Status Check .json took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");

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
        ProteinView entry = dataService.findProteinViewByAccession(accession);
        stopWatch.stop();
        log.debug("Status Check .txt took :  (" + stopWatch.getTotalTimeSeconds() + " sec)");

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

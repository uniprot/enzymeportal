package uk.ac.ebi.ep.web.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.ac.ebi.ep.dataservice.dto.WebComponentDto;
import uk.ac.ebi.ep.dataservice.dto.WebStatXrefDto;
import uk.ac.ebi.ep.dataservice.entities.WebStatXref;
import uk.ac.ebi.ep.dataservice.service.WebStatComponentService;
import uk.ac.ebi.ep.dataservice.service.WebStatService;
import uk.ac.ebi.ep.web.model.WebStat;

/**
 *
 * @author joseph
 */
@Slf4j
@Controller
public class ReleaseStatController {

    private static final String WEBSTAT = "website-statistics";

    private final WebStatService webStatService;
    private final WebStatComponentService webStatComponentService;

    @Autowired
    public ReleaseStatController(WebStatService webStatService, WebStatComponentService webStatComponentService) {
        this.webStatService = webStatService;
        this.webStatComponentService = webStatComponentService;
    }

    @GetMapping(value = "/statistics")
    public String showReleaseStatPage(Model model) {

        List<String> category = webStatService.findReleaseMonths();

        WebStatXrefDto xrefDto = webStatService.findCurrentRelease();
        WebComponentDto componentDto = webStatComponentService.findCurrentRelease();

        WebStat s = new WebStat();

        s.setChebi(xrefDto.getChebi());
        s.setChembl(xrefDto.getChembl());
        s.setIntenz(xrefDto.getIntenz());
        s.setKegg(xrefDto.getKegg());
        s.setMetabolights(xrefDto.getMetabolights());
        s.setOmim(xrefDto.getOmim());
        s.setPdb(xrefDto.getPdbe());
        s.setReactome(xrefDto.getReactome());
        s.setRhea(xrefDto.getRhea());
        s.setUniprot(xrefDto.getUniprot());

        s.setProteinStructure(componentDto.getProteinStructure());
        s.setActivators(componentDto.getActivators());
        s.setCatalyticActivities(componentDto.getCatalyticActivities());
        s.setDisease(componentDto.getDiseases());
        s.setPathways(componentDto.getPathways());
        s.setRheaReaction(componentDto.getReactions());
        s.setCofactors(componentDto.getCofactors());
        s.setInhibitors(componentDto.getInhibitors());
        s.setMetabolites(componentDto.getMetabolites());
        s.setExpEvidence(componentDto.getExpEvidence());
        s.setReviewed(componentDto.getReviewed());
        s.setUnreviewed(componentDto.getUnreviewed());

        model.addAttribute("webStat", s);

        model.addAttribute("month", xrefDto.getMonthFullName());
        model.addAttribute("year", xrefDto.getYear());
        model.addAttribute("category", category);
        return WEBSTAT;
    }

    @ResponseBody
    @GetMapping(value = "/service/releases")
    public WebStatXrefDto getReleaseStatJson() {
       return  webStatService.findLatestRelease();
    }

    @ResponseBody
    @GetMapping(value = "/service/releases/list")
    public List<WebStatXref> getReleaseStatJsonArray() {
        Sort sort = Sort.by("releaseDate").ascending();
        Pageable pageable = PageRequest.of(0, 6, sort);
        return webStatService.findStatisticsInLast3Months(pageable).getContent();

    }

    @ResponseBody
    @GetMapping(value = "/service/releases/component")
    public WebComponentDto getReleaseStatComponentsJson() {

        return webStatComponentService.findLatestRelease();
    }

    @ResponseBody
    @GetMapping(value = "/service/releases/component/list")
    public List<WebComponentDto> getReleaseStatComponentsJsonArray() {

        return webStatComponentService.findLast3MonthsStatistics();
    }

}

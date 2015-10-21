/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import uk.ac.ebi.ep.data.search.model.SearchModel;
import uk.ac.ebi.ep.data.search.model.SearchParams;

/**
 *
 * @author joseph
 */
@ControllerAdvice
public class GlobalControllerAdvisor {

    private static final Logger logger = Logger.getLogger(GlobalControllerAdvisor.class);

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex) {
        logger.error("Exception Occured while requesting this url = " + request.getRequestURL() + " : exception is :: " + ex);
        return "error";
    }

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(HttpServletRequest request, Exception ex) {
        logger.error("SQLException Occured:: URL=" + request.getRequestURL() + " :: " + ex);
        return "error";
    }

//    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
//    @ExceptionHandler(IOException.class)
//    public void handleIOException(){
//        logger.error("IOException handler executed");
//       
//    }
    @ModelAttribute(value = "searchModel")
    public void addAttributes(HttpServletRequest request, Model model) {

        model.addAttribute("searchModel", newEmptySearchModel());
        request.setAttribute("searchModel", newEmptySearchModel());

    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        //webDataBinder.setBindEmptyMultipartFiles(false);configure later
    }

    private SearchModel newEmptySearchModel() {
        SearchModel searchModelForm = new SearchModel();
        SearchParams searchParams = new SearchParams();
        searchParams.setStart(0);
        searchParams.setType(SearchParams.SearchType.KEYWORD);//default
        searchParams.setPrevioustext("");
        searchModelForm.setSearchparams(searchParams);
        return searchModelForm;
    }

    //    @RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
//@ResponseBody
//public FileSystemResource getFile(@PathVariable("file_name") String fileName) {
//    return new FileSystemResource(myService.getFileFor(fileName)); 
//}
}

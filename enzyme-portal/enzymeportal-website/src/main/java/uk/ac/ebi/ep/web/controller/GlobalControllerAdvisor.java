package uk.ac.ebi.ep.web.controller;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


/**
 *
 * @author joseph
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvisor {

    @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex) {
        log.error("Exception Occured while requesting this url = " + request.getRequestURL() + " : exception is :: " + ex);
        request.setAttribute("errorParam", request.getRequestURI());
        return "error";
    }

    @ExceptionHandler(SQLException.class)
    public String handleSQLException(HttpServletRequest request, Exception ex) {
        log.error("SQLException Occured:: URL=" + request.getRequestURL() + " :: " + ex);
        return "error";
    }

}

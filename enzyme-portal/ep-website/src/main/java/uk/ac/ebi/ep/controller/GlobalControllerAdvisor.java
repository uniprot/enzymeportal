/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.controller;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 *
 * @author joseph
 */
@ControllerAdvice
public class GlobalControllerAdvisor {
    
    private static final Logger logger = Logger.getLogger(GlobalControllerAdvisor.class);
     
       @ExceptionHandler(Exception.class)
    public String handleException(HttpServletRequest request, Exception ex){
        logger.info("Exception Occured during this reques = "+request.getRequestURL());
        return "error";
    } 
    
    @ExceptionHandler(SQLException.class)
    public String handleSQLException(HttpServletRequest request, Exception ex){
        logger.info("SQLException Occured:: URL="+request.getRequestURL());
        return "error";
    }
     
//    @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="IOException occured")
//    @ExceptionHandler(IOException.class)
//    public void handleIOException(){
//        logger.error("IOException handler executed");
//       
//    }
}

package uk.ac.ebi.ep.restapi.exceptions;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author joseph
 */
@Slf4j
@ControllerAdvice
public class ApiResponseEntityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDescriptor> resourceNotFoundHandler(ResourceNotFoundException ex) {
        ExceptionDescriptor descriptor = ExceptionDescriptor.builder()
                .description(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(descriptor, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ExceptionDescriptor> invalidInputHandler(InvalidInputException ex) {
        ExceptionDescriptor descriptor = ExceptionDescriptor.builder()
                .description(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(descriptor, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDescriptor> ConnversionFailedHandler(RuntimeException ex) {
        ExceptionDescriptor descriptor = ExceptionDescriptor.builder()
                .description(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(descriptor, HttpStatus.BAD_REQUEST);
    }
    

    
        @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
    public ResponseEntity<ExceptionDescriptor> HttpMediaTypeNotSupportedHandler(HttpMediaTypeNotAcceptableException ex) {
      
        String acceptable = "Acceptable Media types: " + MediaType.toString(ex.getSupportedMediaTypes());

        ExceptionDescriptor descriptor = ExceptionDescriptor.builder()
                .description(String.format("%s", acceptable))
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(descriptor, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ResponseEntity<ExceptionDescriptor> HttpMediaTypeNotSupportedHandler(HttpMediaTypeNotSupportedException ex) {
        String unsupported = "Unsupported content type: " + ex.getContentType();
        String supported = "Supported content types: " + MediaType.toString(ex.getSupportedMediaTypes());

        ExceptionDescriptor descriptor = ExceptionDescriptor.builder()
                .description(String.format("%s.%s", unsupported, supported))
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(descriptor, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionDescriptor> HttpMessageNotReadableHandler(HttpMessageNotReadableException ex) {
        ExceptionDescriptor descriptor = ExceptionDescriptor.builder()
                .description(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity(descriptor, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDescriptor> internalServerErrorHandler(final Throwable e) {

        String message = "API internal server problem.";

        ExceptionDescriptor descriptor = ExceptionDescriptor.builder()
                .description(message)
                .timestamp(LocalDateTime.now())
                .build();

        UUID uuid = UUID.randomUUID();
        String errorRef = uuid.toString();
        log.error("errorRef=" + errorRef, message, e);

        return new ResponseEntity(descriptor, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

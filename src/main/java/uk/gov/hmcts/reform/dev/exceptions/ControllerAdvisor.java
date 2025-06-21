package uk.gov.hmcts.reform.dev.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import uk.gov.hmcts.reform.dev.models.ErrorObject;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> invalidRequest(
        InvalidRequestException ex, WebRequest request) {
        ErrorObject errorObject = getErrorObject(ex);
        return new ResponseEntity<>(errorObject.getError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> notFoundException(EntityNotFoundException ex) {
        ErrorObject errorObject = getErrorObject(ex);
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> badRequestException(IllegalArgumentException ex) {
        ErrorObject errorObject = getErrorObject(ex);
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptions(Exception ex) {
        ErrorObject errorObject = getErrorObject(ex);
        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static ErrorObject getErrorObject(Exception ex) {
        ErrorObject errorObject = new ErrorObject();
        errorObject.addError("timestamp", LocalDateTime.now());
        errorObject.addError("message", ex.getMessage());
        return errorObject;
    }
}

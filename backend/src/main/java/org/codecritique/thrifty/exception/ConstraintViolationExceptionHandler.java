package org.codecritique.thrifty.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ConstraintViolationExceptionHandler {

    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleConstraintViolationException(javax.validation.ConstraintViolationException e) {
        Map<String, String> errors = new HashMap<>();
        for (javax.validation.ConstraintViolation<?> cv : e.getConstraintViolations()) {
            String property = cv.getPropertyPath().toString();
            String error = cv.getMessage();
            errors.put(property, error);
        }
        return errors;
    }

    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(org.hibernate.exception.ConstraintViolationException e) {
        return e.getMessage() + " : unique index or primary key violation";
    }

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolationException(org.springframework.dao.DataIntegrityViolationException e) {
        if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
            return e.getCause().getMessage() + " : unique index or primary key violation";
        throw e;
    }

}
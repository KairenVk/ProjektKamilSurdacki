package com.tss.advices;

import com.tss.exceptions.EntityByNameNotFoundException;
import com.tss.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EntityNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String entityNotFoundHandler(EntityNotFoundException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(EntityByNameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String entityByNameNotFoundHandler(EntityByNameNotFoundException ex) {
        return ex.getMessage();
    }
}

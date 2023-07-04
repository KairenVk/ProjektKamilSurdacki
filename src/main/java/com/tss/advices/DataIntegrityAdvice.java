package com.tss.advices;

import com.tss.exceptions.MissingParameterException;
import com.tss.exceptions.UserExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DataIntegrityAdvice {

    @ResponseBody
    @ExceptionHandler(UserExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String userExists(UserExistsException ex) {
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(MissingParameterException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    String missingParameter(MissingParameterException ex) {
        return ex.getMessage();
    }
}

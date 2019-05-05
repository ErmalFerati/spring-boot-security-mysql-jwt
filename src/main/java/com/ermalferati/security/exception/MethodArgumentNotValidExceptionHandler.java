package com.ermalferati.security.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class MethodArgumentNotValidExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(MethodArgumentNotValidExceptionHandler.class);

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public MethodArgumentNotValidError methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = getFieldErrors(result);
        return getMethodArgumentNotValidError(fieldErrors);
    }

    private List<FieldError> getFieldErrors(BindingResult result) {
        return result.getFieldErrors().stream()
                .map(fieldError -> new FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
    }

    private MethodArgumentNotValidError getMethodArgumentNotValidError(List<FieldError> fieldErrors) {
        MethodArgumentNotValidError methodArgumentNotValidError = new MethodArgumentNotValidError();
        methodArgumentNotValidError.setStatus(HttpStatus.BAD_REQUEST.value());
        methodArgumentNotValidError.setMessage("Field validation error");
        methodArgumentNotValidError.setFieldErrors(fieldErrors);

        return methodArgumentNotValidError;
    }

    private void logFieldErrors() {

    }
}

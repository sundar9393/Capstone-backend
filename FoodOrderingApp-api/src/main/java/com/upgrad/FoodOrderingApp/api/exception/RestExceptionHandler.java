package com.upgrad.FoodOrderingApp.api.exception;


import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> signupRestrictedExceptionHandler(SignUpRestrictedException sgr, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(sgr.getCode()).message(sgr.getErrorMessage())
                                                 .rootCause(sgr.getMessage()),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedExceptionHandler(AuthenticationFailedException afe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(afe.getCode()).message(afe.getErrorMessage())
                .rootCause(afe.getMessage()),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedExceptionHandler(AuthorizationFailedException athr, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(athr.getCode()).message(athr.getErrorMessage())
                ,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerExceptionHandler(UpdateCustomerException uce, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(uce.getCode()).message(uce.getErrorMessage())
                , HttpStatus.BAD_REQUEST);
    }
}

package com.upgrad.FoodOrderingApp.api.exception;


import com.upgrad.FoodOrderingApp.api.model.ErrorResponse;
import com.upgrad.FoodOrderingApp.service.exception.*;
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
                .rootCause(afe.getMessage()),HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedExceptionHandler(AuthorizationFailedException athr, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(athr.getCode()).message(athr.getErrorMessage())
                ,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerExceptionHandler(UpdateCustomerException uce, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(uce.getCode()).message(uce.getErrorMessage())
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> saveAddressExceptionHandler(SaveAddressException sae, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(sae.getCode()).message(sae.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> addressNotFoundExceptionHandler(AddressNotFoundException anfe, WebRequest request) {
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(anfe.getCode()).message(anfe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }









}

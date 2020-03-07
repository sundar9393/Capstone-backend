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
        return new ResponseEntity<>(new ErrorResponse().code(athr.getCode()).message(athr.getErrorMessage())
                ,HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UpdateCustomerException.class)
    public ResponseEntity<ErrorResponse> updateCustomerExceptionHandler(UpdateCustomerException uce, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(uce.getCode()).message(uce.getErrorMessage())
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SaveAddressException.class)
    public ResponseEntity<ErrorResponse> saveAddressExceptionHandler(SaveAddressException sae, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(sae.getCode()).message(sae.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AddressNotFoundException.class)
    public ResponseEntity<ErrorResponse> addressNotFoundExceptionHandler(AddressNotFoundException anfe, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(anfe.getCode()).message(anfe.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<ErrorResponse> restaurantNotFoundExceptionHandler(RestaurantNotFoundException rne, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(rne.getCode()).message(rne.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> categoryNotFoundExceptionHandler(CategoryNotFoundException cne, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(cne.getCode()).message(cne.getErrorMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> invalidRatingExceptionHandler(InvalidRatingException ire, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(ire.getCode()).message(ire.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CouponNotFoundException.class)
    public ResponseEntity<ErrorResponse> couponNotFoundExceptionHandler(CouponNotFoundException cnf, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(cnf.getCode()).message(cnf.getErrorMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ErrorResponse> itemNotFoundExceptionHandler(ItemNotFoundException inf, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(inf.getCode()).message(inf.getErrorMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PaymentMethodNotFoundException.class)
    public ResponseEntity<ErrorResponse> paymentNotFoundExceptionHandler(PaymentMethodNotFoundException pnf, WebRequest request) {
        return new ResponseEntity<>(new ErrorResponse().code(pnf.getCode()).message(pnf.getErrorMessage()), HttpStatus.NOT_FOUND);
    }


}

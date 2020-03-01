package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.mappers.RequestMapper;
import com.upgrad.FoodOrderingApp.api.mappers.ResponseMapper;
import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.JwtTokenProvider;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;

@RestController
@RequestMapping()
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
    produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest)
            throws SignUpRestrictedException, AuthenticationFailedException {
        CustomerEntity customerEntity = RequestMapper.toCustomerEntity(signupCustomerRequest);
        SignupCustomerResponse signupCustomerResponse = ResponseMapper.toSignupResponse(customerService.signupCustomer(customerEntity));
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader(name = "authorization") final String authorizationHeader)
            throws AuthenticationFailedException {
        CustomerEntity customerEntity;

        // Check the format of the Authorization Header
        customerService.verifyAuthorizationHeaderFormat(authorizationHeader);
        //Decoding the Basic Authorization Header
        byte[] decode = Base64.getDecoder().decode(authorizationHeader.split("Basic ")[1]);
        String decodedText = new String(decode);
        //Splitting the Contact Number and Password
        String[] decodedArray = decodedText.split(":");
        //Retrieving the Customer Record using the phone number and password
        customerEntity = customerService.getCustomerWithPhoneNumberAndPassword(decodedArray[0], decodedArray[1]);
    }

}

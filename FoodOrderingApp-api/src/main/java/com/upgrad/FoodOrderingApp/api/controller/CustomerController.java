package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.Util.Utility;
import com.upgrad.FoodOrderingApp.api.mappers.RequestMapper;
import com.upgrad.FoodOrderingApp.api.mappers.ResponseMapper;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.Base64;

@RestController
@RequestMapping()
public class CustomerController {

    @Autowired
    CustomerService customerService;


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

        // validating and decoding the Authorization Header to get phonenumber and password entered
        String[] decodedArray = Utility.decodeAuthHeather(authorizationHeader);
        //Creating a Customer Auth token after successful login
        final CustomerAuthTokenEntity customerAuthToken = customerService.login(decodedArray[0], decodedArray[1]);
        // After successful authentication, generating the Login In Response
        LoginResponse loginResponse = ResponseMapper.toLoginResponse(customerAuthToken);
        // Adding Access-Token to Header
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("access-token", customerAuthToken.getAccess_token());
        // Returning the Login Response
        return new ResponseEntity<LoginResponse>(loginResponse, responseHeaders, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader(name = "access-token") final String accessToken) throws
            AuthorizationFailedException {

        //Get the Auth token from the DB using the accessToken after doing necessary validations
        final CustomerAuthTokenEntity customerAuthToken = customerService.logout(accessToken);
        //Generate Logout response after successful authorization and logging out
        LogoutResponse logoutResponse = ResponseMapper.toLogoutResponse(customerAuthToken);
        //return
        return new ResponseEntity<LogoutResponse>(logoutResponse,HttpStatus.OK);

    }


    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> changePassword(@RequestHeader(name = "access-token") final String accessToken,
                                                                 @RequestBody(required = true) UpdatePasswordRequest updatePasswordRequest)
            throws UpdateCustomerException, AuthorizationFailedException, SignUpRestrictedException {
       //validate update password request
       if(StringUtils.isNotEmpty(updatePasswordRequest.getOldPassword()) && StringUtils.isNotEmpty(updatePasswordRequest.getNewPassword())) {
            String token = Utility.getAccessTokenFromHeader(accessToken);
            if(!(Utility.isPasswordStrong(updatePasswordRequest.getNewPassword()))) {
                throw new UpdateCustomerException("UCR-001","Weak password!");
            }
            CustomerEntity customerEntity = customerService.changePassword(token,updatePasswordRequest.getOldPassword(),updatePasswordRequest.getNewPassword());
            UpdatePasswordResponse updatePasswordResponse = ResponseMapper.toUpdatePassResponse(customerEntity);
            return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse,HttpStatus.OK);

       } else {
           throw new UpdateCustomerException("UCR-003","No field should be empty");
       }
    }

}

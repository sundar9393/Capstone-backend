package com.upgrad.FoodOrderingApp.api.mappers;

import com.upgrad.FoodOrderingApp.api.model.LoginResponse;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

public class ResponseMapper {

    public static SignupCustomerResponse toSignupResponse(CustomerEntity customerEntity) {
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(customerEntity.getUuid())
                                                        .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return signupCustomerResponse;
    }

    public static LoginResponse toLoginResponse(CustomerAuthTokenEntity authTokenEntity) {
        CustomerEntity customerEntity = authTokenEntity.getCustomer();
        return new LoginResponse()
                .id(customerEntity.getUuid())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .contactNumber(customerEntity.getContactNumber())
                .emailAddress(customerEntity.getEmail())
                .message("LOGGED IN SUCCESSFULLY");
    }

}

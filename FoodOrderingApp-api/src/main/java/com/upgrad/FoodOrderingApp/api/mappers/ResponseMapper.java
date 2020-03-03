package com.upgrad.FoodOrderingApp.api.mappers;

import com.upgrad.FoodOrderingApp.api.model.*;
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

    public static LogoutResponse toLogoutResponse(CustomerAuthTokenEntity authTokenEntity) {
        CustomerEntity customerEntity = authTokenEntity.getCustomer();
        return new LogoutResponse()
                .id(customerEntity.getUuid())
                .message("LOGGED OUT SUCCESSFULLY");
    }

    public static UpdatePasswordResponse toUpdatePassResponse(CustomerEntity customerEntity) {
        return new UpdatePasswordResponse().id(customerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
    }

    public static UpdateCustomerResponse toUpdateCustomerResponse(CustomerEntity customerEntity) {
        return new UpdateCustomerResponse().id(customerEntity.getUuid())
                                           .firstName(customerEntity.getFirstName())
                                           .lastName(customerEntity.getLastName())
                                           .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
    }

}

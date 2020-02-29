package com.upgrad.FoodOrderingApp.api.mappers;

import com.upgrad.FoodOrderingApp.api.model.SignupCustomerResponse;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;

public class ResponseMapper {

    public static SignupCustomerResponse toSignupResponse(CustomerEntity customerEntity) {
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(customerEntity.getUuid())
                                                        .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return signupCustomerResponse;
    }
}

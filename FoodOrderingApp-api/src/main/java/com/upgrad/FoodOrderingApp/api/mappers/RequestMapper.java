package com.upgrad.FoodOrderingApp.api.mappers;

import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.apache.commons.lang3.StringUtils;

public class RequestMapper {

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";


    public static CustomerEntity toCustomerEntity(SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {

        if(StringUtils.isNotEmpty(signupCustomerRequest.getFirstName()) && StringUtils.isNotEmpty(signupCustomerRequest.getContactNumber())
                &&StringUtils.isNotEmpty(signupCustomerRequest.getEmailAddress()) && StringUtils.isNotEmpty(signupCustomerRequest.getPassword())) {

            if(!(signupCustomerRequest.getContactNumber().matches("[0-9]+")) || !(signupCustomerRequest.getContactNumber().length()== 10)){
                throw new SignUpRestrictedException("SGR-003","Invalid contact number!");
            }

            if(!(signupCustomerRequest.getEmailAddress().contains("@")) || !(signupCustomerRequest.getEmailAddress().substring(signupCustomerRequest.getEmailAddress().length()-4).equals(".com"))
                    || !(signupCustomerRequest.getEmailAddress().substring(signupCustomerRequest.getEmailAddress().indexOf("@"),signupCustomerRequest.getEmailAddress().length()-4).matches("[a-zA-Z@]+"))) {
                throw new SignUpRestrictedException("SGR-002","Invalid email-id format!");
            }

            if(!(signupCustomerRequest.getPassword().matches(PASSWORD_PATTERN))) {
                throw new SignUpRestrictedException("SGR-004","Weak password!");
            }

            CustomerEntity customerEntity = new CustomerEntity();
            customerEntity.setFirstName(signupCustomerRequest.getFirstName());
            customerEntity.setContactNumber(signupCustomerRequest.getContactNumber());
            customerEntity.setEmail(signupCustomerRequest.getEmailAddress());
            customerEntity.setPassword(signupCustomerRequest.getPassword());
            if (StringUtils.isNotEmpty(signupCustomerRequest.getLastName())) {
                customerEntity.setLastName(signupCustomerRequest.getLastName());
            }

            return customerEntity;
        }
        else {
            throw new SignUpRestrictedException("SGR-005","Except last name all fields should be filled");
        }
    }

}

package com.upgrad.FoodOrderingApp.api.mappers;

import com.upgrad.FoodOrderingApp.api.Util.Utility;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantity;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.api.model.SignupCustomerRequest;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RequestMapper {


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

            if(!(Utility.isPasswordStrong(signupCustomerRequest.getPassword()))) {
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

    public static AddressEntity toAddressEntity(SaveAddressRequest saveAddressRequest, StateEntity stateEntity) throws SaveAddressException {
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setHouseNumber(saveAddressRequest.getFlatBuildingName());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setStatus(1);
        if(!(saveAddressRequest.getPincode().matches("^[1-9][0-9]{5}$"))) {
            throw new SaveAddressException("SAR-002","Invalid pincode");
        }
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setState(stateEntity);
         return addressEntity;

    }



}

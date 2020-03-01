package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    PasswordCryptographyProvider passwordEncryptor;

    public CustomerEntity getCustomerWithPhoneNumber(String contactNumber) throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDao.getCustomerByPhone(contactNumber);

        if (customerEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        } else {
            return customerEntity;
        }

    }


    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity signupCustomer(CustomerEntity customerEntity) throws SignUpRestrictedException, AuthenticationFailedException {
        CustomerEntity existingCustomer = getCustomerWithPhoneNumber(customerEntity.getContactNumber());
        if(existingCustomer!=null){
            throw new SignUpRestrictedException("SGR-001","This contact number is already registered! Try other contact number.");
        }

        String[] encryptedValues = passwordEncryptor.encrypt(customerEntity.getPassword());
        customerEntity.setPassword(encryptedValues[1]);
        customerEntity.setSalt(encryptedValues[0]);
        customerEntity.setUuid(UUID.randomUUID().toString());
        return customerDao.signupCustomer(customerEntity);
    }

    public void verifyAuthorizationHeaderFormat(final String authorizationHeader)
            throws AuthenticationFailedException {
        if (!Pattern.matches("Basic \\d{3,30}:\\S{3,255}", authorizationHeader)) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");

        }

    }

}
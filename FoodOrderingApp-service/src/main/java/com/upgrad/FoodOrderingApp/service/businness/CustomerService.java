package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
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

    /*  Writing the verification method in Utility.class
    public void verifyAuthorizationHeaderFormat(final String authorizationHeader)
            throws AuthenticationFailedException {
        // Contact Number must be minimum 3 character and maximum 30 characters
        // Password must be minimum 3 characters and maximum 255 characters


        if (!Pattern.matches("Basic \\d{3,30}:\\S{3,255}", authorizationHeader)) {
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");

        }

    }
*/

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity login(final String contactNumber, final String password)
            throws AuthenticationFailedException {

        CustomerEntity customerEntity = getCustomerWithPhoneNumber(contactNumber);

        //Encrypt the password befoe comaparing it woth the password in the db

        final String encryptedPassword = passwordEncryptor.encrypt(password,customerEntity.getSalt());

        if(encryptedPassword.equals(customerEntity.getPassword())) {
            //Create a Auth token entity
            JwtTokenProvider tokenProvider = new JwtTokenProvider(encryptedPassword);
            CustomerAuthTokenEntity authToken = new CustomerAuthTokenEntity();
            authToken.setCustomer(customerEntity);

            final ZonedDateTime now =ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(2);

            authToken.setAccess_token(tokenProvider.generateToken(customerEntity.getUuid(),now,expiresAt));
            authToken.setLogin_at(now);
            authToken.setExpires_at(expiresAt);
            authToken.setUuid(UUID.randomUUID().toString());

            return customerDao.createAuthToken(authToken);

        }
        else {
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthTokenEntity logout(String accessToken) throws AuthorizationFailedException {
        if(StringUtils.isNotEmpty(accessToken)) {
            CustomerAuthTokenEntity authToken = customerDao.getAuthTokenWithAccessToken(accessToken);
            validateAuthToken(authToken);
            authToken.setLogout_at(ZonedDateTime.now());
            return customerDao.logout(authToken);
        } else {
            throw new AuthorizationFailedException("ATHR-005", "Access token cannot be empty");
        }


    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerDetails(String accessToken, String firstname, String lastname) throws AuthorizationFailedException {
        if(StringUtils.isNotEmpty(accessToken)) {
            CustomerAuthTokenEntity authToken = customerDao.getAuthTokenWithAccessToken(accessToken);
            validateAuthToken(authToken);
            CustomerEntity customer = authToken.getCustomer();
            //Update firstname and lastname
            customer.setFirstName(firstname);
            if(StringUtils.isNotEmpty(lastname)){
                customer.setLastName(lastname);
            }
            return customerDao.updateCustomer(customer);
        } else {
            throw new AuthorizationFailedException("ATHR-005", "Access token cannot be empty");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity changePassword(String accessToken, String oldpass, String newpass) throws AuthorizationFailedException, UpdateCustomerException {
        if(StringUtils.isNotEmpty(accessToken)) {
            CustomerAuthTokenEntity authToken = customerDao.getAuthTokenWithAccessToken(accessToken);
            validateAuthToken(authToken);
            CustomerEntity customer = authToken.getCustomer();
            String encryptedOldpass = passwordEncryptor.encrypt(oldpass,customer.getSalt());
            //Comparing entered old password with password in db after encrypting
            if(!(encryptedOldpass.equals(customer.getPassword()))) {
                throw new UpdateCustomerException("UCR-004","Incorrect old password!");
            }
            String encryptedNewpass = passwordEncryptor.encrypt(newpass,customer.getSalt());
            if(encryptedNewpass.equals(customer.getPassword())) {
                throw new UpdateCustomerException("UCR-007","Enter a different new password,It cannot be same as old password");
            }
            customer.setPassword(encryptedNewpass);
            return customerDao.updateCustomer(customer);


        } else {
            throw new AuthorizationFailedException("ATHR-005", "Access token cannot be empty");
        }

    }

    /*
    Method to validate auth token
     */
    private void validateAuthToken(CustomerAuthTokenEntity authTokenEntity) throws AuthorizationFailedException {
        if(authTokenEntity!=null) {
            if (authTokenEntity.getLogout_at()!= null) {
                throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
            }
            if (ZonedDateTime.now().isAfter(authTokenEntity.getExpires_at())) {
                throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
            }
        } else {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
    }

}
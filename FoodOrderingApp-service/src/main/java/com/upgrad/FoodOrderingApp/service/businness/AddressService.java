package com.upgrad.FoodOrderingApp.service.businness;


import com.upgrad.FoodOrderingApp.service.dao.AddressDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    @Autowired
    AddressDao addressDao;

    @Autowired
    CustomerDao customerDao;

     public StateEntity getStateWithUuid(String uuid) {
        return addressDao.getStateWithUuid(uuid);
     }

     @Transactional(propagation = Propagation.REQUIRED)
     public AddressEntity saveAddress(String accessToken, AddressEntity addressEntity) throws AuthorizationFailedException {

         CustomerAuthTokenEntity authTokenEntity = customerDao.getAuthTokenWithAccessToken(accessToken);
         ServiceUtil.validateAuthToken(authTokenEntity);

         addressEntity.setUuid(UUID.randomUUID().toString());
         addressEntity.setCustomers(authTokenEntity.getCustomer());
         
         return addressDao.saveAddress(addressEntity);
     }

     public List<AddressEntity> getAllAddresses(String accessToken) throws AuthorizationFailedException {

         CustomerAuthTokenEntity authTokenEntity = customerDao.getAuthTokenWithAccessToken(accessToken);
         ServiceUtil.validateAuthToken(authTokenEntity);

         return addressDao.getAllAddresses();
     }

     public List<StateEntity> getAllStates() {
         return addressDao.getAllStates();
     }

     public AddressEntity getAddressWithUuid(String addrUuid) {
        return addressDao.getAddressWithUuid(addrUuid);
     }

     @Transactional(propagation = Propagation.REQUIRED)
     public AddressEntity deleteAddress(String accessToken, String addrUuid) throws AuthorizationFailedException, AddressNotFoundException {

         CustomerAuthTokenEntity authTokenEntity = customerDao.getAuthTokenWithAccessToken(accessToken);
         ServiceUtil.validateAuthToken(authTokenEntity);
         
         AddressEntity addressEntity = getAddressWithUuid(addrUuid);
         if(addressEntity==null){
             throw new AddressNotFoundException("ANF-003","No address by this id");
         }
         boolean isOwner = false;
         List<CustomerEntity> customersList = addressEntity.getCustomers();
         for(CustomerEntity customer: customersList) {
             if(customer.getUuid().equals(authTokenEntity.getCustomer().getUuid())) {
                isOwner = true;
                break;
             }
         }
         if(isOwner){
             return addressDao.deleteAddress(addressEntity);
         } else {
             throw new AuthorizationFailedException("ATHR-004","You are not authorized to view/update/delete any one else's address");
         }
     }

}

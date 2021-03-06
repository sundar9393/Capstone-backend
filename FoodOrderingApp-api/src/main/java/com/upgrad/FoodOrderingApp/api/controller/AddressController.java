package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.Util.Utility;
import com.upgrad.FoodOrderingApp.api.mappers.RequestMapper;
import com.upgrad.FoodOrderingApp.api.model.DeleteAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import com.upgrad.FoodOrderingApp.service.util.ServiceUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AddressController {

    @Autowired
    AddressService addressService;

    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestBody(required = true) SaveAddressRequest saveAddressRequest,
                                                           @RequestHeader(name = "access-token") final String accessToken)
                                                           throws SaveAddressException, AddressNotFoundException, AuthorizationFailedException {
        String token = Utility.getAccessTokenFromHeader(accessToken);
        if(StringUtils.isNotEmpty(saveAddressRequest.getFlatBuildingName()) && StringUtils.isNotEmpty(saveAddressRequest.getCity())
        && StringUtils.isNotEmpty(saveAddressRequest.getLocality()) && StringUtils.isNotEmpty(saveAddressRequest.getPincode())
        &&StringUtils.isNotEmpty(saveAddressRequest.getStateUuid())) {

            StateEntity stateEntity = addressService.getStateWithUuid(saveAddressRequest.getStateUuid());
            if(stateEntity == null) {
                throw new AddressNotFoundException("ANF-002", "No state by this id");
            }
            AddressEntity addrToBeSaved = RequestMapper.toAddressEntity(saveAddressRequest,stateEntity);
            AddressEntity savedAddr = addressService.saveAddress(token,addrToBeSaved);
            SaveAddressResponse saveAddressResponse = new
                    SaveAddressResponse()
                                                        .id(savedAddr.getUuid())
                                                        .status("ADDRESS SUCCESSFULLY REGISTERED");

            return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);



        } else {
            throw new SaveAddressException("SAR-001","No field can be empty");
        }


    }

    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<AddressEntity>> getAllAddresses(@RequestHeader(name = "access-token") final String accessToken) throws AuthorizationFailedException {

        String token = Utility.getAccessTokenFromHeader(accessToken);
        List<AddressEntity> addresses = addressService.getAllAddresses(token);

        return new ResponseEntity<List<AddressEntity>>(addresses,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<StateEntity>> getAllStates() {
        List<StateEntity> states = addressService.getAllStates();
        return new ResponseEntity<List<StateEntity>>(states,HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<DeleteAddressResponse> deleteAddress(@RequestHeader(name = "access-token") final String accessToken,
                                                               @PathVariable(name = "address_id") final String addrUuid) throws AuthorizationFailedException, AddressNotFoundException {
        String token = Utility.getAccessTokenFromHeader(accessToken);
        if(StringUtils.isNotEmpty(addrUuid)) {
           AddressEntity deletedAddress =  addressService.deleteAddress(token,addrUuid);
           return new ResponseEntity<DeleteAddressResponse>(new DeleteAddressResponse().id(UUID.fromString(deletedAddress.getUuid()))
           .status("ADDRESS DELETED SUCCESSFULLY"),HttpStatus.OK);
        } else{
            throw new AddressNotFoundException("ANF-005","Address id can not be empty");
        }
    }
}

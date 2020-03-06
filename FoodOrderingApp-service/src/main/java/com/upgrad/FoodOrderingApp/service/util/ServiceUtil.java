package com.upgrad.FoodOrderingApp.service.util;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;

import java.time.ZonedDateTime;

public class ServiceUtil {

    /*
   Method to validate auth token
    */
    public static void validateAuthToken(CustomerAuthTokenEntity authTokenEntity) throws AuthorizationFailedException {
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

package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

//This interacts with the service class and hits the DB

@Repository
public class CustomerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerEntity getCustomerByPhone(String contactNumber) {
        try {
            return entityManager.createNamedQuery("customerByContactNumber", CustomerEntity.class).setParameter("contactNumber", contactNumber).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerEntity signupCustomer(CustomerEntity customerEntity) {
        entityManager.persist(customerEntity);
        return customerEntity;
    }

    public CustomerAuthTokenEntity createAuthToken(CustomerAuthTokenEntity authTokenEntity) {
        entityManager.persist(authTokenEntity);
        return authTokenEntity;
    }

    public CustomerAuthTokenEntity getAuthTokenWithAccessToken(String accessToken) {
        try{
            return entityManager.createNamedQuery("getAuthTokenWithAccessToken", CustomerAuthTokenEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CustomerAuthTokenEntity logout(CustomerAuthTokenEntity customerAuthTokenEntity) {
        return entityManager.merge(customerAuthTokenEntity);
    }

    public CustomerEntity updateCustomer(CustomerEntity customerEntity) {
        return entityManager.merge(customerEntity);
    }
}

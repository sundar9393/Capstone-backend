package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class PaymentDao {

    @PersistenceContext
    EntityManager entityManager;


    public PaymentEntity getPaymentByUuid(String paymentId) {
        try {
            return entityManager.createNamedQuery("getPaymentWithUuid", PaymentEntity.class).setParameter("uuid", paymentId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
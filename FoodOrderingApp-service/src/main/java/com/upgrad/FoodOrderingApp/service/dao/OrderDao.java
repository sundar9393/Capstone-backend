package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class OrderDao {

    @PersistenceContext
    EntityManager entityManager;

    public CouponEntity getCouponByName(String couponName) {
        try {
            return entityManager.createNamedQuery("getCouponWithName",CouponEntity.class).setParameter("coupon", couponName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }
}

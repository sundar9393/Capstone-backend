package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import com.upgrad.FoodOrderingApp.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    CustomerDao customerDao;

    public CouponEntity getCouponByName(String accessToken, String couponName) throws AuthorizationFailedException, CouponNotFoundException {
        CustomerAuthTokenEntity authTokenEntity = customerDao.getAuthTokenWithAccessToken(accessToken);
        ServiceUtil.validateAuthToken(authTokenEntity);

        CouponEntity coupon = orderDao.getCouponByName(couponName.toLowerCase());

        if(coupon==null) {
            throw new CouponNotFoundException("CPF-001", "No coupon by this name");
        }

        return coupon;
    }

    public List<OrderEntity> getAllOrders(String accessToken) throws AuthorizationFailedException {

        CustomerAuthTokenEntity authTokenEntity = customerDao.getAuthTokenWithAccessToken(accessToken);
        ServiceUtil.validateAuthToken(authTokenEntity);

        CustomerEntity customer = authTokenEntity.getCustomer();

        List<OrderEntity> ordersListed = customer.getOrders();

        Collections.sort(ordersListed, new Comparator<OrderEntity>() {
            @Override
            public int compare(OrderEntity o1, OrderEntity o2) {
                if(o1.getDate().after(o2.getDate())) {
                    return -1;
                } else if(o1.getDate().before(o2.getDate())) {
                    return 1;
                }
                return 0;
            }
        });

        return ordersListed;
    }

    //get coupon by uuid

    public CouponEntity getCouponByUuid(String uuid) throws CouponNotFoundException {
        CouponEntity couponEntity = orderDao.getCouponByUuid(uuid);
        if(couponEntity == null) {
            throw new CouponNotFoundException("CPF-002","No coupon by this id");
        } else {
            return couponEntity;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OrderEntity saveOrder(String accessToken, OrderEntity orderEntity) throws AuthorizationFailedException {
        CustomerAuthTokenEntity authTokenEntity = customerDao.getAuthTokenWithAccessToken(accessToken);
        ServiceUtil.validateAuthToken(authTokenEntity);

        //Check whether the the address belongs to logged in person
        AddressEntity orderAddress = orderEntity.getAddress();

        boolean isOwner = false;
        List<CustomerEntity> customersList = orderAddress.getCustomers();
        for(CustomerEntity customer: customersList) {
            if(customer.getUuid().equals(authTokenEntity.getCustomer().getUuid())) {
                isOwner = true;
                break;
            }
        }
        if(isOwner){
            orderEntity.setCustomer(authTokenEntity.getCustomer());
            orderEntity.setUuid(UUID.randomUUID().toString());
            orderEntity.setDate(new Date());

            return orderDao.saveOrder(orderEntity);


        } else {
            throw new AuthorizationFailedException("ATHR-004","You are not authorized to view/update/delete any one else's address");
        }

    }



    public List<PaymentEntity> getAllPaymentMethods() {
        return orderDao.getAllPaymentMethods();
    }
}

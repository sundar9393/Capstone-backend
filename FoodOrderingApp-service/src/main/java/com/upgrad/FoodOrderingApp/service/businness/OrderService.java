package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.OrderDao;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import com.upgrad.FoodOrderingApp.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
}

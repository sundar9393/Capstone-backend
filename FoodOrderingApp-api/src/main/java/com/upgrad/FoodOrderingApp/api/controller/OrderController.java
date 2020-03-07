package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.Util.Utility;
import com.upgrad.FoodOrderingApp.api.mappers.RequestMapper;
import com.upgrad.FoodOrderingApp.api.mappers.ResponseMapper;
import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    AddressService addressService;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    PaymentService paymentService;

    @RequestMapping(method = RequestMethod.GET, path = "/order/coupon/{coupon_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CouponDetailsResponse> getCoupon(@RequestHeader(name = "access-token") final String accessToken,
                                                           @PathVariable(name = "coupon_name") final String couponName) throws AuthorizationFailedException, CouponNotFoundException {

        String authToken = Utility.getAccessTokenFromHeader(accessToken);

        if(StringUtils.isNotEmpty(couponName)) {

            CouponEntity coupon = orderService.getCouponByName(authToken, couponName);

            CouponDetailsResponse couponDetailsResponse = ResponseMapper.toCouponDetailsResponse(coupon);

            return new ResponseEntity<>(couponDetailsResponse, HttpStatus.OK);


        } else {
            throw new CouponNotFoundException("CPF-002","Coupon name field should not be empty");
        }


    }
    @RequestMapping(method = RequestMethod.GET, path = "/order", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<OrderList>> getAllOrders(@RequestHeader(name = "access-token") final String accessToken) throws AuthorizationFailedException {

        String authToken = Utility.getAccessTokenFromHeader(accessToken);
        List<OrderEntity> allOrders = orderService.getAllOrders(authToken);

        List<OrderList> customerOrders = ResponseMapper.toOrderList(allOrders);

        return new ResponseEntity<>(customerOrders, HttpStatus.OK);


    }

    @RequestMapping(method = RequestMethod.POST, path = "/order", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveOrderResponse> saveOrder(@RequestHeader(name = "access-token") final String accessToken,
                                                       @RequestBody(required = false)SaveOrderRequest saveOrderRequest) throws AuthorizationFailedException, ItemNotFoundException, PaymentMethodNotFoundException, CouponNotFoundException, RestaurantNotFoundException, AddressNotFoundException {

        String authToken = Utility.getAccessTokenFromHeader(accessToken);


        //Mapping saveOrderRequest to orderentity

        OrderEntity orderEntity = new OrderEntity();

        AddressEntity orderAddress = addressService.getAddressWithUuid(saveOrderRequest.getAddressId());
        if(orderAddress==null) {
            throw new AddressNotFoundException("ANF-003","No address by this id");
        }

        RestaurantEntity orderRestaurant = restaurantService.getRestaurantByuuid(saveOrderRequest.getRestaurantId().toString());
        CouponEntity orderCoupon = orderService.getCouponByUuid(saveOrderRequest.getCouponId().toString());

        PaymentEntity orderPayment = paymentService.getPaymentByUuid(saveOrderRequest.getPaymentId().toString());

        Set<OrderItem> orderItems = new HashSet<>();

        for(ItemQuantity itemQuantity: saveOrderRequest.getItemQuantities()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setPrice(itemQuantity.getPrice());
            orderItem.setQuantity(orderItem.getQuantity());
            ItemEntity itemEntity = restaurantService.getItemByUuid(itemQuantity.getItemId().toString());
            orderItem.setItems(itemEntity);
            orderItem.setOrder(orderEntity); //Doubt
            orderItems.add(orderItem);
        }


        orderEntity.setOrderItem(orderItems);

        orderEntity.setBillAmount(saveOrderRequest.getBill());
        orderEntity.setDiscount(saveOrderRequest.getDiscount());
        orderEntity.setAddress(orderAddress);
        orderEntity.setCouponCode(orderCoupon);
        orderEntity.setPayment(orderPayment);
        orderEntity.setRestaurant(orderRestaurant);

        //Call the service method to save the order

        OrderEntity savedOrder = orderService.saveOrder(authToken, orderEntity);

        return new ResponseEntity<>(new SaveOrderResponse().id(savedOrder.getUuid()).status("ORDER SUCCESSFULLY PLACED"),HttpStatus.OK);

    }
}

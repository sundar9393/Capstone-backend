package com.upgrad.FoodOrderingApp.api.controller;


import com.upgrad.FoodOrderingApp.api.Util.Utility;
import com.upgrad.FoodOrderingApp.api.mappers.ResponseMapper;
import com.upgrad.FoodOrderingApp.api.model.CouponDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.OrderService;
import com.upgrad.FoodOrderingApp.service.entity.CouponEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CouponNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

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
}

package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.mappers.ResponseMapper;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getAllRestaurants() {

        List<RestaurantEntity> restaurants = restaurantService.getAllRestaurants();
        List<RestaurantDetailsResponse> restaurantDetailsResponse = ResponseMapper.toRestaurantDetailsResponseList(restaurants);


        return new ResponseEntity<>(restaurantDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/name/{restaurant_name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurantByName(@PathVariable(name = "restaurant_name") final String restaurantName) throws RestaurantNotFoundException {

        if(StringUtils.isNotEmpty(restaurantName)) {
            List<RestaurantEntity> restaurantsByName = restaurantService.getRestaurantsByName(restaurantName);
            List<RestaurantDetailsResponse> restaurantDetailsResponsesByName = ResponseMapper.toRestaurantDetailsResponseList(restaurantsByName);

            return new ResponseEntity<>(restaurantDetailsResponsesByName, HttpStatus.OK);

        } else {
            throw new RestaurantNotFoundException("RNF-003","Restaurant name field should not be empty");
        }

    }

}

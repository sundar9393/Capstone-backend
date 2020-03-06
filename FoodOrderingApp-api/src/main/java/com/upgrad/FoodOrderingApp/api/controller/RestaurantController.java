package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.Util.Utility;
import com.upgrad.FoodOrderingApp.api.mappers.ResponseMapper;
import com.upgrad.FoodOrderingApp.api.model.RestaurantDetailsResponse;
import com.upgrad.FoodOrderingApp.api.model.RestaurantUpdatedResponse;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

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

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<RestaurantDetailsResponse>> getRestaurantsByCategory(@PathVariable(name = "category_id") final String categoryUuid) throws CategoryNotFoundException {
        if(StringUtils.isNotEmpty(categoryUuid)) {
            List<RestaurantEntity> restaurantEntitiesByCategory = restaurantService.getRestaurantsByCategory(categoryUuid);
            List<RestaurantDetailsResponse> restaurantDetailsResponsesByCategory = ResponseMapper.toRestaurantDetailsResponseList(restaurantEntitiesByCategory);

            return new ResponseEntity<>(restaurantDetailsResponsesByCategory, HttpStatus.OK);
        } else {
            throw new CategoryNotFoundException("CNF-001","Category id field should not be empty");
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantDetailsResponse> getRestaurantByUuid(@PathVariable("restaurant_id") final String restaurantId) throws RestaurantNotFoundException {
        if(StringUtils.isNotEmpty(restaurantId)) {
            RestaurantEntity restaurantByUuid = restaurantService.getRestaurantByuuid(restaurantId);
            RestaurantDetailsResponse restaurantDetailsResponseWithItems = ResponseMapper.toRestaurantDeatilsWithItems(restaurantByUuid);

            return new ResponseEntity<>(restaurantDetailsResponseWithItems, HttpStatus.OK);
        } else {
            throw new RestaurantNotFoundException("RNF-002","Restaurant id field should not be empty");
        }
    }


    @RequestMapping(method = RequestMethod.PUT, path = "/restaurant/{restaurant_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<RestaurantUpdatedResponse> rateRestaurant(@RequestHeader(name = "access-token") final String accessToken,
                                                                    @PathVariable(name = "restaurant_id") final String restaurantId,
                                                                    @RequestParam(name = "rating") final BigDecimal rating) throws AuthorizationFailedException, InvalidRatingException, RestaurantNotFoundException {

       String authToken = Utility.getAccessTokenFromHeader(accessToken);
       if(rating==null || rating.intValue() < 1 || rating.intValue() >5){
           throw new InvalidRatingException("IRE-001","Rating should be in the range of 1 to 5");
       }
        RestaurantEntity restaurantEntity = restaurantService.rateRestaurant(authToken, restaurantId, rating);

       return new ResponseEntity<>(new RestaurantUpdatedResponse().id(UUID.fromString(restaurantEntity.getUuid())).
               status("RESTAURANT RATING UPDATED SUCCESSFULLY"), HttpStatus.OK);

    }
}

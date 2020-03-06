package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthTokenEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import com.upgrad.FoodOrderingApp.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantDao restaurantDao;

    @Autowired
    CustomerDao customerDao;

    public List<RestaurantEntity> getAllRestaurants() {
        return restaurantDao.getAllRestaurants();
    }

    public List<RestaurantEntity> getRestaurantsByName(String restaurantName) {
        return  restaurantDao.getRestaurantByName(restaurantName.toLowerCase());
    }

    public List<RestaurantEntity> getRestaurantsByCategory(String categoryUuid) throws CategoryNotFoundException {

        CategoryEntity categoryEntity = restaurantDao.getCategoryByUuid(categoryUuid);
        if(categoryEntity==null) {
            throw new CategoryNotFoundException("CNF-002","No category by this id");
        }
        return restaurantDao.getRestaurantsByCategory(categoryEntity.getCategory());

    }

    public RestaurantEntity getRestaurantByuuid(String restaurantId) throws RestaurantNotFoundException {

        RestaurantEntity restaurantEntity = restaurantDao.getRestaurantByUuid(restaurantId);

        if(restaurantEntity==null) {
            throw new RestaurantNotFoundException("RNF-001","No restaurant by this id");
        }

        return restaurantEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RestaurantEntity rateRestaurant(String accessToken, String restaurantId, BigDecimal rating) throws AuthorizationFailedException, RestaurantNotFoundException {
        CustomerAuthTokenEntity authTokenEntity = customerDao.getAuthTokenWithAccessToken(accessToken);
        ServiceUtil.validateAuthToken(authTokenEntity);

        RestaurantEntity restaurantEntity = getRestaurantByuuid(restaurantId);

        BigDecimal updatedTotalRating = new BigDecimal((restaurantEntity.getRating().doubleValue() * restaurantEntity.getCustomersRated()) + rating.doubleValue());
        BigDecimal updatedAvgRating = updatedTotalRating.divide(new BigDecimal(restaurantEntity.getCustomersRated() + 1), 2, RoundingMode.HALF_UP);

        restaurantEntity.setRating(updatedAvgRating);
        restaurantEntity.setCustomersRated(restaurantEntity.getCustomersRated()+1);

        return restaurantDao.rateRestaurant(restaurantEntity);

    }
}

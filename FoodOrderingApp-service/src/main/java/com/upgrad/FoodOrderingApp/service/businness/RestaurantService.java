package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    @Autowired
    RestaurantDao restaurantDao;

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
}

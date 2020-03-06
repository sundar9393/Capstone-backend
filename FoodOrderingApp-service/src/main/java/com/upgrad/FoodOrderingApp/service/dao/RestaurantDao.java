package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.entity.RestaurantEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class RestaurantDao {

    @PersistenceContext
    EntityManager entityManager;

    public List<RestaurantEntity> getAllRestaurants() {
        List<RestaurantEntity> restaurantEntityList =  entityManager.createNamedQuery("getAllRestaurants",RestaurantEntity.class).getResultList();
        return restaurantEntityList;
    }

    public List<RestaurantEntity> getRestaurantByName(String restaurantName) {
        List<RestaurantEntity> restaurantEntityList = entityManager.createNamedQuery("getRestaurantByName", RestaurantEntity.class).setParameter("name","%"+restaurantName+"%").getResultList();
        return restaurantEntityList;
    }

    public List<RestaurantEntity> getRestaurantsByCategory(String category) {
        List<RestaurantEntity> restaurantEntityList = entityManager.createNamedQuery("getRestaurantsByCategory", RestaurantEntity.class).setParameter("categoryName", category).getResultList();
        return restaurantEntityList;
    }

    public CategoryEntity getCategoryByUuid(String categoryId) {
        try {
            return entityManager.createNamedQuery("getCategoryWithUuid", CategoryEntity.class).setParameter("uuid", categoryId).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }

    public RestaurantEntity getRestaurantByUuid(String restaurantId) {
        try {
            return entityManager.createNamedQuery("getRestaurantByuuid", RestaurantEntity.class).setParameter("uuid", restaurantId).getSingleResult();

        } catch (NoResultException nre) {
            return null;
        }
    }
}

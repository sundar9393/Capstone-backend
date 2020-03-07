package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.dao.RestaurantDao;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.ItemNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import com.upgrad.FoodOrderingApp.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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

    public List<ItemEntity> getTopfiveItems(String restaurantId) throws RestaurantNotFoundException {
        RestaurantEntity restaurantEntity = getRestaurantByuuid(restaurantId);

        List<ItemEntity> topFive = new ArrayList<>();

        Map<String, Integer> itemCounter = new HashMap<>();
        Set<String> items = new HashSet<>();

        Set<OrderEntity> orders = restaurantEntity.getOrders();

        for(OrderEntity order: orders) {

            Set<OrderItem> orderItems = order.getOrderItem();

            for(OrderItem orderItem: orderItems) {

                if(items.contains(orderItem.getItem().getItemName())) {

                    if(itemCounter.containsKey(orderItem.getItem().getUuid())) {

                        Integer existingQuantity = itemCounter.get(orderItem.getItem().getUuid());
                        itemCounter.put(orderItem.getItem().getUuid(), existingQuantity+orderItem.getQuantity());

                    }
                } else {

                    items.add(orderItem.getItem().getItemName());
                    itemCounter.put(orderItem.getItem().getUuid(),orderItem.getQuantity());
                }

            }
        }

        List<Map.Entry<String, Integer>> sortedList = sortMapInDescendingOrder(itemCounter);
        int count = 0;
        for(Map.Entry<String, Integer> item: sortedList) {
            if(count <= 5) {
                break;
            }
            topFive.add(restaurantDao.getItemByUuid(item.getKey()));
            count++;
        }

    return topFive;

    }

    public ItemEntity getItemByUuid(String itemId) throws ItemNotFoundException {
        ItemEntity itemEntity = restaurantDao.getItemByUuid(itemId);
        if(itemEntity == null) {
            throw new ItemNotFoundException("INF-003","No item by this id exist");
        } else {
            return itemEntity;
        }
    }


    private List<Map.Entry<String,Integer>> sortMapInDescendingOrder(Map<String , Integer> map) {

        List<Map.Entry<String,Integer>> sortList = new ArrayList<>(map.entrySet());

        Collections.sort(sortList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        return sortList;

    }
}

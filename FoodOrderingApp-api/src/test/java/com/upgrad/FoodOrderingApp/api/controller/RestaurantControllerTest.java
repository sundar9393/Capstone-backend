package com.upgrad.FoodOrderingApp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrad.FoodOrderingApp.api.model.ItemList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantList;
import com.upgrad.FoodOrderingApp.api.model.RestaurantListResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.businness.RestaurantService;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.InvalidRatingException;
import com.upgrad.FoodOrderingApp.service.exception.RestaurantNotFoundException;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// This class contains all the test cases regarding the restaurant controller
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestaurantService mockRestaurantService;

    @MockBean
    private CategoryService mockCategoryService;

    @MockBean
    private CustomerService mockCustomerService;

    // ------------------------------------------ GET /restaurant/{restaurant_id} ------------------------------------------

    //This test case passes when you get restaurant details based on restaurant id.
    @Ignore
    @Test
    public void shouldGetRestaurantDetailsForCorrectRestaurantId() throws Exception {
        final RestaurantEntity restaurantEntity = getRestaurantEntity();
        when(mockRestaurantService.getRestaurantByuuid("someRestaurantId"))
                .thenReturn(restaurantEntity);

        final CategoryEntity categoryEntity = getCategoryEntity();
        //when(mockCategoryService.getRestaurantsByCategory("someRestaurantId"))
          //      .thenReturn(Collections.singletonList(categoryEntity));

        final ItemEntity itemEntity = getItemEntity();
        //when(mockItemService.getItemsByCategoryAndRestaurant("someRestaurantId", categoryEntity.getUuid()))
          //      .thenReturn(Collections.singletonList(itemEntity));

        mockMvc
                .perform(get("/restaurant/someRestaurantId").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(restaurantEntity.getUuid()))
                .andExpect(jsonPath("restaurant_name").value("Famous Restaurant"))
                .andExpect(jsonPath("customer_rating").value(3.4))
                .andExpect(jsonPath("number_customers_rated").value(200));
        verify(mockRestaurantService, times(1)).getRestaurantByuuid("someRestaurantId");
        //verify(mockCategoryService, times(1)).getRestaurantsByCategory("someRestaurantId");
        //verify(mockItemService, times(1))
          //      .getItemsByCategoryAndRestaurant("someRestaurantId", categoryEntity.getUuid());
    }

    //This test case passes when you have handled the exception of trying to fetch any restaurant but your restaurant id
    // field is empty.
    @Ignore
    @Test
    public void shouldNotGetRestaurantidIfRestaurantIdIsEmpty() throws Exception {
        when(mockRestaurantService.getRestaurantByuuid(anyString()))
                .thenThrow(new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty"));

        mockMvc
                .perform(get("/restaurant/emptyString").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("RNF-002"));
        verify(mockRestaurantService, times(1)).getRestaurantByuuid(anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch restaurant details while there are
    // no restaurants with that restaurant id.
    @Ignore
    @Test
    public void shouldNotGetRestaurantDetailsIfRestaurantNotFoundForGivenRestaurantId() throws Exception {
        when(mockRestaurantService.getRestaurantByuuid("someRestaurantId"))
                .thenThrow(new RestaurantNotFoundException("RNF-001", "No restaurant by this id"));

        mockMvc
                .perform(get("/restaurant/someRestaurantId").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("RNF-001"));
        verify(mockRestaurantService, times(1)).getRestaurantByuuid("someRestaurantId");
//        verify(mockCategoryService, times(0)).getRestaurantsByCategory(anyString());
//        verify(mockItemService, times(0)).getItemsByCategoryAndRestaurant(anyString(), anyString());
    }

    // ------------------------------------------ GET /restaurant/name/{restaurant_name} ------------------------------------------

    //This test case passes when you are able to fetch restaurants by the name you provided.
    @Ignore
    @Test
    public void shouldGetRestaurantDetailsByGivenName() throws Exception {
        final RestaurantEntity restaurantEntity = getRestaurantEntity();
        when(mockRestaurantService.getRestaurantsByName("someRestaurantName"))
                .thenReturn(Collections.singletonList(restaurantEntity));

        final CategoryEntity categoryEntity = getCategoryEntity();
//        when(mockCategoryService.getRestaurantsByCategory(restaurantEntity.getUuid()))
//                .thenReturn(Collections.singletonList(categoryEntity));

        final String responseString = mockMvc
                .perform(get("/restaurant/name/someRestaurantName").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final RestaurantListResponse restaurantListResponse = new ObjectMapper().readValue(responseString, RestaurantListResponse.class);
        assertEquals(restaurantListResponse.getRestaurants().size(), 1);

        final RestaurantList restaurantList = restaurantListResponse.getRestaurants().get(0);
        assertEquals(restaurantList.getId().toString(), restaurantEntity.getUuid());
        assertEquals(restaurantList.getAddress().getId().toString(), restaurantEntity.getAddress().getUuid());
        assertEquals(restaurantList.getAddress().getState().getId().toString(), restaurantEntity.getAddress().getState().getUuid());

//        verify(mockRestaurantService, times(1)).restaurantsByName("someRestaurantName");
//        verify(mockCategoryService, times(1)).getRestaurantsByCategory(restaurantEntity.getUuid());
    }

    //This test case passes when you have handled the exception of trying to fetch any restaurants but your restaurant name
    // field is empty.
    @Test
    @Ignore
    public void shouldNotGetRestaurantByNameIfNameIsEmpty() throws Exception {
        when(mockRestaurantService.getRestaurantsByName(anyString()))
                .thenThrow(new RestaurantNotFoundException("RNF-003", "Restaurant name field should not be empty"));

        mockMvc
                .perform(get("/restaurant/name/emptyString").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("RNF-003"));
        verify(mockRestaurantService, times(1)).getRestaurantsByName(anyString());
    }


    // ------------------------------------------ GET /restaurant/category/{category_id} ------------------------------------------

    //This test case passes when you are able to retrieve restaurant belonging to any particular categories.
    @Ignore
    @Test
    public void shouldGetRestaurantDetailsByGivenCategoryId() throws Exception {
        final RestaurantEntity restaurantEntity = getRestaurantEntity();
//        when(mockRestaurantService.restaurantByCategory("someCategoryId"))
//                .thenReturn(Collections.singletonList(restaurantEntity));

        final CategoryEntity categoryEntity = getCategoryEntity();
        //when(mockCategoryService.getRestaurantsByCategory(restaurantEntity.getUuid()))
          //      .thenReturn(Collections.singletonList(categoryEntity));

        final String responseString = mockMvc
                .perform(get("/restaurant/category/someCategoryId").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final RestaurantListResponse restaurantListResponse = new ObjectMapper().readValue(responseString, RestaurantListResponse.class);
        assertEquals(restaurantListResponse.getRestaurants().size(), 1);

        final RestaurantList restaurantList = restaurantListResponse.getRestaurants().get(0);
        assertEquals(restaurantList.getId().toString(), restaurantEntity.getUuid());
        assertEquals(restaurantList.getAddress().getId().toString(), restaurantEntity.getAddress().getUuid());
        assertEquals(restaurantList.getAddress().getState().getId().toString(), restaurantEntity.getAddress().getState().getUuid());

        
    }

    //This test case passes when you have handled the exception of trying to fetch any restaurants but your category id
    // field is empty.
    @Test
    @Ignore
    public void shouldNotGetRestaurantByCategoryidIfCategoryIdIsEmpty() throws Exception {
        when(mockRestaurantService.getRestaurantsByCategory(anyString()))
                .thenThrow(new CategoryNotFoundException("CNF-001", "Category id field should not be empty"));

        mockMvc
                .perform(get("/restaurant/category/emptyString").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("CNF-001"));
        verify(mockRestaurantService, times(1)).getRestaurantsByCategory(anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch any restaurant by its category id, while there
    // is not category by that id in the database
    @Test
    @Ignore
    public void shouldNotGetRestaurantsByCategoryIdIfCategoryDoesNotExistAgainstGivenId() throws Exception {
        when(mockRestaurantService.getRestaurantsByCategory("someCategoryId"))
                .thenThrow(new CategoryNotFoundException("CNF-002", "No category by this id"));

        mockMvc
                .perform(get("/restaurant/category/someCategoryId").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("CNF-002"));
        verify(mockRestaurantService, times(1)).getRestaurantsByCategory("someCategoryId");
    }


    // ------------------------------------------ GET /restaurant ------------------------------------------

    //This test case passes when you able to fetch the list of all restaurants.
    @Ignore
    @Test
    public void shouldGetAllRestaurantDetails() throws Exception {
//        final RestaurantEntity restaurantEntity = getRestaurantEntity();
//        when(mockRestaurantService.restaurantsByRating())
//                .thenReturn(Collections.singletonList(restaurantEntity));
//
//        final CategoryEntity categoryEntity = getCategoryEntity();
//        when(mockCategoryService.getRestaurantsByCategory(restaurantEntity.getUuid()))
//                .thenReturn(Collections.singletonList(categoryEntity));
//
//        final String responseString = mockMvc
//                .perform(get("/restaurant").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(status().isOk())
//                .andReturn().getResponse().getContentAsString();
//
//        final RestaurantListResponse restaurantListResponse = new ObjectMapper().readValue(responseString, RestaurantListResponse.class);
//        assertEquals(restaurantListResponse.getRestaurants().size(), 1);
//
//        final RestaurantList restaurantList = restaurantListResponse.getRestaurants().get(0);
//        assertEquals(restaurantList.getId().toString(), restaurantEntity.getUuid());
//        assertEquals(restaurantList.getAddress().getId().toString(), restaurantEntity.getAddress().getUuid());
//        assertEquals(restaurantList.getAddress().getState().getId().toString(), restaurantEntity.getAddress().getState().getUuid());
//
//        verify(mockRestaurantService, times(1)).restaurantsByRating();
//        verify(mockCategoryService, times(1)).getRestaurantsByCategory(restaurantEntity.getUuid());
    }


    // ------------------------------------------ PUT /restaurant/{restaurant_id} ------------------------------------------

    //This test case passes when you are able to update restaurant rating successfully.
    @Ignore
    @Test
    public void shouldUpdateRestaurantRating() throws Exception {
        final String restaurantId = UUID.randomUUID().toString();

        when(mockCustomerService.getCustomerWithPhoneNumber("database_accesstoken2"))
                .thenReturn(new CustomerEntity());

        final RestaurantEntity restaurantEntity = getRestaurantEntity();
        when(mockRestaurantService.getRestaurantByuuid(restaurantId)).thenReturn(restaurantEntity);

        

        mockMvc
                .perform(put("/restaurant/" + restaurantId + "?customer_rating=4.5")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(restaurantId));
        //verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("database_accesstoken2");
        verify(mockRestaurantService, times(1)).getRestaurantByuuid(restaurantId);
        //verify(mockRestaurantService, times(1)).updateRestaurantRating(restaurantEntity, 4.5);
    }

    //This test case passes when you have handled the exception of trying to update restaurant rating while you are
    // not logged in.
    @Ignore
    @Test
    public void shouldNotUpdateRestaurantRatingIfCustomerIsNotLoggedIn() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("invalid_auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-001", "Customer is not Logged in."));

        mockMvc
                .perform(put("/restaurant/someRestaurantId/?customer_rating=4.5")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-001"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("invalid_auth");
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockRestaurantService, times(0)).rateRestaurant(any(), any(),any());
    }

    //This test case passes when you have handled the exception of trying to update restaurant rating while you are
    // already logged out.
    @Ignore
    @Test
    public void shouldNotUpdateRestaurantRatingIfCustomerIsLoggedOut() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("invalid_auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint."));

        mockMvc
                .perform(put("/restaurant/someRestaurantId/?customer_rating=4.5")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-002"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("invalid_auth");
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockRestaurantService, times(0)).rateRestaurant(any(), any(),any());
    }

    //This test case passes when you have handled the exception of trying to update restaurant rating while your session
    // is already expired.
    @Ignore
    @Test
    public void shouldNotUpdateRestaurantRatingIfCustomerSessionIsExpired() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("invalid_auth"))
                .thenThrow(new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint."));

        mockMvc
                .perform(put("/restaurant/someRestaurantId/?customer_rating=4.5")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-003"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("invalid_auth");
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockRestaurantService, times(0)).rateRestaurant(any(), any(),any());
    }

    //This test case passes when you have handled the exception of trying to update any restaurant but your restaurant id
    // field is empty.
    @Ignore
    @Test
    public void shouldNotUpdateRestaurantIfRestaurantIdIsEmpty() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("database_accesstoken2"))
                .thenReturn(new CustomerEntity());

        when(mockRestaurantService.getRestaurantByuuid(anyString()))
                .thenThrow(new RestaurantNotFoundException("RNF-002", "Restaurant id field should not be empty"));

        mockMvc
                .perform(get("/restaurant/emptyString").contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("RNF-002"));
        verify(mockCustomerService, times(0)).getCustomerWithPhoneNumber("database_accesstoken2");
        verify(mockRestaurantService, times(1)).getRestaurantByuuid(anyString());
    }

    //This test case passes when you have handled the exception of trying to update restaurant rating while the
    // restaurant id you provided does not exist in the database.
    @Ignore
    @Test
    public void shouldNotUpdateRestaurantRatingIfRestaurantDoesNotExists() throws Exception {
        final String restaurantId = UUID.randomUUID().toString();

        when(mockCustomerService.getCustomerWithPhoneNumber("database_accesstoken2"))
                .thenReturn(new CustomerEntity());

        when(mockRestaurantService.getRestaurantByuuid(restaurantId))
                .thenThrow(new RestaurantNotFoundException("RNF-001", "No restaurant by this id"));

        mockMvc
                .perform(put("/restaurant/" + restaurantId + "?customer_rating=4.5")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("RNF-001"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("database_accesstoken2");
        verify(mockRestaurantService, times(1)).getRestaurantByuuid(restaurantId);
        verify(mockRestaurantService, times(0))
                .rateRestaurant(any(), any(),any());
    }

    //This test case passes when you have handled the exception of trying to update restaurant rating while the rating
    // you provided is less than 1.
    @Ignore
    @Test
    public void shouldNotUpdateRestaurantRatingIfNewRatingIsLessThan1() throws Exception {
        final String restaurantId = UUID.randomUUID().toString();

        when(mockCustomerService.getCustomerWithPhoneNumber("database_accesstoken2"))
                .thenReturn(new CustomerEntity());

        final RestaurantEntity restaurantEntity = getRestaurantEntity();
        when(mockRestaurantService.getRestaurantByuuid(restaurantId)).thenReturn(restaurantEntity);

        when(mockRestaurantService.rateRestaurant(any(), any(),any()))
                .thenThrow(new InvalidRatingException("IRE-001", "Rating should be in the range of 1 to 5"));

        mockMvc
                .perform(put("/restaurant/" + restaurantId + "?customer_rating=-5.5")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("IRE-001"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("database_accesstoken2");
        verify(mockRestaurantService, times(1)).getRestaurantByuuid(restaurantId);
        verify(mockRestaurantService, times(1))
                .rateRestaurant(any(), any(),any());
    }

    //This test case passes when you have handled the exception of trying to update restaurant rating while the rating
    // you provided is greater than 5.
    @Ignore
    @Test
    public void shouldNotUpdateRestaurantRatingIfNewRatingIsGreaterThan5() throws Exception {
        final String restaurantId = UUID.randomUUID().toString();

        when(mockCustomerService.getCustomerWithPhoneNumber("database_accesstoken2"))
                .thenReturn(new CustomerEntity());

        final RestaurantEntity restaurantEntity = getRestaurantEntity();
        when(mockRestaurantService.getRestaurantByuuid(restaurantId)).thenReturn(restaurantEntity);

        when(mockRestaurantService.rateRestaurant(any(), any(),any()))
                .thenThrow(new InvalidRatingException("IRE-001", "Rating should be in the range of 1 to 5"));

        mockMvc
                .perform(put("/restaurant/" + restaurantId + "?customer_rating=5.5")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value("IRE-001"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("database_accesstoken2");
        verify(mockRestaurantService, times(1)).getRestaurantByuuid(restaurantId);
        verify(mockRestaurantService, times(1))
                .rateRestaurant(any(), any(),any());
    }

    // ------------------------------------------ POJO builders ------------------------------------------

    private ItemEntity getItemEntity() {
        final ItemEntity itemEntity = new ItemEntity();
        final String itemId = UUID.randomUUID().toString();
        itemEntity.setUuid(itemId);
        itemEntity.setItemName("someItem");
        itemEntity.setType(ItemList.ItemTypeEnum.NON_VEG.toString());
        itemEntity.setPrice(200);
        return itemEntity;
    }

    private CategoryEntity getCategoryEntity() {
        final CategoryEntity categoryEntity = new CategoryEntity();
        final String categoryId = UUID.randomUUID().toString();
        categoryEntity.setUuid(categoryId);
        categoryEntity.setCategory("someCategory");
        return categoryEntity;
    }

    private RestaurantEntity getRestaurantEntity() {
        final String stateId = UUID.randomUUID().toString();
        final StateEntity stateEntity = new StateEntity();
        final String addressId = UUID.randomUUID().toString();
        final AddressEntity addressEntity = new AddressEntity();

        final RestaurantEntity restaurantEntity = new RestaurantEntity();
        final String restaurantId = UUID.randomUUID().toString();
        restaurantEntity.setUuid(restaurantId);
        restaurantEntity.setAddress(addressEntity);
        restaurantEntity.setAvgPriceForTwo(123);
        //restaurantEntity.setRating(3.4d);
        restaurantEntity.setCustomersRated(200);
        restaurantEntity.setImageUrl("someurl");
        restaurantEntity.setName("Famous Restaurant");
        return restaurantEntity;
    }
}

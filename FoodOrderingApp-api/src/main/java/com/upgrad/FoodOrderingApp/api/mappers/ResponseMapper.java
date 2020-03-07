package com.upgrad.FoodOrderingApp.api.mappers;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResponseMapper {

    public static SignupCustomerResponse toSignupResponse(CustomerEntity customerEntity) {
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(customerEntity.getUuid())
                                                        .status("CUSTOMER SUCCESSFULLY REGISTERED");
        return signupCustomerResponse;
    }

    public static LoginResponse toLoginResponse(CustomerAuthTokenEntity authTokenEntity) {
        CustomerEntity customerEntity = authTokenEntity.getCustomer();
        return new LoginResponse()
                .id(customerEntity.getUuid())
                .firstName(customerEntity.getFirstName())
                .lastName(customerEntity.getLastName())
                .contactNumber(customerEntity.getContactNumber())
                .emailAddress(customerEntity.getEmail())
                .message("LOGGED IN SUCCESSFULLY");
    }

    public static LogoutResponse toLogoutResponse(CustomerAuthTokenEntity authTokenEntity) {
        CustomerEntity customerEntity = authTokenEntity.getCustomer();
        return new LogoutResponse()
                .id(customerEntity.getUuid())
                .message("LOGGED OUT SUCCESSFULLY");
    }

    public static UpdatePasswordResponse toUpdatePassResponse(CustomerEntity customerEntity) {
        return new UpdatePasswordResponse().id(customerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
    }

    public static UpdateCustomerResponse toUpdateCustomerResponse(CustomerEntity customerEntity) {
        return new UpdateCustomerResponse().id(customerEntity.getUuid())
                                           .firstName(customerEntity.getFirstName())
                                           .lastName(customerEntity.getLastName())
                                           .status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
    }

    public static List<RestaurantDetailsResponse> toRestaurantDetailsResponseList(List<RestaurantEntity> restaurantEntities) {

        List<RestaurantDetailsResponse> restaurantDetails = new ArrayList<>();

        for(RestaurantEntity restaurantEntity: restaurantEntities) {


            RestaurantDetailsResponse details = new RestaurantDetailsResponse();
            RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
            RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
            List<CategoryList> categories = new ArrayList<>();

            details.setId(UUID.fromString(restaurantEntity.getUuid()));
            details.setRestaurantName(restaurantEntity.getName());
            details.setPhotoURL(restaurantEntity.getImageUrl());

            address.setId(UUID.fromString(restaurantEntity.getAddress().getUuid()));
            address.setFlatBuildingName(restaurantEntity.getAddress().getHouseNumber());
            address.setLocality(restaurantEntity.getAddress().getLocality());
            address.setCity(restaurantEntity.getAddress().getCity());
            address.setPincode(restaurantEntity.getAddress().getPincode());

            state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
            state.setStateName(restaurantEntity.getAddress().getState().getStateName());

            address.setState(state);

            details.setAddress(address);
            details.setAveragePrice(restaurantEntity.getAvgPriceForTwo());
            details.setCustomerRating(restaurantEntity.getRating());
            details.setNumberCustomersRated(restaurantEntity.getCustomersRated());

            for(CategoryEntity categoryEntity: restaurantEntity.getCategories()) {
                CategoryList categoryList = new CategoryList();
                categoryList.setCategoryName(categoryEntity.getCategory());
                categories.add(categoryList);
            }

            details.setCategories(categories);

            restaurantDetails.add(details);

        }
        return restaurantDetails;
    }

    public static RestaurantDetailsResponse toRestaurantDeatilsWithItems(RestaurantEntity restaurantEntity) {
        RestaurantDetailsResponse details = new RestaurantDetailsResponse();

        RestaurantDetailsResponseAddress address = new RestaurantDetailsResponseAddress();
        RestaurantDetailsResponseAddressState state = new RestaurantDetailsResponseAddressState();
        List<CategoryList> categories = new ArrayList<>();


        details.setId(UUID.fromString(restaurantEntity.getUuid()));
        details.setRestaurantName(restaurantEntity.getName());
        details.setPhotoURL(restaurantEntity.getImageUrl());

        address.setId(UUID.fromString(restaurantEntity.getAddress().getUuid()));
        address.setFlatBuildingName(restaurantEntity.getAddress().getHouseNumber());
        address.setLocality(restaurantEntity.getAddress().getLocality());
        address.setCity(restaurantEntity.getAddress().getCity());
        address.setPincode(restaurantEntity.getAddress().getPincode());

        state.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
        state.setStateName(restaurantEntity.getAddress().getState().getStateName());

        address.setState(state);

        details.setAddress(address);
        details.setAveragePrice(restaurantEntity.getAvgPriceForTwo());
        details.setCustomerRating(restaurantEntity.getRating());
        details.setNumberCustomersRated(restaurantEntity.getCustomersRated());

        for(CategoryEntity categoryEntity: restaurantEntity.getCategories()) {
            CategoryList categoryList = new CategoryList();
            List<ItemList> items = new ArrayList<>();

            categoryList.setCategoryName(categoryEntity.getCategory());
            categoryList.setId(UUID.fromString(categoryEntity.getUuid()));
            for(ItemEntity item : categoryEntity.getItems()) {
                ItemList itemList = new ItemList();
                itemList.setId(UUID.fromString(item.getUuid()));
                itemList.setItemName(item.getItemName());
                if(item.getType().equals("1")) {
                    itemList.setItemType(ItemList.ItemTypeEnum.NON_VEG);
                } else {
                    itemList.setItemType(ItemList.ItemTypeEnum.VEG);
                }
                itemList.setPrice(item.getPrice());

                items.add(itemList);
            }

            categoryList.setItemList(items);

            categories.add(categoryList);
        }
        details.setCategories(categories);

        return details;
    }


    public static List<ItemList> toItemList(List<ItemEntity> topfiveItems) {
        List<ItemList> itemsList = new ArrayList<>();

        for(ItemEntity itemEntity: topfiveItems) {
            ItemList item = new ItemList();
            item.setId(UUID.fromString(itemEntity.getUuid()));
            item.setItemName(itemEntity.getItemName());
            item.setPrice(itemEntity.getPrice());
            if(itemEntity.getType().equals("1")){
                item.setItemType(ItemList.ItemTypeEnum.NON_VEG);
            } else {
                item.setItemType(ItemList.ItemTypeEnum.VEG);
            }
            itemsList.add(item);
        }

        return itemsList;
    }

    public static CouponDetailsResponse toCouponDetailsResponse(CouponEntity couponEntity) {
        CouponDetailsResponse couponDetailsResponse = new CouponDetailsResponse()
                .id(UUID.fromString(couponEntity.getUuid()))
                .couponName(couponEntity.getCouponCode())
                .percent(couponEntity.getDiscountPercent());
        return couponDetailsResponse;
    }

}

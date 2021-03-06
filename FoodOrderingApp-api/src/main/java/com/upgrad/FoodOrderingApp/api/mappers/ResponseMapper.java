package com.upgrad.FoodOrderingApp.api.mappers;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.entity.*;

import java.util.ArrayList;
import java.util.Collections;
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

    public static List<OrderList> toOrderList(List<OrderEntity> allOrders) {

        List<OrderList> allOrdersList = new ArrayList<>();

        for(OrderEntity orderEntity: allOrders) {

            OrderList orderList = new OrderList();

            orderList.setId(UUID.fromString(orderEntity.getUuid()));
            orderList.setBill(orderEntity.getBillAmount());
            orderList.setDate(orderEntity.getDate().toString());
            orderList.setDiscount(orderEntity.getDiscount());

            OrderListAddress orderListAddress= new OrderListAddress();
            orderListAddress.setCity(orderEntity.getAddress().getCity());
            orderListAddress.setFlatBuildingName(orderEntity.getAddress().getHouseNumber());
            orderListAddress.setId(UUID.fromString(orderEntity.getAddress().getUuid()));
            orderListAddress.setLocality(orderEntity.getAddress().getLocality());
            orderListAddress.setPincode(orderEntity.getAddress().getPincode());

            OrderListAddressState orderListAddressState = new OrderListAddressState();
            orderListAddressState.setId(UUID.fromString(orderEntity.getAddress().getState().getUuid()));
            orderListAddressState.setStateName(orderEntity.getAddress().getState().getStateName());

            orderListAddress.setState(orderListAddressState);

            orderList.setAddress(orderListAddress);

            OrderListCoupon orderListCoupon = new OrderListCoupon();
            orderListCoupon.setId(UUID.fromString(orderEntity.getCouponCode().getUuid()));
            orderListCoupon.setCouponName(orderEntity.getCouponCode().getCouponCode());
            orderListCoupon.setPercent(orderEntity.getCouponCode().getDiscountPercent());

            orderList.setCoupon(orderListCoupon);

            OrderListCustomer orderListCustomer = new OrderListCustomer();
            orderListCustomer.setFirstName(orderEntity.getCustomer().getFirstName());
            orderListCustomer.setLastName(orderEntity.getCustomer().getLastName());
            orderListCustomer.setContactNumber(orderEntity.getCustomer().getContactNumber());
            orderListCustomer.setEmailAddress(orderEntity.getCustomer().getEmail());
            orderListCustomer.setId(UUID.fromString(orderEntity.getCustomer().getUuid()));

            orderList.setCustomer(orderListCustomer);

            OrderListPayment orderListPayment = new OrderListPayment();
            orderListPayment.setId(UUID.fromString(orderEntity.getPayment().getUuid()));
            orderListPayment.setPaymentName(orderEntity.getPayment().getPaymentType());

            orderList.setPayment(orderListPayment);

            List<ItemQuantityResponse> itemQuantityResponses = new ArrayList<>();

            for(OrderItem orderItem :orderEntity.getOrderItem()) {
                ItemQuantityResponse itemQuantityResponse = new ItemQuantityResponse();

                ItemQuantityResponseItem itemQuantityResponseItem = new ItemQuantityResponseItem();
                itemQuantityResponseItem.setId(UUID.fromString(orderItem.getItem().getUuid()));
                itemQuantityResponseItem.setItemName(orderItem.getItem().getItemName());
                itemQuantityResponseItem.setItemPrice(orderItem.getItem().getPrice());
                if(orderItem.getItem().getType().equals("1")) {
                    itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.NON_VEG);
                } else {
                    itemQuantityResponseItem.setType(ItemQuantityResponseItem.TypeEnum.VEG);
                }


                itemQuantityResponse.setItem(itemQuantityResponseItem);

                itemQuantityResponse.setPrice(orderItem.getPrice());
                itemQuantityResponse.setQuantity(orderItem.getQuantity());

                itemQuantityResponses.add(itemQuantityResponse);

            }

            orderList.setItemQuantities(itemQuantityResponses);

            allOrdersList.add(orderList);
        }

        return allOrdersList;
    }

    public static PaymentListResponse toPaymentListResponse(List<PaymentEntity> paymentEntities) {
       PaymentListResponse paymentListResponse = new PaymentListResponse();

       List<PaymentResponse> paymentResponses = new ArrayList<>();

       for(PaymentEntity paymentEntity: paymentEntities) {
           PaymentResponse paymentResponse = new PaymentResponse();

           paymentResponse.setId(UUID.fromString(paymentEntity.getUuid()));
           paymentResponse.setPaymentName(paymentEntity.getPaymentType());

           paymentResponses.add(paymentResponse);
       }

       paymentListResponse.setPaymentMethods(paymentResponses);

       return paymentListResponse;
    }

    public static CategoriesListResponse toCategoriesList(List<CategoryEntity> categoryEntities) {
        CategoriesListResponse categoriesListResponse = new CategoriesListResponse();

        for (CategoryEntity categoryEntity : categoryEntities) {
            categoriesListResponse.addCategoriesItem(new CategoryListResponse()
                    .id(UUID.fromString(categoryEntity.getUuid()))
                    .categoryName(categoryEntity.getCategory()));
        }

        return categoriesListResponse;
    }


    public static CategoryDetailsResponse toCategoriesDetailsResponse(CategoryEntity categoryEntity) {

        CategoryDetailsResponse categoryDetailsResponse = new CategoryDetailsResponse()
                .id(UUID.fromString(categoryEntity.getUuid()))
                .categoryName(categoryEntity.getCategory());

        for (ItemEntity itemEntity : categoryEntity.getItems()) {


            ItemList itemList = new ItemList()
                    .id(UUID.fromString(itemEntity.getUuid()))
                    .itemName(itemEntity.getItemName())
                    .price(itemEntity.getPrice());

            if(itemEntity.getType().equals("1")){
                itemList.setItemType(ItemList.ItemTypeEnum.NON_VEG);
            } else {
                itemList.setItemType(ItemList.ItemTypeEnum.VEG);
            }


            categoryDetailsResponse.addItemListItem(itemList);
        }

        return categoryDetailsResponse;
    }

    public static RestaurantListResponse toRestaurantList(List<RestaurantEntity> restaurantEntities) {
        RestaurantListResponse restaurantListResponse = new RestaurantListResponse();

        List<RestaurantList> restaurantLists = new ArrayList<>();

        for(RestaurantEntity restaurantEntity: restaurantEntities) {
            RestaurantList restaurantList = new RestaurantList();

            restaurantList.setId(UUID.fromString(restaurantEntity.getUuid()));
            restaurantList.setAveragePrice(restaurantEntity.getAvgPriceForTwo());
            restaurantList.setCustomerRating(restaurantEntity.getRating());
            restaurantList.setNumberCustomersRated(restaurantEntity.getCustomersRated());
            restaurantList.setPhotoURL(restaurantEntity.getImageUrl());
            restaurantList.setRestaurantName(restaurantEntity.getName());

            RestaurantDetailsResponseAddress restaurantDetailsResponseAddress = new RestaurantDetailsResponseAddress();
            restaurantDetailsResponseAddress.setId(UUID.fromString(restaurantEntity.getAddress().getUuid()));
            restaurantDetailsResponseAddress.setPincode(restaurantEntity.getAddress().getPincode());
            restaurantDetailsResponseAddress.setCity(restaurantEntity.getAddress().getCity());
            restaurantDetailsResponseAddress.setLocality(restaurantEntity.getAddress().getLocality());
            restaurantDetailsResponseAddress.setFlatBuildingName(restaurantEntity.getAddress().getHouseNumber());

            RestaurantDetailsResponseAddressState restaurantDetailsResponseAddressState = new RestaurantDetailsResponseAddressState();

            restaurantDetailsResponseAddressState.setId(UUID.fromString(restaurantEntity.getAddress().getState().getUuid()));
            restaurantDetailsResponseAddressState.setStateName(restaurantEntity.getAddress().getState().getStateName());

            restaurantDetailsResponseAddress.setState(restaurantDetailsResponseAddressState);

            restaurantList.setAddress(restaurantDetailsResponseAddress);

            String categories = getCategoryNamesAsSortedString(restaurantEntity.getCategories());

            restaurantList.setCategories(categories);

            restaurantLists.add(restaurantList);

        }

        restaurantListResponse.setRestaurants(restaurantLists);

        return restaurantListResponse;
    }

    private static String getCategoryNamesAsSortedString(List<CategoryEntity> categoryEntities) {
        List<String> categories = new ArrayList<>();

        for(CategoryEntity categoryEntity: categoryEntities) {
            categories.add(categoryEntity.getCategory());
        }

        Collections.sort(categories);

        return categories.toString();

    }

}

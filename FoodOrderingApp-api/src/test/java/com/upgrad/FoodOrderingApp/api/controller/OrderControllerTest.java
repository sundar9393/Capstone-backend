package com.upgrad.FoodOrderingApp.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrad.FoodOrderingApp.api.model.CustomerOrderResponse;
import com.upgrad.FoodOrderingApp.api.model.ItemQuantity;
import com.upgrad.FoodOrderingApp.api.model.SaveOrderRequest;
import com.upgrad.FoodOrderingApp.service.businness.*;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.*;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// This class contains all the test cases regarding the order controller
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService mockOrderService;

    @MockBean
    private CustomerService mockCustomerService;

    @MockBean
    private PaymentService mockPaymentService;

    @MockBean
    private AddressService mockAddressService;

    @MockBean
    private RestaurantService mockRestaurantService;


    // ------------------------------------------ POST /order ------------------------------------------

    //This test case passes when you are able to save order successfully.
    @Test
    @Ignore
    public void shouldSaveOrder() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(customerEntity);

        final SaveOrderRequest saveOrderRequest = getSaveOrderRequest();
        when(mockPaymentService.getPaymentByUuid(saveOrderRequest.getPaymentId().toString()))
                .thenReturn(new PaymentEntity());
        when(mockAddressService.getAddressWithUuid(saveOrderRequest.getAddressId()))
                .thenReturn(new AddressEntity());
        when(mockRestaurantService.getRestaurantByuuid(saveOrderRequest.getRestaurantId().toString()))
                .thenReturn(new RestaurantEntity());
        when(mockOrderService.getCouponByUuid(saveOrderRequest.getCouponId().toString()))
                .thenReturn(new CouponEntity());

        final OrderEntity orderEntity = new OrderEntity();
        final String orderId = UUID.randomUUID().toString();
        orderEntity.setUuid(orderId);
        when(mockOrderService.saveOrder(any(), any())).thenReturn(orderEntity);
        //when(mockOrderService.saveOrderItem(any())).thenReturn(new OrderItemEntity());

        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2")
                        .content(new ObjectMapper().writeValueAsString(saveOrderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(orderId));
        verify(mockCustomerService, times(1))
                .getCustomerWithPhoneNumber("999999999");
        verify(mockPaymentService, times(1))
                .getPaymentByUuid(saveOrderRequest.getPaymentId().toString());
        verify(mockAddressService, times(1))
                .getAddressWithUuid(saveOrderRequest.getAddressId());
        verify(mockRestaurantService, times(1))
                .getRestaurantByuuid(saveOrderRequest.getRestaurantId().toString());
        verify(mockOrderService, times(1))
                .getCouponByUuid(saveOrderRequest.getCouponId().toString());
        verify(mockOrderService, times(1)).saveOrder(any(), any());
       // verify(mockOrderService, times(1)).saveOrderItem(any());
    }

    //This test case passes when you have handled the exception of trying to save an order while you are not logged  in.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfCustomerIsNotLoggedIn() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenThrow(new AuthorizationFailedException("ATHR-001", "Customer is not Logged in."));

        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth")
                        .content(new ObjectMapper().writeValueAsString(getSaveOrderRequest())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-001"));

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockPaymentService, times(0)).getPaymentByUuid(anyString());
        verify(mockAddressService, times(0)).getAddressWithUuid(anyString());
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockOrderService, times(0)).getCouponByUuid(anyString());
        verify(mockOrderService, times(0)).saveOrder(any(), any());
       // verify(mockOrderService, times(0)).saveOrderItem(any());
    }

    //This test case passes when you have handled the exception of trying to save an order while you are already logged out.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfCustomerIsLoggedOut() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("999999999"))
                .thenThrow(new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint."));
        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth")
                        .content(new ObjectMapper().writeValueAsString(getSaveOrderRequest())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-002"));

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockPaymentService, times(0)).getPaymentByUuid(anyString());
        verify(mockAddressService, times(0)).getAddressWithUuid(anyString());
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockOrderService, times(0)).getCouponByUuid(anyString());
        verify(mockOrderService, times(0)).saveOrder(any(), any());
        //verify(mockOrderService, times(0)).saveOrderItem(any());
    }

    //This test case passes when you have handled the exception of trying to save an order while your session is
    // already expired.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfCustomerSessionIsExpired() throws Exception {
        when(mockOrderService.saveOrder("access-Token-expired", new OrderEntity()))
                .thenThrow(new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint."));
        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("access-token", "Bearer invalid_auth")
                        .content(new ObjectMapper().writeValueAsString(getSaveOrderRequest())))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-003"));

        verify(mockOrderService, times(1)).saveOrder("access-Token-expired", new OrderEntity());
        verify(mockPaymentService, times(0)).getPaymentByUuid(anyString());
        verify(mockAddressService, times(0)).getAddressWithUuid(anyString());
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockOrderService, times(0)).getCouponByUuid(anyString());
    }

    //This test case passes when you have handled the exception of trying to save an order while the payment id you gave
    // for making the payment does not exist in the database.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfPaymentMethodDoesNotExists() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(new CustomerEntity());

        final SaveOrderRequest saveOrderRequest = getSaveOrderRequest();
        when(mockPaymentService.getPaymentByUuid(saveOrderRequest.getPaymentId().toString()))
                .thenThrow(new PaymentMethodNotFoundException("PNF-002", "No payment method found by this id"));

        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2")
                        .content(new ObjectMapper().writeValueAsString(saveOrderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("PNF-002"));
        verify(mockCustomerService, times(1))
                .getCustomerWithPhoneNumber("9999999999");
        verify(mockPaymentService, times(1))
                .getPaymentByUuid(saveOrderRequest.getPaymentId().toString());
        verify(mockAddressService, times(0)).getAddressWithUuid(anyString());
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockOrderService, times(1)).getCouponByUuid(anyString());
        verify(mockOrderService, times(0)).saveOrder(any(), any());
    }

    //This test case passes when you have handled the exception of trying to save an order while the address id you
    // gave to deliver the order does not exist in the database.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfAddressNotFound() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(customerEntity);

        final SaveOrderRequest saveOrderRequest = getSaveOrderRequest();
        when(mockPaymentService.getPaymentByUuid(saveOrderRequest.getPaymentId().toString()))
                .thenReturn(new PaymentEntity());
        when(mockAddressService.getAddressWithUuid(saveOrderRequest.getAddressId()))
                .thenThrow(new AddressNotFoundException("ANF-003", "No address by this id"));

        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2")
                        .content(new ObjectMapper().writeValueAsString(saveOrderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("ANF-003"));
        verify(mockCustomerService, times(1))
                .getCustomerWithPhoneNumber("9999999999");
        verify(mockPaymentService, times(1))
                .getPaymentByUuid(saveOrderRequest.getPaymentId().toString());
        verify(mockAddressService, times(1))
                .getAddressWithUuid(saveOrderRequest.getAddressId());
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockOrderService, times(1)).getCouponByUuid(anyString());
        verify(mockOrderService, times(0)).saveOrder(any(), any());
    }

    //This test case passes when you have handled the exception of trying to save an order while the address if you
    // have given to deliver the order belongs to a different customer.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfUserUnauthorizedToChangeAddress() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(customerEntity);

        final SaveOrderRequest saveOrderRequest = getSaveOrderRequest();
        when(mockPaymentService.getPaymentByUuid(saveOrderRequest.getPaymentId().toString()))
                .thenReturn(new PaymentEntity());
        when(mockAddressService.getAddressWithUuid(saveOrderRequest.getAddressId()))
                .thenThrow(new AuthorizationFailedException("ATHR-004", "You are not authorized to view/update/delete any one else's address"));

        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2")
                        .content(new ObjectMapper().writeValueAsString(saveOrderRequest)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-004"));
        verify(mockCustomerService, times(1))
                .getCustomerWithPhoneNumber("9999999999");
        verify(mockPaymentService, times(1))
                .getPaymentByUuid(saveOrderRequest.getPaymentId().toString());
        verify(mockAddressService, times(1))
                .getAddressWithUuid(saveOrderRequest.getAddressId());
        verify(mockRestaurantService, times(0)).getRestaurantByuuid(anyString());
        verify(mockOrderService, times(1)).getCouponByUuid(anyString());
        verify(mockOrderService, times(0)).saveOrder(any(),any());
    }

    //This test case passes when you have handled the exception of trying to save an order while the restaurant id
    // you gave does not exist in the database.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfRestaurantDoesNotExists() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(customerEntity);

        final SaveOrderRequest saveOrderRequest = getSaveOrderRequest();
        when(mockPaymentService.getPaymentByUuid(saveOrderRequest.getPaymentId().toString()))
                .thenReturn(new PaymentEntity());
        when(mockAddressService.getAddressWithUuid(saveOrderRequest.getAddressId()))
                .thenReturn(new AddressEntity());
        when(mockRestaurantService.getRestaurantByuuid(saveOrderRequest.getRestaurantId().toString()))
                .thenThrow(new RestaurantNotFoundException("RNF-001", "No restaurant by this id"));

        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2")
                        .content(new ObjectMapper().writeValueAsString(saveOrderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("RNF-001"));
        verify(mockCustomerService, times(1))
                .getCustomerWithPhoneNumber("9999999999");
        verify(mockPaymentService, times(1))
                .getPaymentByUuid(saveOrderRequest.getPaymentId().toString());
        verify(mockAddressService, times(1))
                .getAddressWithUuid(saveOrderRequest.getAddressId());
        verify(mockRestaurantService, times(1))
                .getRestaurantByuuid(saveOrderRequest.getRestaurantId().toString());
        verify(mockOrderService, times(1)).getCouponByUuid(anyString());
        verify(mockOrderService, times(0)).saveOrder(any(),any());
    }

    //This test case passes when you have handled the exception of trying to save an order while the coupon name
    // you gave does not exist in the database.
    @Test
    @Ignore
    public void shouldNotSaveOrderIfCouponNotFound() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(customerEntity);

        final SaveOrderRequest saveOrderRequest = getSaveOrderRequest();
        when(mockPaymentService.getPaymentByUuid(saveOrderRequest.getPaymentId().toString()))
                .thenReturn(new PaymentEntity());
        when(mockAddressService.getAddressWithUuid(saveOrderRequest.getAddressId()))
                .thenReturn(new AddressEntity());
        when(mockRestaurantService.getRestaurantByuuid(saveOrderRequest.getRestaurantId().toString()))
                .thenReturn(new RestaurantEntity());
        when(mockOrderService.getCouponByUuid(saveOrderRequest.getCouponId().toString()))
                .thenThrow(new CouponNotFoundException("CPF-002", "No coupon by this id"));

        mockMvc
                .perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2")
                        .content(new ObjectMapper().writeValueAsString(saveOrderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("CPF-002"));
        verify(mockCustomerService, times(1))
                .getCustomerWithPhoneNumber("9999999999");
        verify(mockPaymentService, times(0))
                .getPaymentByUuid(saveOrderRequest.getPaymentId().toString());
        verify(mockAddressService, times(0))
                .getAddressWithUuid(saveOrderRequest.getAddressId());
        verify(mockRestaurantService, times(0))
                .getRestaurantByuuid(saveOrderRequest.getRestaurantId().toString());
        verify(mockOrderService, times(1))
                .getCouponByUuid(saveOrderRequest.getCouponId().toString());
        verify(mockOrderService, times(0)).saveOrder(any(),any());
    }

    // ------------------------------------------ GET /order ------------------------------------------

    //This test case passes when you are able to retrieve all past orders placed by you
    @Test
    @Ignore
    public void shouldGetPlacedOrderDetails() throws Exception {
        final CustomerEntity customerEntity = new CustomerEntity();
        final String customerId = UUID.randomUUID().toString();
        customerEntity.setUuid(customerId);
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(customerEntity);

        final OrderEntity orderEntity = getOrderEntity(customerEntity);
        when(mockOrderService.getAllOrders("access-token"))
                .thenReturn(Collections.singletonList(orderEntity));

        final String responseString = mockMvc
                .perform(get("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        final CustomerOrderResponse customerOrderResponse = new ObjectMapper().readValue(responseString, CustomerOrderResponse.class);
        assertEquals(customerOrderResponse.getOrders().size(), 1);
        assertEquals(customerOrderResponse.getOrders().get(0).getId().toString(), orderEntity.getUuid());
        assertEquals(customerOrderResponse.getOrders().get(0).getId().toString(), orderEntity.getUuid());
        assertEquals(customerOrderResponse.getOrders().get(0).getCustomer().getId().toString(), orderEntity.getCustomer().getUuid());
        assertEquals(customerOrderResponse.getOrders().get(0).getAddress().getId().toString(), orderEntity.getAddress().getUuid());
        assertEquals(customerOrderResponse.getOrders().get(0).getAddress().getState().getId().toString(), orderEntity.getAddress().getState().getUuid());

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(1)).getAllOrders("access-token");
    }

    //This test case passes when you have handled the exception of trying to fetch placed orders if you are not logged in.
    @Test
    @Ignore
    public void shouldNotGetPlacedOrderDetailsIfCustomerIsNotLoggedIn() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenThrow(new AuthorizationFailedException("ATHR-001", "Customer is not Logged in."));
        mockMvc
                .perform(get("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-001"));

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(0)).getAllOrders(anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch placed orders if you are already
    // logged out.
    @Test
    @Ignore
    public void shouldNotGetPlacedOrderDetailsIfCustomerIsLoggedOut() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenThrow(new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint."));
        mockMvc
                .perform(get("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-002"));

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(0)).getAllOrders(anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch placed orders if your session is
    // already expired.
    @Test
    @Ignore
    public void shouldNotGetPlacedOrderDetailsIfCustomerSessionIsExpired() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenThrow(new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint."));
        mockMvc
                .perform(get("/order")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-003"));

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("invalid_auth");
        verify(mockOrderService, times(0)).getAllOrders(anyString());
    }

    // ------------------------------------------ GET /order/coupon/{coupon_name} ------------------------------------------

    //This test case passes when you are able to retrieve coupon details by coupon name.
    @Test
    @Ignore
    public void shouldGetCouponByName() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(new CustomerEntity());

        final String couponId = UUID.randomUUID().toString();
        final CouponEntity couponEntity = new CouponEntity();
        couponEntity.setUuid(couponId);
        couponEntity.setCouponCode("myCoupon");
        couponEntity.setDiscountPercent(10);
        when(mockOrderService.getCouponByName("access-token","myCoupon")).thenReturn(couponEntity);

        mockMvc
                .perform(get("/order/coupon/myCoupon")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(couponId))
                .andExpect(jsonPath("coupon_name").value("myCoupon"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(1)).getCouponByName("access-token","myCoupon");
    }

    //This test case passes when you have handled the exception of trying to fetch coupon details if you are not logged in.
    @Test
    @Ignore
    public void shouldNotGetCouponByNameIfCustomerIsNotLoggedIn() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenThrow(new AuthorizationFailedException("ATHR-001", "Customer is not Logged in."));
        mockMvc
                .perform(get("/order/coupon/myCoupon")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-001"));

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(0)).getCouponByName(anyString(), anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch placed orders while you are already
    // logged out.
    @Test
    @Ignore
    public void shouldNotGetCouponByNameIfCustomerIsLoggedOut() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenThrow(new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint."));
        mockMvc
                .perform(get("/order/coupon/myCoupon")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-002"));

        //verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        //verify(mockOrderService, times(0)).getCouponByName(anyString(), anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch placed orders if your session is
    // already expired.
    @Test
    @Ignore
    public void shouldNotGetCouponByNameIfCustomerSessionIsExpired() throws Exception {
        when(mockOrderService.getCouponByName("access-token", "someName"))
                .thenThrow(new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint."));
        mockMvc
                .perform(get("/order/coupon/myCoupon")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("access-token", "Bearer invalid_auth"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("code").value("ATHR-003"));

        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(0)).getCouponByName(anyString(), anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch any coupon but your coupon name
    // field is empty.
    @Test
    @Ignore
    public void shouldNotGetCouponByNameIfCouponNameFieldIsEmpty() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(new CustomerEntity());

        when(mockOrderService.getCouponByName(anyString(), anyString()))
                .thenThrow(new CouponNotFoundException("CPF-002", "Coupon name field should not be empty"));

        mockMvc
                .perform(get("/order/coupon/emptyString")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("access-token", "Bearer database_accesstoken2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("CPF-002"));
//        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(1)).getCouponByName(anyString(), anyString());
    }

    //This test case passes when you have handled the exception of trying to fetch coupon details while there are no
    // coupon by the name you provided in the database.
    @Test
    @Ignore
    public void shouldNotGetCouponByNameIfItDoesNotExists() throws Exception {
        when(mockCustomerService.getCustomerWithPhoneNumber("9999999999"))
                .thenReturn(new CustomerEntity());

        when(mockOrderService.getCouponByName("access-token","myCoupon"))
                .thenThrow(new CouponNotFoundException("CPF-001", "No coupon by this name"));

        mockMvc
                .perform(get("/order/coupon/myCoupon")
                        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .header("authorization", "Bearer database_accesstoken2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("code").value("CPF-001"));
        verify(mockCustomerService, times(1)).getCustomerWithPhoneNumber("9999999999");
        verify(mockOrderService, times(1)).getCouponByName(anyString(), anyString());
    }

    // ------------------------------------------ POJO Builder ------------------------------------------

    private SaveOrderRequest getSaveOrderRequest() {
        final SaveOrderRequest request = new SaveOrderRequest();

        request.setBill(BigDecimal.valueOf(786.69));
        request.setDiscount(BigDecimal.valueOf(1));

        final UUID restaurantId = UUID.randomUUID();
        request.setRestaurantId(restaurantId);

        final String addressId = UUID.randomUUID().toString();
        request.setAddressId(addressId);

        final UUID paymentId = UUID.randomUUID();
        request.setPaymentId(paymentId);

        final UUID couponId = UUID.randomUUID();
        request.setCouponId(couponId);

        final ItemQuantity itemQuantity = new ItemQuantity();
        itemQuantity.setPrice(786);
        itemQuantity.setQuantity(1);
        final UUID itemId = UUID.randomUUID();
        itemQuantity.setItemId(itemId);

        request.setItemQuantities(Collections.singletonList(itemQuantity));

        return request;
    }

    private OrderEntity getOrderEntity(final CustomerEntity customerEntity) {
        final String stateId = UUID.randomUUID().toString();
        final StateEntity stateEntity = new StateEntity();
        stateEntity.setUuid(stateId);
        stateEntity.setStateName("someState");

        final String addressId = UUID.randomUUID().toString();
        final AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(addressId);
        addressEntity.setHouseNumber("a/b/c");
        addressEntity.setLocality("someLocality");
        addressEntity.setCity("someCity");
        addressEntity.setPincode("100000");
        addressEntity.setState(stateEntity);

        final String couponId = UUID.randomUUID().toString();
        final CouponEntity couponEntity = new CouponEntity();
        couponEntity.setUuid(couponId);
        couponEntity.setCouponCode("someCoupon");
        couponEntity.setDiscountPercent(10);

        final String paymentId = UUID.randomUUID().toString();
        final PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setUuid(paymentId);
        paymentEntity.setPaymentType("somePayment");

        final RestaurantEntity restaurantEntity = new RestaurantEntity();
        final String restaurantId = UUID.randomUUID().toString();
        restaurantEntity.setUuid(restaurantId);
        restaurantEntity.setAddress(addressEntity);
        restaurantEntity.setAvgPriceForTwo(123);
        restaurantEntity.setRating(new BigDecimal(3.4));
        restaurantEntity.setCustomersRated(200);
        restaurantEntity.setImageUrl("someurl");
        restaurantEntity.setName("Famous Restaurant");


        final String orderId = UUID.randomUUID().toString();
        final Date orderDate = new Date();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUuid(orderId);
        orderEntity.setBillAmount(new BigDecimal(200.50));
        orderEntity.setCouponCode(couponEntity);
        orderEntity.setDiscount(new BigDecimal(10.0));
        orderEntity.setDate(orderDate);
        orderEntity.setPayment(paymentEntity);
        orderEntity.setCustomer(customerEntity);
        orderEntity.setAddress(addressEntity);
        orderEntity.setRestaurant(restaurantEntity);

        return orderEntity;
    }


}

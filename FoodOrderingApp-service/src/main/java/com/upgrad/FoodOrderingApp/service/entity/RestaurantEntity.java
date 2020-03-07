package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "restaurant")
@NamedQueries(
        {
                @NamedQuery(name = "getAllRestaurants", query = "SELECT r from RestaurantEntity r"),
                @NamedQuery(name = "getRestaurantByName", query = "SELECT r from RestaurantEntity r where lower(r.name) like :name"),
                @NamedQuery(name="getRestaurantsByCategory", query = "SELECT r from RestaurantEntity r join r.categories category where category.category = :categoryName "),
                @NamedQuery(name = "getRestaurantByuuid", query = "select r from RestaurantEntity r where r.uuid = :uuid")
        }
)
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "restaurant_name")
    @NotNull
    private String name;

    @Column(name = "photo_url")
    @NotNull
    private String imageUrl;

    @Column(name = "customer_rating")
    @NotNull
    private BigDecimal rating;

    @Column(name = "average_price_for_two")
    @NotNull
    private Integer avgPriceForTwo;

    @Column(name = "number_of_customers_rated")
    @NotNull
    private Integer customersRated;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    @NotNull
    AddressEntity address;

    @ManyToMany()
    @JoinTable(
            name = "restaurant_category",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    List<CategoryEntity> categories = new ArrayList<>();

    @ManyToMany()
    @JoinTable(
            name = "restaurant_item",
            joinColumns = {@JoinColumn(name = "restaurant_id")},
            inverseJoinColumns = {@JoinColumn(name = "item_id")}
    )
    List<ItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    Set<OrderEntity> orders = new HashSet<>();

    public RestaurantEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getRating() {
        return rating;
    }

    public void setRating(BigDecimal rating) {
        this.rating = rating;
    }

    public Integer getAvgPriceForTwo() {
        return avgPriceForTwo;
    }

    public void setAvgPriceForTwo(Integer avgPriceForTwo) {
        this.avgPriceForTwo = avgPriceForTwo;
    }

    public Integer getCustomersRated() {
        return customersRated;
    }

    public void setCustomersRated(Integer customersRated) {
        this.customersRated = customersRated;
    }

    public AddressEntity getAddress() {
        return address;
    }

    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemEntity> items) {
        this.items = items;
    }

    public Set<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderEntity> orders) {
        this.orders = orders;
    }
}

package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "item")
@NamedQuery(name = "getItemByUuid", query = "select i from ItemEntity i where i.uuid = :uuid")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "item_name")
    @NotNull
    private String itemName;

    @Column(name = "price")
    @NotNull
    private Integer price;

    @Column(name = "type")
    @NotNull
    private String type;

    @ManyToMany()
    @JoinTable(
            name = "category_item",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")}
    )
    List<CategoryEntity> categories = new ArrayList<>();

    @ManyToMany(mappedBy = "items")
    List<RestaurantEntity> restaurants = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    Set<OrderItem> orderItem = new HashSet<>();

    public ItemEntity() {

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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<CategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryEntity> categories) {
        this.categories = categories;
    }

    public List<RestaurantEntity> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<RestaurantEntity> restaurants) {
        this.restaurants = restaurants;
    }

    public Set<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(Set<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }
}

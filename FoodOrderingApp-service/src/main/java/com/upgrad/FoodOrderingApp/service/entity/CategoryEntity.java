package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class CategoryEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "category_name")
    @NotNull
    private String category;

    @ManyToMany(mappedBy = "categories")
    private List<RestaurantEntity> restaurants = new ArrayList<>();

    public CategoryEntity() {

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<RestaurantEntity> getRestaurants() {
        return restaurants;
    }

    public void addRestaurants(RestaurantEntity restaurant) {
        this.restaurants.add(restaurant);
    }
}

package com.upgrad.FoodOrderingApp.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "coupon")
@NamedQuery(name = "getCouponWithName", query = "select c from CouponEntity c where lower(c.couponCode) = :coupon")
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "coupon_name")
    @NotNull
    private String couponCode;

    @Column(name = "percent")
    @NotNull
    private Integer discountPercent;

    public CouponEntity() {
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

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(Integer discountPercent) {
        this.discountPercent = discountPercent;
    }
}

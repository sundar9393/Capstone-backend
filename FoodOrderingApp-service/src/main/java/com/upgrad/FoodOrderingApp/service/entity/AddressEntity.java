package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "address")
@NamedQueries(
        {
                @NamedQuery(name = "getAllAddressOrdered", query = "SELECT a from AddressEntity a order by a.id desc ")
        }
)
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "flat_buil_number")
    @NotNull
    private String houseNumber;

    @Column(name = "locality")
    @NotNull
    private String locality;

    @Column(name = "city")
    @NotNull
    private String city;

    @Column(name = "pincode")
    @NotNull
    private String pincode;

    @ManyToOne
    @JoinColumn(name = "state_id")
    @NotNull
    private StateEntity state;

    @Column(name = "active")
    private Integer status;



    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_address",
            joinColumns = {@JoinColumn(name = "address_id")},
            inverseJoinColumns = {@JoinColumn(name = "customer_id")}
    )
    private List<CustomerEntity> customers = new ArrayList<>();

    public AddressEntity() {

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

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public StateEntity getState() {
        return state;
    }

    public void setState(StateEntity state) {
        this.state = state;
    }

    public List<CustomerEntity> getCustomers() {
        return customers;
    }

    public void setCustomers(CustomerEntity customer) {
        this.customers.add(customer);
    }
}

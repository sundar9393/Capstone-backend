package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@NamedQueries(
        {
           @NamedQuery(name = "customerByContactNumber", query = "SELECT c from CustomerEntity c where c.contactNumber = :contactNumber")
        }
)
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "uuid")
    @NotNull
    private String uuid;

    @Column(name = "firstname")
    @NotNull
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "email")
    @NotNull
    private String email;

    @Column(name = "contact_number")
    @NotNull
    private String contactNumber;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "salt")
    @NotNull
    private String salt;

    //Setting mapping between Customer and Customer auth token
    @OneToMany(mappedBy = "customer")
    private List<CustomerAuthTokenEntity> authTokens;

    @ManyToMany(mappedBy = "customers")
    private List<AddressEntity> addresses = new ArrayList<>();

    @OneToMany(mappedBy = "customer")
    private List<OrderEntity> orders;

    public CustomerEntity() {

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public List<CustomerAuthTokenEntity> getAuthTokens() {
        return authTokens;
    }

    public void setAuthTokens(List<CustomerAuthTokenEntity> authTokens) {
        this.authTokens = authTokens;
    }

    public List<AddressEntity> getAddresses() {
        return addresses;
    }

    public void setAddresses(AddressEntity address) {
        this.addresses.add(address);
    }

    public void setAddresses(List<AddressEntity> addresses) {
        this.addresses = addresses;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }
}

package com.upgrad.FoodOrderingApp.service.dao;


//This interacts with the service class and hits the DB

import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AddressDao {

    @PersistenceContext
    EntityManager entityManager;

    public StateEntity getStateWithUuid(String uuid) {
        try {
           return entityManager.createNamedQuery("getStateWithUuid",StateEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }

    }

    public AddressEntity saveAddress(AddressEntity addressEntity) {
        entityManager.persist(addressEntity);
        return addressEntity;
    }

    public List<AddressEntity> getAllAddresses() {
        return entityManager.createNamedQuery("getAllAddressOrdered", AddressEntity.class).getResultList();
    }

    public List<StateEntity> getAllStates() {
        return entityManager.createNamedQuery("getAllStates", StateEntity.class).getResultList();
    }

    public AddressEntity getAddressWithUuid(String addrUuid) {
        try {
            return entityManager.createNamedQuery("getAddressWithUuid", AddressEntity.class).setParameter("uuid",addrUuid).getSingleResult();
        } catch(NoResultException nre) {
            return null;
        }

    }

    public AddressEntity deleteAddress(AddressEntity addressEntity) {
        entityManager.remove(addressEntity);
        return addressEntity;
    }

    public AddressEntity updateAddress(AddressEntity addressEntity) {
        return entityManager.merge(addressEntity);
    }

}

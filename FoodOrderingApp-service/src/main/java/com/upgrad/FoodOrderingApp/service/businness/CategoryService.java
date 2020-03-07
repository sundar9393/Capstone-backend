package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.CategoryDao;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDao;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import com.upgrad.FoodOrderingApp.service.util.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    CustomerDao customerDao;

    @Autowired
    CategoryDao categoryDao;

    public List<CategoryEntity> getAllCategories(String accessToken) throws AuthorizationFailedException {
        ServiceUtil.validateAuthToken(customerDao.getAuthTokenWithAccessToken(accessToken));

        return categoryDao.getAllCategories();
    }

    public CategoryEntity getCategoryByUuid(String accessToken, String uuid) throws AuthorizationFailedException,
            CategoryNotFoundException {
        ServiceUtil.validateAuthToken(customerDao.getAuthTokenWithAccessToken(accessToken));

        if (uuid.isEmpty()) {
            throw new CategoryNotFoundException("CNF-001", "Category id field should not be empty");
        } else {
            CategoryEntity categoryEntity = categoryDao.getCategoryByUuid(uuid);

            if (categoryEntity == null) {
                throw new CategoryNotFoundException("CNF-002", "No category by this id");
            } else {
                return categoryEntity;
            }

        }

    }

}
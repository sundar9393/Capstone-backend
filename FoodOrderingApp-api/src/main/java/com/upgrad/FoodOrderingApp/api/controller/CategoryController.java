package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.Util.Utility;
import com.upgrad.FoodOrderingApp.api.mappers.ResponseMapper;
import com.upgrad.FoodOrderingApp.api.model.CategoriesListResponse;
import com.upgrad.FoodOrderingApp.api.model.CategoryDetailsResponse;
import com.upgrad.FoodOrderingApp.service.businness.CategoryService;
import com.upgrad.FoodOrderingApp.service.entity.CategoryEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(method = RequestMethod.GET, path = "/category", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoriesListResponse> getAllCategories(@RequestHeader(name = "access-token") final String accessToken)
            throws AuthorizationFailedException {
        String token = Utility.getAccessTokenFromHeader(accessToken);
        List<CategoryEntity> categories = categoryService.getAllCategories(token);
        CategoriesListResponse categoriesListResponse = ResponseMapper.toCategoriesList(categories);

        return new ResponseEntity<CategoriesListResponse>(categoriesListResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/category/{category_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<CategoryDetailsResponse> getCategoryById(@RequestHeader(name = "access-token") final String accessToken,
            @PathVariable(name = "category_id") final String categoryId) throws AuthorizationFailedException, CategoryNotFoundException {
        String token = Utility.getAccessTokenFromHeader(accessToken);
        CategoryEntity categoryEntity = categoryService.getCategoryByUuid(token, categoryId);
        CategoryDetailsResponse categoryDetailsResponse = ResponseMapper.toCategoriesDetailsResponse(categoryEntity);

        return new ResponseEntity<CategoryDetailsResponse>(categoryDetailsResponse, HttpStatus.OK);
    }

}
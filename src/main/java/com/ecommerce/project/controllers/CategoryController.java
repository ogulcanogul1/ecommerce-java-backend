package com.ecommerce.project.controllers;

import com.ecommerce.project.dtos.categoryDtos.CreateCategoryDto;
import com.ecommerce.project.dtos.categoryDtos.ResponseCategoryDto;
import com.ecommerce.project.dtos.categoryDtos.UpdateCategoryDto;
import com.ecommerce.project.constants.AppConstants;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.categoryService.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
       this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<ServiceResult<ResponseCategoryDto>> getCategories(
            @RequestParam(value = "pageNumber",required = false) Integer pageNumber,
            @RequestParam(value = "pageSize",required = false) Integer pageSize,
            @RequestParam(value = "sortDirection",required = false , defaultValue = AppConstants.DEFAULT_ORDER_SORT_DIRECTION) String sortDirection,
            @RequestParam(value = "sortBy",required = false, defaultValue = AppConstants.DEFAULT_ORDER_BY) String sortBy) {
        return new ResponseEntity<>(categoryService.getCategories(pageNumber,pageSize,sortDirection,sortBy),HttpStatus.OK);
    }

    @PostMapping("/public/categories")
    public ResponseEntity<ServiceResult<String>> addCategory(@Valid @RequestBody CreateCategoryDto category) {
        return new ResponseEntity<>(categoryService.addCategory(category), HttpStatus.CREATED);
    }

    @DeleteMapping("/public/categories/{id}")
    public ResponseEntity<ServiceResult<String>> deleteCategories(@PathVariable Long id){
        return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
    }

    @PutMapping("/public/categories/{id}")
    public ResponseEntity<ServiceResult<Category>> updateCategory(@PathVariable Long id,@Valid @RequestBody UpdateCategoryDto categoryRequest){
        return ResponseEntity.ok(categoryService.updateCategory(id,categoryRequest));
    }

}

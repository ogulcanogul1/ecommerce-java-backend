package com.ecommerce.project.services.categoryService;

import com.ecommerce.project.dtos.categoryDtos.CreateCategoryDto;
import com.ecommerce.project.dtos.categoryDtos.ResponseCategoryDto;
import com.ecommerce.project.dtos.categoryDtos.UpdateCategoryDto;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.result.ServiceResult;

public interface CategoryService {

    ServiceResult<ResponseCategoryDto> getCategories(Integer pageNumber, Integer pageSize,String sortDirection, String sortBy);

    ServiceResult<String> addCategory(CreateCategoryDto category);

    ServiceResult<String> deleteCategory(Long categoryId);

    ServiceResult<Category> updateCategory(Long categoryId , UpdateCategoryDto categoryRequest);
}

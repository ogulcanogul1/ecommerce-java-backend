package com.ecommerce.project.services.categoryService;

import com.ecommerce.project.dtos.*;
import com.ecommerce.project.dtos.categoryDtos.CategoryDto;
import com.ecommerce.project.dtos.categoryDtos.CreateCategoryDto;
import com.ecommerce.project.dtos.categoryDtos.ResponseCategoryDto;
import com.ecommerce.project.dtos.categoryDtos.UpdateCategoryDto;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.models.Category;
import com.ecommerce.project.repositories.CategoryRepository;
import com.ecommerce.project.result.ServiceResult;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public ServiceResult<ResponseCategoryDto> getCategories(Integer pageNumber, Integer pageSize, String sortDirection, String sortBy) {

        Sort sort = sortDirection.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        if (pageNumber == null || pageSize == null) {
            List<Category> categories = categoryRepository.findAll(sort);

            if (categories.isEmpty())
                throw new APIException("No Category created yet!");

            List<CategoryDto> categoryDto = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryDto.class))
                    .toList();

            ResponseCategoryDto responseCategoryDto = new ResponseCategoryDto(
                    categoryDto,
                    categoryDto.stream().count(),
                    null
            );
            return ServiceResult.success(responseCategoryDto, "all categories");
        }


        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize , sort);
        Page<Category> pageCategories = categoryRepository.findAll(pageable);

        List<CategoryDto> categoryDto = pageCategories.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .toList();

        PaginationInfo paginationInfo = new PaginationInfo(
                pageNumber,
                pageSize,
                pageCategories.getTotalPages()
        );

        ResponseCategoryDto responseCategoryDto = new ResponseCategoryDto(
                categoryDto,
                pageCategories.getTotalElements(),
                paginationInfo
        );
        return ServiceResult.success(responseCategoryDto, "get categories by page");
    }

    @Override
    public ServiceResult<String> addCategory(CreateCategoryDto categoryDto) {

        Category resultCategory = categoryRepository.findByCategoryName(categoryDto.getCategoryName());

        if(resultCategory != null) {
            throw new APIException("Category with this name already exists!");
        }

        //Category mapCategory = new Category(null,categoryDto.getCategoryName());

        Category mapCategory = modelMapper.map(categoryDto,Category.class);


        categoryRepository.save(mapCategory);
        return ServiceResult.success("Category added successfully!");
    }

    @Override
    public ServiceResult<String> deleteCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));


        categoryRepository.delete(category);
        return ServiceResult.success("Category with categoryId : " + categoryId + " deleted successfully!");
    }

    @Override
    public ServiceResult<Category> updateCategory(Long categoryId , UpdateCategoryDto categoryRequest) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        category.setCategoryName(categoryRequest.getCategoryName());

        return ServiceResult.success(categoryRepository.save(category),"Category updated successfully!");
    }
}

package com.ecommerce.project.dtos.categoryDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryDto {
    @NotBlank(message = "Category name cannot be blank")
    private String categoryName;
}

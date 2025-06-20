package com.ecommerce.project.dtos.productDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateProductDto {

    @NotBlank(message = "Product name cannot be blank")
    private String productName;
    private String description;
    private String imageUrl;

    @NotNull
    @Min(value = 1, message = "Category id must be greater than 0")
    private Integer quantity;

    @Min(value = 1, message = "Price must be greater than 0")
    private Double price;
}

package com.ecommerce.project.dtos.productDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String productName;
    private String imageUrl;
    private String description;
    private Integer quantity;
    private Double price;
}

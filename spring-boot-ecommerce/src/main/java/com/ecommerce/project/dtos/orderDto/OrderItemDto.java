package com.ecommerce.project.dtos.orderDto;

import com.ecommerce.project.dtos.productDtos.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private Long orderItemId;
    private ProductDto product;
    private Integer quantity;
    private double discount;
    private double orderedProductPrice;
}
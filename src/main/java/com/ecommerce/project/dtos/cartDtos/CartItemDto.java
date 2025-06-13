package com.ecommerce.project.dtos.cartDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private String productName;
    private Double productPrice;
    private Double productDiscountPrice;
    private Integer quantity;
    private Integer percentageOfDiscount;
    private Double cartItemTotalPrice;
}

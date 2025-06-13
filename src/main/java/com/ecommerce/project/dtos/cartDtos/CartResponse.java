package com.ecommerce.project.dtos.cartDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartResponse {
    private Long cartId;
    private Double totalPrice = 0.0;
    private List<CartItemDto> cartItems = new ArrayList<>();
}

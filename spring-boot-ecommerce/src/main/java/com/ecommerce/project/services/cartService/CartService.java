package com.ecommerce.project.services.cartService;


import com.ecommerce.project.dtos.cartDtos.CartItemDto;
import com.ecommerce.project.dtos.cartDtos.CartResponse;
import com.ecommerce.project.result.ServiceResult;

import java.util.List;

public interface CartService {

    ServiceResult<CartResponse> addProductToCart(Long productId, Integer quantity);

    ServiceResult<List<CartResponse>> getAllCarts(Integer pageNumber, Integer pageSize, String sortDirection,String sortBy);

    ServiceResult<CartResponse> getCartOfSignedUser();

    ServiceResult<CartItemDto> updateQuantityInCartItem(Long productId, int operationValue);

    ServiceResult<CartItemDto> deleteProductFromCart(Long productId);
}

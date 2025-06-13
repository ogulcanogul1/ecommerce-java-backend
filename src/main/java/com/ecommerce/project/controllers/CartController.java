package com.ecommerce.project.controllers;


import com.ecommerce.project.constants.AppConstants;
import com.ecommerce.project.dtos.cartDtos.CartItemDto;
import com.ecommerce.project.dtos.cartDtos.CartResponse;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.cartService.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<ServiceResult<CartResponse>> addProductToCart(@PathVariable Long productId,
                                                                        @PathVariable Integer quantity){
        ServiceResult<CartResponse> cartResponse = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartResponse, HttpStatus.OK);
    }

    @RequestMapping("/carts")
    public ResponseEntity<ServiceResult<List<CartResponse>>> getCarts(
            @RequestParam(name = "pageNumber",required = false) Integer pageNumber,
            @RequestParam(name = "pageSize" , required = false) Integer pageSize,
            @RequestParam(name = "sortDirection" , required = false , defaultValue = AppConstants.DEFAULT_ORDER_SORT_DIRECTION) String sortDirection,
            @RequestParam(name = "sortBy" , required = false , defaultValue = AppConstants.DEFAULT_CART_ORDER_BY) String sortBy
    ){
        ServiceResult<List<CartResponse>> cartResponse = cartService.getAllCarts(pageNumber,pageSize,sortDirection,sortBy);
        return new ResponseEntity<>(cartResponse,HttpStatus.OK);
    }

    @RequestMapping("/carts/user")
    public ResponseEntity<ServiceResult<CartResponse>> getCartOfSignedInUser(){
        return new ResponseEntity<>(cartService.getCartOfSignedUser(),HttpStatus.OK);
    }

    @PutMapping("/carts/products/{productId}/operation/{operation}")
    public ResponseEntity<ServiceResult<CartItemDto>> updateQuantityInCartItem(@PathVariable Long productId,
                                                                               @PathVariable String operation){
        ServiceResult<CartItemDto> dto = cartService.updateQuantityInCartItem(productId, operation.equalsIgnoreCase("delete") ? -1 : 1);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/carts/products/{productId}")
    public ResponseEntity<ServiceResult<CartItemDto>> deleteProductFromCart(@PathVariable Long productId){
        ServiceResult<CartItemDto> dto = cartService.deleteProductFromCart(productId);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}

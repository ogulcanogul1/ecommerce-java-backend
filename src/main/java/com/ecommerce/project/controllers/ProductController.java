package com.ecommerce.project.controllers;

import com.ecommerce.project.constants.AppConstants;
import com.ecommerce.project.dtos.productDtos.CreateProductDto;
import com.ecommerce.project.dtos.productDtos.ProductDto;
import com.ecommerce.project.dtos.productDtos.ResponseProductDto;
import com.ecommerce.project.dtos.productDtos.UpdateProductDto;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.productService.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/public/products")
    public ResponseEntity<ServiceResult<ResponseProductDto>> getProducts(
            @RequestParam(value="pageSize", required = false) Integer pageSize,
            @RequestParam(value="pageNumber", required = false) Integer pageNumber,
            @RequestParam(value="sortDirection", required = false, defaultValue = AppConstants.DEFAULT_ORDER_SORT_DIRECTION) String sortDirection,
            @RequestParam(value="sortBy", required = false, defaultValue = AppConstants.DEFAULT_PRODUCT_ORDER_BY) String sortBy
    ) {
        return new ResponseEntity<>(productService.getProducts(pageSize,pageNumber,sortDirection,sortBy), HttpStatus.OK);
    }
    @PostMapping("/admin/categories/{categoryId}/products")
    public ResponseEntity<ServiceResult<String>> addProduct(@PathVariable Long categoryId,@Valid @RequestBody CreateProductDto createProductDto) {
        return new ResponseEntity<>(productService.addProduct(createProductDto,categoryId), HttpStatus.CREATED);
    }

    @DeleteMapping("/public/products/{productId}")
    public ResponseEntity<ServiceResult<String>> deleteProduct(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    @PutMapping("/public/products/{productId}")
    public ResponseEntity<ServiceResult<ProductDto>> updateProduct(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateProductDto updateRequest) {
        return ResponseEntity.ok(productService.updateProduct(productId, updateRequest));
    }

    @GetMapping("/public/categories/{categoryId}/products")
    public ResponseEntity<ServiceResult<ResponseProductDto>> getCategoryProducts(
             @PathVariable(value = "categoryId") Long categoryId,
             @RequestParam(value = "pageSize", required = false) Integer pageSize,
             @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
             @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.DEFAULT_ORDER_SORT_DIRECTION) String sortDirection,
                @RequestParam(value = "sortBy", required = false, defaultValue = AppConstants.DEFAULT_PRODUCT_ORDER_BY) String sortBy
    ){
        return new ResponseEntity<>(productService.getProductsByCategory(categoryId,pageSize,pageNumber,sortDirection,sortBy), HttpStatus.OK);
    }

    @GetMapping("/public/products/filter/{keyword}")
    public ResponseEntity<ServiceResult<ResponseProductDto>> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.DEFAULT_ORDER_SORT_DIRECTION) String sortDirection,
            @RequestParam(value = "sortBy", required = false, defaultValue = AppConstants.DEFAULT_PRODUCT_ORDER_BY) String sortBy
    ) {
        return new ResponseEntity<>(productService.getProductsByKeyword(keyword, pageSize, pageNumber, sortDirection, sortBy), HttpStatus.OK);
    }


    @PatchMapping("/public/products/{productId}/image")
    public ResponseEntity<ServiceResult<ProductDto>> patchProductImage (
            @PathVariable Long productId,
            @RequestParam(value = "imageUrl", required = false) MultipartFile image
    ) throws IOException {

        return new ResponseEntity<>(productService.patchProductImage(productId,image), HttpStatus.OK);
    }
}

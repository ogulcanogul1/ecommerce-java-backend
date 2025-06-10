package com.ecommerce.project.services.productService;

import com.ecommerce.project.dtos.productDtos.CreateProductDto;
import com.ecommerce.project.dtos.productDtos.ProductDto;
import com.ecommerce.project.dtos.productDtos.ResponseProductDto;
import com.ecommerce.project.dtos.productDtos.UpdateProductDto;
import com.ecommerce.project.result.ServiceResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    ServiceResult<ResponseProductDto> getProducts(Integer pageSize, Integer pageNumber, String sortDirection, String sortBy);

    ServiceResult<String> addProduct(CreateProductDto createProductDto,Long categoryId);

    ServiceResult<String> deleteProduct(Long productId);

    ServiceResult<ProductDto> updateProduct(Long productId , UpdateProductDto productRequest);

    ServiceResult<ResponseProductDto> getProductsByCategory(Long categoryId,Integer pageSize, Integer pageNumber, String sortDirection, String sortBy);

    ServiceResult<ResponseProductDto> getProductsByKeyword(String keyword, Integer pageSize, Integer pageNumber, String sortDirection, String sortBy);

    ServiceResult<ProductDto> patchProductImage(Long productId, MultipartFile image) throws IOException;
}

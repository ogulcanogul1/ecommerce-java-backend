package com.ecommerce.project.dtos.productDtos;

import com.ecommerce.project.dtos.PaginationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class ResponseProductDto {
    private List<ProductDto> products;
    private Long totalElements;
    private PaginationInfo paginationInfo;
}

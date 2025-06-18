package com.ecommerce.project.dtos.categoryDtos;

import com.ecommerce.project.dtos.PaginationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCategoryDto {
    private List<CategoryDto> categories;
    private Long totalElements;
    private PaginationInfo paginationInfo;

}

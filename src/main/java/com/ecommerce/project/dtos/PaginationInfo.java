package com.ecommerce.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationInfo {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
}

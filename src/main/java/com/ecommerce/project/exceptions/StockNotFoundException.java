package com.ecommerce.project.exceptions;


import lombok.Getter;
import lombok.Setter;

public class StockNotFoundException extends RuntimeException {

    @Getter @Setter
    private Long productId;

    @Getter @Setter
    private String productName;

    @Getter @Setter
    private Integer totalQuantity;

    public StockNotFoundException(String message) {
        super(message);
    }

    public StockNotFoundException(Long productId, String productName, Integer totalQuantity) {
        super(String.format("%s stock not found. Id : %s total quantity: %s" , productName, productId, totalQuantity));
    }

}

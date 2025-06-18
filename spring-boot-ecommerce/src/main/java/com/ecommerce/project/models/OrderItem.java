package com.ecommerce.project.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "OrderItems")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;


    public static Double totalOrderItemDiscountPrice(Double discountPrice,Integer quantity){
        return discountPrice * quantity;
    }
    public static Double totalOrderItemNonDiscountPrice(Double price,Integer quantity){
        return price * quantity;
    }
}

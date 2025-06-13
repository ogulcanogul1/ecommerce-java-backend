package com.ecommerce.project.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE},fetch = FetchType.EAGER)
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer quantity;

    private Double cartItemTotalPrice;

    public Double getCartItemTotalPrice() {
        cartItemTotalPrice = product.getDiscountPrice() * this.quantity;
        return cartItemTotalPrice;
    }
}

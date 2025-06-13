package com.ecommerce.project.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private String description;
    private String imageUrl;
    private Integer quantity;
    private Double price;
    private Integer percentageOfDiscount = 0;
    private Double discountPrice;


    public Product(Long productId, String productName, String description, String imageUrl, Integer quantity, Double price, Integer percentageOfDiscount, Double discountPrice) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.price = price;
        this.percentageOfDiscount = percentageOfDiscount;
    }

    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST , CascadeType.MERGE })
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne(optional = true, cascade = { CascadeType.PERSIST , CascadeType.MERGE })
    @JoinColumn(name = "seller_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "product")
    List<CartItem> cartItems = new ArrayList<>();

    public Double getDiscountPrice() {
        if(this.percentageOfDiscount > 0){
            this.discountPrice = this.price * (1 - (percentageOfDiscount / 100.0));
        }
        else{
            this.discountPrice = this.price;
        }
        return discountPrice;
    }
}

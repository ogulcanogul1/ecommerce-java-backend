package com.ecommerce.project.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
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


    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST , CascadeType.MERGE })
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @ManyToOne(optional = true, cascade = { CascadeType.PERSIST , CascadeType.MERGE })
    @JoinColumn(name = "seller_id", nullable = true)
    private User user;

    @OneToMany(mappedBy = "product")
    List<CartItem> cartItems = new ArrayList<>();
}

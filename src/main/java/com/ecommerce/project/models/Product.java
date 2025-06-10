package com.ecommerce.project.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String description;
    private String imageUrl;
    private Integer quantity;
    private Double price;


    @ManyToOne(optional = false, cascade = { CascadeType.PERSIST , CascadeType.MERGE })
    @JoinColumn(name = "category_id")
    private Category category;
}

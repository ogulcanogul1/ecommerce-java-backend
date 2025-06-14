package com.ecommerce.project.models;

import com.ecommerce.project.models.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(nullable = false)
    private String email;

    private LocalDateTime orderDate;
    
    private Double totalAmount;

    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name="address_id",nullable = false)
    private Address address;

    @ManyToOne(optional = false)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

}

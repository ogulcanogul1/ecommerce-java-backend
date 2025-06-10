package com.ecommerce.project.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "addresses")
@NoArgsConstructor
@Data
public class Address {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long addressId;

    private String country;

    private String city;

    private String state;

    private String street;

    private String zip;

    @ToString.Exclude
    @ManyToOne(optional = true, cascade = { jakarta.persistence.CascadeType.PERSIST, jakarta.persistence.CascadeType.MERGE })
    @JoinColumn(name = "user_id", nullable = true)
    private User user;
}

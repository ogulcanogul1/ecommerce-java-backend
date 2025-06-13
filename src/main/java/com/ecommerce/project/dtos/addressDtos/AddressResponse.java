package com.ecommerce.project.dtos.addressDtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponse {
    private String country;

    private String city;

    private String state;

    private String street;

    private String zip;
}

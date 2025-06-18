package com.ecommerce.project.dtos.addressDtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UpdateAddressRequest {
    @NotBlank(message = "Country must not be blank!")
    private String country;

    @NotBlank(message = "City must not be blank!")
    private String city;

    @NotBlank(message = "State must not be blank!")
    private String state;

    @NotBlank(message = "Street must not be blank!")
    private String street;

    @NotBlank(message = "Zip must not be blank!")
    private String zip;
}

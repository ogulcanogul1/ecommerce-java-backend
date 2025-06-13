package com.ecommerce.project.controllers;


import com.ecommerce.project.dtos.addressDtos.AddressResponse;
import com.ecommerce.project.dtos.addressDtos.CreateAddressRequest;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.addressService.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }



    @PostMapping("/address")
    public ResponseEntity<ServiceResult<AddressResponse>> createAddress(@RequestBody CreateAddressRequest request){
        return new ResponseEntity<>(addressService.createAddress(request),HttpStatus.CREATED);
    }
}

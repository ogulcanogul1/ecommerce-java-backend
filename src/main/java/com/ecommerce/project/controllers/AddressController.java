package com.ecommerce.project.controllers;


import com.ecommerce.project.constants.AppConstants;
import com.ecommerce.project.dtos.addressDtos.AddressResponse;
import com.ecommerce.project.dtos.addressDtos.CreateAddressRequest;
import com.ecommerce.project.dtos.addressDtos.UpdateAddressRequest;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.services.addressService.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }



    @PostMapping("/address")
    public ResponseEntity<ServiceResult<AddressResponse>> createAddress(@Valid @RequestBody CreateAddressRequest request){
        return new ResponseEntity<>(addressService.createAddress(request),HttpStatus.CREATED);
    }

    @GetMapping("/address")
    public ResponseEntity<ServiceResult<List<AddressResponse>>> getAllAddresses(
            @RequestParam(name = "pageNumber",required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",required = false) Integer pageSize,
            @RequestParam(name="sortDirection",required = false, defaultValue = AppConstants.DEFAULT_ORDER_SORT_DIRECTION) String sortDirection,
            @RequestParam(name = "sortBy",required = false, defaultValue = AppConstants.DEFAULT_CART_ADDRESS_BY) String sortBy
    ){

        return new ResponseEntity<>(addressService.getAllAddresses(pageNumber, pageSize, sortDirection, sortBy),HttpStatus.OK);
    }

    @GetMapping("address/{addressId}")
    public ResponseEntity<ServiceResult<AddressResponse>> getAddressById(@PathVariable Long addressId){
        return new ResponseEntity<>(addressService.getAddressById(addressId),HttpStatus.OK);
    }

    @GetMapping("/address/user")
    public ResponseEntity<ServiceResult<List<AddressResponse>>> getAddressByUserId(){
         return new ResponseEntity<>(addressService.getAddressByUser(),HttpStatus.OK);
    }

    @PutMapping("/address/{addressId}")
    public ResponseEntity<ServiceResult<AddressResponse>> updateAddress(@Valid @RequestBody UpdateAddressRequest request,@PathVariable Long addressId){
        ServiceResult<AddressResponse> addressServiceResult = addressService.updateAddress(request , addressId);

        return new ResponseEntity<>(addressServiceResult,HttpStatus.OK);
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<ServiceResult<AddressResponse>> deleteAddress(@PathVariable Long addressId){
        return new ResponseEntity<>( addressService.deleteAddress(addressId),HttpStatus.OK);
    }
}

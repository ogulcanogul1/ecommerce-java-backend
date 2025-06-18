package com.ecommerce.project.services.addressService;

import com.ecommerce.project.dtos.addressDtos.AddressResponse;
import com.ecommerce.project.dtos.addressDtos.CreateAddressRequest;
import com.ecommerce.project.dtos.addressDtos.UpdateAddressRequest;
import com.ecommerce.project.result.ServiceResult;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    ServiceResult<AddressResponse> createAddress(CreateAddressRequest request);

    ServiceResult<List<AddressResponse>> getAllAddresses(Integer pageNumber, Integer pageSize, String sortDirection, String sortBy);

    ServiceResult<AddressResponse> getAddressById(Long addressId);

    ServiceResult<List<AddressResponse>> getAddressByUser();

    ServiceResult<AddressResponse> updateAddress(@Valid UpdateAddressRequest request, Long addressId);

    ServiceResult<AddressResponse> deleteAddress(Long addressId);
}

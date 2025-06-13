package com.ecommerce.project.services.addressService;

import com.ecommerce.project.dtos.addressDtos.AddressResponse;
import com.ecommerce.project.dtos.addressDtos.CreateAddressRequest;
import com.ecommerce.project.result.ServiceResult;

public interface AddressService {
    ServiceResult<AddressResponse> createAddress(CreateAddressRequest request);
}

package com.ecommerce.project.services.addressService;

import com.ecommerce.project.dtos.addressDtos.AddressResponse;
import com.ecommerce.project.dtos.addressDtos.CreateAddressRequest;
import com.ecommerce.project.models.Address;
import com.ecommerce.project.models.User;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    @Autowired
    public AddressServiceImpl(AddressRepository addressRepository, ModelMapper modelMapper, AuthUtil authUtil, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
        this.authUtil = authUtil;
        this.userRepository = userRepository;
    }
    @Override
    public ServiceResult<AddressResponse> createAddress(CreateAddressRequest request) {

        User user = userRepository.findByEmail(authUtil.loggedInEmail())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", authUtil.loggedInEmail())));


        Address address = modelMapper.map(request,Address.class);


        user.getAddresses().add(address);

        Address savedAddress = addressRepository.save(address);

        AddressResponse response = modelMapper.map(savedAddress,AddressResponse.class);

        return ServiceResult.success(response,"address created!");
    }
}

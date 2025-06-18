package com.ecommerce.project.services.addressService;

import com.ecommerce.project.dtos.addressDtos.AddressResponse;
import com.ecommerce.project.dtos.addressDtos.CreateAddressRequest;
import com.ecommerce.project.dtos.addressDtos.UpdateAddressRequest;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.models.Address;
import com.ecommerce.project.models.User;
import com.ecommerce.project.repositories.AddressRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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

        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        AddressResponse response = modelMapper.map(savedAddress,AddressResponse.class);

        return ServiceResult.success(response,"address created!");
    }

    @Override
    public ServiceResult<List<AddressResponse>> getAllAddresses(Integer pageNumber, Integer pageSize, String sortDirection, String sortBy) {
        Sort sort = sortDirection.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        if(pageNumber == null || pageNumber <= 0 ||pageSize == null || pageSize <= 0) {

            List<Address> addresses = addressRepository.findAll(sort);

            List<AddressResponse> addResponseList = addresses.stream().map(address -> modelMapper.map(address,AddressResponse.class)).toList();

            return ServiceResult.success(addResponseList,"All Addresses");
        }

        Pageable pageable = PageRequest.of(pageNumber-1,pageSize,sort);
        Page<Address> pages = addressRepository.findAll(pageable);

        if(pages.isEmpty()){
            throw  new APIException("Address not Created yet");
        }

          List<Address> addresses = pages.getContent();

        List<AddressResponse> addressResponsesList = addresses.stream().map(address -> modelMapper.map(address,AddressResponse.class)).toList();


        return ServiceResult.success(addressResponsesList,"All Addresses");
    }

    @Override
    public ServiceResult<AddressResponse> getAddressById(Long addressId) {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("address","addressId",addressId));

        AddressResponse response = modelMapper.map(address,AddressResponse.class);

        return ServiceResult.success(response,String.format("Address with id: %d found!", addressId));
    }

    @Override
    public ServiceResult<List<AddressResponse>> getAddressByUser() {

        List<Address> addresses = addressRepository.findByUserEmail(authUtil.loggedInEmail())
                .orElseThrow(() -> new APIException("Address not Created yet!"));

        List<AddressResponse> responseList = addresses.stream().map(address -> modelMapper.map(address,AddressResponse.class)).toList();

        return ServiceResult.success(responseList,"All Addresses By User");
    }

    @Override
    public ServiceResult<AddressResponse> updateAddress(UpdateAddressRequest request, Long addressId) {

        List<Address> addresses = addressRepository.findByUserEmail(authUtil.loggedInEmail())
                .orElseThrow(() -> new APIException("Address not Created yet!"));


        AddressResponse response = null;
        for (Address address : addresses) {
            if(address.getAddressId().equals(addressId)){

                 address.setCountry(request.getCountry());
               address.setCity(request.getCity());
               address.setStreet(request.getStreet());
               address.setState(request.getState());
               address.setZip(request.getZip());

               addressRepository.save(address);

               response = modelMapper.map(address,AddressResponse.class);
               break;
            }
        }

        if(response == null){
            throw new APIException("Address not Created yet!");
        }
        return ServiceResult.success(response,"Address Updated!");
    }

    @Override
    public ServiceResult<AddressResponse> deleteAddress(Long addressId) {

        List<Address> addresses = addressRepository.findByUserEmail(authUtil.loggedInEmail())
                .orElseThrow(() -> new APIException("Address not Created yet!"));

        Address deletedAddress = null;
        for(Address address : addresses){
            if(address.getAddressId().equals(addressId)){
                addressRepository.delete(address);
                deletedAddress = address;
                break;
            }
        }

        if(deletedAddress == null){
            throw new APIException("Address not Created yet!");
        }

        AddressResponse response = modelMapper.map(deletedAddress,AddressResponse.class);

        return ServiceResult.success(response,"Address Deleted Successfuly");
    }
}

package com.ecommerce.project.controllers;

import com.ecommerce.project.models.User;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.security.JwtUtils;
import com.ecommerce.project.security.SecurityDto.LoginRequest;
import com.ecommerce.project.security.SecurityDto.SignUpRequest;
import com.ecommerce.project.security.SecurityDto.UserResponse;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import com.ecommerce.project.services.authService.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;
    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signIn")
    public ResponseEntity<ServiceResult<?>> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        ServiceResult<?> serviceResult = authService.login(loginRequest);
        UserInfoResponse response = (UserInfoResponse) serviceResult.getData();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,response.getJwtToken())
                .body(serviceResult);
    }

    @PostMapping("/signUp")
    public ResponseEntity<ServiceResult<User>> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        ServiceResult<User> serviceResult = authService.signUp(signUpRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(serviceResult.getData().getUserId())
                .toUri();

        return ResponseEntity.created(location).body(serviceResult);
    }

    @GetMapping("/username")
    public ResponseEntity<ServiceResult<UserResponse>> currentUser(Authentication authentication){
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ServiceResult<UserResponse> response =  ServiceResult.success(new UserResponse(userDetails.getUsername(),userDetails.getEmail(),userDetails.getAuthorities()),"Current User");

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/signout")

    public ResponseEntity<ServiceResult<User>> signOut(Authentication authentication){
         ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
         return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                 cookie.toString())
                 .body(ServiceResult.success("You've been signed out"));
    }
}

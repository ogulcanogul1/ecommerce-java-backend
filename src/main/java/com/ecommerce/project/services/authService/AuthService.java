package com.ecommerce.project.services.authService;

import com.ecommerce.project.models.User;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.security.SecurityDto.LoginRequest;
import com.ecommerce.project.security.SecurityDto.SignUpRequest;


public interface AuthService {

    ServiceResult<?> login(LoginRequest loginRequest);

    ServiceResult<User> signUp(SignUpRequest signUpRequest);

}

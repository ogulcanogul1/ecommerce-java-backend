package com.ecommerce.project.services.authService;

import com.ecommerce.project.exceptions.AlreadyTakenException;
import com.ecommerce.project.models.Role;
import com.ecommerce.project.models.User;
import com.ecommerce.project.repositories.RoleRepository;
import com.ecommerce.project.repositories.UserRepository;
import com.ecommerce.project.result.ServiceResult;
import com.ecommerce.project.security.JwtUtils;
import com.ecommerce.project.security.SecurityDto.LoginRequest;
import com.ecommerce.project.security.SecurityDto.SignUpRequest;
import com.ecommerce.project.security.response.UserInfoResponse;
import com.ecommerce.project.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class AuthServiceImpl implements AuthService{

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder){

        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public ServiceResult<?> login(LoginRequest loginRequest) {
        Authentication authentication;
        try{
            authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        }
        catch (Exception e){
            String message = "Bad Credentials";
            return ServiceResult.error(message);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        UserInfoResponse response = new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(), roles, jwtCookie.toString());

        return ServiceResult.success(response, "User logged in successfully");
    }

    @Override
    public ServiceResult<User> signUp(SignUpRequest signUpRequest) {

        Optional<User> usernameControl = userRepository.findByUsername(signUpRequest.getUsername());

        if(usernameControl.isPresent()) {
            throw new AlreadyTakenException("Username");
        }


        Optional<User> emailControl = userRepository.findByEmail(signUpRequest.getEmail());

        if(emailControl.isPresent()) {
            throw new AlreadyTakenException("Email");
        }


        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        List<Role> roles = roleRepository.findAll();

        for(Role role : roles){
            if(signUpRequest.getRoles().contains(role.getRoleName().name())){
                user.getRoles().add(role);
            }
        }

        user = userRepository.save(user);

        return ServiceResult.success(user, "User registered successfully");
    }
}

package com.ecommerce.project.security.SecurityDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank
    @Size(min = 4, max = 25)
    private String username;

    @NotBlank
    @Size(min = 4, max = 25)
    private String password;

    @NotBlank
    @Email
    private String email;

    private Set<String> roles;
}

package com.ecommerce.project.security.SecurityDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public class LoginRequest {
    @Getter @Setter
    @NotBlank
    @Size(min = 4, max = 25)
    private String username;

    @Getter @Setter
    @NotBlank
    @Size(min = 4, max = 25)
    private String password;

}

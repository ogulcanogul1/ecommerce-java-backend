package com.ecommerce.project.security.services;

import com.ecommerce.project.models.Role;
import com.ecommerce.project.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    public static final long serialVersionUID = 1L;

    private String username;
    private String email;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user){
        Collection<GrantedAuthority> temp_authorities = new ArrayList<>();
        for(Role role : user.getRoles()) {
            temp_authorities.add(new SimpleGrantedAuthority(role.getRoleName().toString()));
        }
        return new UserDetailsImpl(user.getUsername(), user.getEmail(), user.getPassword(), temp_authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

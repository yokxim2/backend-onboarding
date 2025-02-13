package com.example.backendonboarding.dto;

import com.example.backendonboarding.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
public class SignupResponseDto {
    private String username;
    private String nickname;
    private Collection<? extends GrantedAuthority> authorities;

    public SignupResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        UserDetails userDetails = new CustomUserDetails(user);
        this.authorities = userDetails.getAuthorities();
    }
}

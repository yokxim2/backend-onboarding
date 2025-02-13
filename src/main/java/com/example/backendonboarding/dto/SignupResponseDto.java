package com.example.backendonboarding.dto;

import com.example.backendonboarding.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupResponseDto {
    private String username;
    private String nickname;
    private String role;

    public SignupResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}

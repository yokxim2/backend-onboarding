package com.example.backendonboarding.dto;

import com.example.backendonboarding.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@Schema(description = "회원가입 응답 DTO")
public class SignupResponseDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String username;

    @Schema(description = "닉네임", example = "홍길동")
    private String nickname;

    @Schema(description = "사용자 권한 목록")
    private Collection<? extends GrantedAuthority> authorities;

    public SignupResponseDto(User user) {
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        UserDetails userDetails = new CustomUserDetails(user);
        this.authorities = userDetails.getAuthorities();
    }
}

package com.example.backendonboarding.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "로그인 요청 DTO")
public class LoginRequestDto {

    @Schema(description = "사용자 ID", example = "user123")
    private String username;

    @Schema(description = "비밀번호", example = "password123")
    private String password;
}

package com.example.backendonboarding.controller;

import com.example.backendonboarding.dto.LoginRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth API", description = "로그인 및 인증 관련 API")
@RestController
@RequestMapping("/api/user")
public class LoginController {

    @PostMapping("/sign")
    @Operation(summary = "로그인", description = "사용자가 아이디와 비밀번호로 로그인을 시도합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공",
                content = @Content(mediaType = "application/json",
                        examples = @ExampleObject(value = "{ \"token\": \"eyJhbGciOiJIUzI1NiIsInR...\" }")))
    @ApiResponse(responseCode = "401", description = "로그인 실패 (아이디 또는 비밀번호 불일치)")
    public ResponseEntity<String> login(@RequestBody LoginRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This is handled by LoginFilter.");
    }
}

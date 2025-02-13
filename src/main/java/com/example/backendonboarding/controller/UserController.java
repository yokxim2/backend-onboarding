package com.example.backendonboarding.controller;

import com.example.backendonboarding.dto.SignupRequestDto;
import com.example.backendonboarding.dto.SignupResponseDto;
import com.example.backendonboarding.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(SignupRequestDto requestDto) {
        return new ResponseEntity<>(userService.signup(requestDto), HttpStatus.OK);
    }
}

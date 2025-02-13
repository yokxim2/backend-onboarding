package com.example.backendonboarding.controller;

import com.example.backendonboarding.dto.SignupRequestDto;
import com.example.backendonboarding.dto.SignupResponseDto;
import com.example.backendonboarding.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.backendonboarding.jwt.LoginFilter.COOKIE_MAX_AGE;

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

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        String[] tokens = userService.reissue(cookies);

        response.setHeader("access", tokens[0]);
        response.addCookie(createCookie("refresh", tokens[1]));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setHttpOnly(true);

        return cookie;
    }
}

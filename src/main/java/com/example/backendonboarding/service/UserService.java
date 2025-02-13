package com.example.backendonboarding.service;

import com.example.backendonboarding.dto.SignupRequestDto;
import com.example.backendonboarding.dto.SignupResponseDto;
import com.example.backendonboarding.entity.Refresh;
import com.example.backendonboarding.entity.User;
import com.example.backendonboarding.jwt.JwtUtil;
import com.example.backendonboarding.repository.RefreshRepository;
import com.example.backendonboarding.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.example.backendonboarding.jwt.LoginFilter.ACCESS_TOKEN_EXPIRATION_TIME;
import static com.example.backendonboarding.jwt.LoginFilter.REFRESH_TOKEN_EXPIRATION_TIME;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil, RefreshRepository refreshRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    public SignupResponseDto signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();

        Boolean isUserExist = userRepository.existsByUsername(username);

        if (isUserExist) {
            throw new IllegalArgumentException("Username is already in use");
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setNickname(nickname);
        newUser.setRole("ROLE_USER");

        User savedUser = userRepository.save(newUser);
        return new SignupResponseDto(savedUser);
    }

    public String[] reissue(Cookie[] cookies) {

        String refresh = null;
        String[] tokens = new String[2];

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            throw new IllegalArgumentException("Refresh cookie not found");
        }

        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {
            throw new IllegalArgumentException("Invalid refresh cookie");
        }

        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            throw new IllegalArgumentException("Refresh cookie not found");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String newAccess = jwtUtil.createJwt("access", username, role, ACCESS_TOKEN_EXPIRATION_TIME);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, REFRESH_TOKEN_EXPIRATION_TIME);

        refreshRepository.deleteByRefresh(refresh);
        saveNewRefresh(username, newRefresh, REFRESH_TOKEN_EXPIRATION_TIME);

        tokens[0] = newAccess;
        tokens[1] = newRefresh;

        return tokens;
    }

    private void saveNewRefresh(String username, String newRefresh, long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refresh = new Refresh();
        refresh.setUsername(username);
        refresh.setRefresh(newRefresh);
        refresh.setExpiration(date.toString());

        refreshRepository.save(refresh);
    }
}

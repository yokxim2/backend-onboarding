package com.example.backendonboarding.service;

import com.example.backendonboarding.dto.SignupRequestDto;
import com.example.backendonboarding.dto.SignupResponseDto;
import com.example.backendonboarding.entity.User;
import com.example.backendonboarding.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SignupResponseDto signup(SignupRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();
        String nickname = requestDto.getNickname();

        Boolean isUserExist = userRepository.findByUsername(username);

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
}

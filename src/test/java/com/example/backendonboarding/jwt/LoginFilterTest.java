package com.example.backendonboarding.jwt;

import com.example.backendonboarding.dto.LoginRequestDto;
import com.example.backendonboarding.entity.Refresh;
import com.example.backendonboarding.entity.User;
import com.example.backendonboarding.repository.RefreshRepository;
import com.example.backendonboarding.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LoginFilterTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RefreshRepository refreshRepository;

    @Autowired
    private UserRepository userRepository;

    private RestTemplate restTemplate;
    private String baseUrl;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
        baseUrl = "http://localhost:8080/api/user/sign";

        refreshRepository.deleteAll();

        if (!userRepository.existsByUsername("testUser")) {
            User user = new User();
            user.setUsername("testUser");
            user.setPassword(new BCryptPasswordEncoder().encode("testPassword"));
            userRepository.save(user);
        }
    }

    @AfterEach
    void tearDown() {
        refreshRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공 시 Access와 Refresh 토큰 발급 확인")
    void test1() {
        // given : 로그인 요청 데이터
        LoginRequestDto requestDto = new LoginRequestDto();
        requestDto.setUsername("testUser");
        requestDto.setPassword("testPassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LoginRequestDto> request = new HttpEntity<>(requestDto, headers);

        // when : 로그인 요청
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, request, String.class);

        // then : 응답 상태 코드 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // then : Access 토큰 존재 여부 : 헤더 확인
        assertThat(response.getHeaders().get("access")).isNotNull();

        // then : Refresh 토큰 존재 여부 : 쿠키 확인
        List<String> cookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        assertThat(cookies).isNotNull().isNotEmpty();

        boolean hasRefreshCookie = cookies.stream().anyMatch(cookie -> cookie.startsWith("refresh="));
        assertThat(hasRefreshCookie).isTrue();

        // then : Refresh 토큰이 DB에 저장되었는지 확인
        List<Refresh> savedRefreshTokens = refreshRepository.findAll();
        assertThat(savedRefreshTokens).hasSize(1);

        Refresh savedRefresh = savedRefreshTokens.get(0);
        assertThat(savedRefresh.getUsername()).isEqualTo("testUser");
        assertThat(savedRefresh.getRefresh()).isNotNull();
    }
}
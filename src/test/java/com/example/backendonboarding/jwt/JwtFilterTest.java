package com.example.backendonboarding.jwt;

import com.example.backendonboarding.entity.User;
import com.example.backendonboarding.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JwtFilterTest {

    private final String baseUrl = "http://localhost:8080/secured-endpoint";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private RestTemplate restTemplate;
    private String validAccessToken;
    private String expiredAccessToken;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();

        // 테스트 유저가 없으면 추가
        if (!userRepository.existsByUsername("testUser")) {
            User user = new User();
            user.setUsername("testUser");
            user.setPassword(new BCryptPasswordEncoder().encode("testPassword"));
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }

        // 정상적인 Access Token 생성 (유효 기간 10분)
        validAccessToken = jwtUtil.createJwt("access", "testUser", "ROLE_USER", 600000L);

        // 만료된 Access Token 생성 (과거 시간)
        expiredAccessToken = jwtUtil.createJwt("access", "testUser", "ROLE_USER", -1000L);
    }

    @Test
    @DisplayName("✅ Access 토큰이 유효하면 secured-endpoint에 정상 접근 가능")
    void testValidAccessToken() {
        // given : 유효한 Access 토큰
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", validAccessToken); // 토큰 추가

        HttpEntity<Void> request = new HttpEntity<>(headers);

        // when : secured-endpoint 요청
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);

        // then : 200 OK 응답과 "Hello world" 메시지 확인
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("Hello world");
    }

    @Test
    @DisplayName("❌ 만료된 Access 토큰이 포함된 요청은 401 Unauthorized 반환")
    void testExpiredAccessToken() {
        // given : 만료된 Access 토큰
        HttpHeaders headers = new HttpHeaders();
        headers.set("access", expiredAccessToken);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        try {
            // when : secured-endpoint 요청
            ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, request, String.class);

            // then : 401 Unauthorized 확인
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(response.getBody()).isEqualTo("access token expired"); // 메시지도 검증
        } catch (HttpClientErrorException e) {
            // 예외가 발생하면 직접 응답 코드와 메시지를 검증
            assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(e.getResponseBodyAsString()).isEqualTo("access token expired");
        }
    }

}

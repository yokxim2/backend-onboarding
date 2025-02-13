package com.example.backendonboarding.jwt;

import com.example.backendonboarding.entity.User;
import com.example.backendonboarding.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPassword";
    private static final String TEST_ROLE = "ROLE_USER";
    private String validAccessToken;
    private String expiredAccessToken;
    private String invalidCategoryToken;

    @BeforeEach
    void setUp() {
//        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(bCryptPasswordEncoder.encode(TEST_PASSWORD));
        testUser.setRole(TEST_ROLE);
        userRepository.save(testUser);

        validAccessToken = jwtUtil.createJwt("access", TEST_USERNAME, TEST_ROLE, 60000L);
        expiredAccessToken = jwtUtil.createJwt("access", TEST_USERNAME, TEST_ROLE, -60000L);
        invalidCategoryToken = jwtUtil.createJwt("refresh", TEST_USERNAME, TEST_ROLE, 60000L);
    }

    @Test
    @DisplayName("토큰 없이 접근 시 403 반환")
    void test1() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secured-endpoint"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("정상 접근")
    void test2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secured-endpoint")
                .header("access", validAccessToken))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("만료된 토큰을 사용해서 접근한 경우 401 반환")
    void test3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secured-endpoint")
                .header("access", expiredAccessToken))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).contains("access token expired"));
    }

    @Test
    @DisplayName("access 카데고리 토큰이 없는 경우 401 반환")
    void test4() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secured-endpoint")
                .header("access", invalidCategoryToken))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertThat(result.getResponse().getContentAsString()).contains("Invalid access token"));
    }
}
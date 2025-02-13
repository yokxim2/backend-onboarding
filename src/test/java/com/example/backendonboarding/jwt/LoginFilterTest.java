package com.example.backendonboarding.jwt;

import com.example.backendonboarding.dto.LoginRequestDto;
import com.example.backendonboarding.entity.User;
import com.example.backendonboarding.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class LoginFilterTest {

   @Autowired
    private MockMvc mockMvc;

   @Autowired
    private UserRepository userRepository;

   @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Autowired
    private JwtUtil jwtUtil;

   private final ObjectMapper objectMapper = new ObjectMapper();
   private static final String TEST_USERNAME = "testUser";
   private static final String TEST_PASSWORD = "testPassword";

   @BeforeEach
    void setUp() {
       userRepository.deleteAll();

       User testUser = new User();
       testUser.setUsername(TEST_USERNAME);
       testUser.setPassword(bCryptPasswordEncoder.encode(TEST_PASSWORD));
       testUser.setRole("ROLE_USER");
       userRepository.save(testUser);
   }

   @Test
    void testLoginReturnsAccessTokenAndRefreshToken() throws Exception {
       LoginRequestDto loginRequest = new LoginRequestDto();
       loginRequest.setUsername(TEST_USERNAME);
       loginRequest.setPassword(TEST_PASSWORD);

       mockMvc.perform(MockMvcRequestBuilders.post("/sign")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(loginRequest)))
               .andExpect(status().isOk())
               .andExpect(header().exists("access"))
               .andExpect(cookie().exists("refresh"))
               .andExpect(result -> {
                   String accessToken = result.getResponse().getHeader("access");
                   Cookie refreshToken = result.getResponse().getCookie("refresh");

                   assertThat(jwtUtil.getUsername(accessToken)).isEqualTo(TEST_USERNAME);
                   assertThat(jwtUtil.getRole(accessToken)).isEqualTo("ROLE_USER");
                   assertThat(refreshToken).isNotNull();
                   assertThat(jwtUtil.getUsername(refreshToken.getValue())).isEqualTo(TEST_USERNAME);
               });

   }
}
package com.killeen.taskflow.components.user;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.killeen.taskflow.components.user.controller.AuthController;
import com.killeen.taskflow.components.user.exception.InvalidCredentialsException;
import com.killeen.taskflow.components.user.exception.UserAlreadyExistsException;
import com.killeen.taskflow.components.user.model.LoginRequest;
import com.killeen.taskflow.components.user.model.LoginResponse;
import com.killeen.taskflow.components.user.model.RegisterRequest;
import com.killeen.taskflow.components.user.model.User;
import com.killeen.taskflow.components.user.service.UserService;
import com.killeen.taskflow.config.JwtService;
import com.killeen.taskflow.config.SecurityConfig;

import io.jsonwebtoken.lang.Collections;
import tools.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean 
    UserService userService;
    @MockitoBean 
    JwtService jwtService;

    // -------------------------------------------------------------------------
    // POST /auth/register
    // -------------------------------------------------------------------------

    @Test
    void register_validRequest_returns201() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                        .email("user@example.com")
                        .password("Pass1234!")
                        .displayName("Alice")
                        .build();
        when(userService.register(anyString(), anyString(), anyString()))
            .thenReturn(User.builder().id(1L).email("user@example.com").displayName("Alice").build());

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@example.com"));
                    
    }

    @Test
    void register_blankEmail_returns400() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                        .email("")
                        .password("Pass1234!")
                        .displayName("Alice")
                        .build();

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isBadRequest());
    }

    @Test
    void register_duplicateEmail_returns409() throws Exception {
        RegisterRequest req = RegisterRequest.builder()
                        .email("dupe@example.com")
                        .password("Pass1234!")
                        .displayName("Alice")
                        .build();
        when(userService.register(anyString(), anyString(), anyString()))
            .thenThrow(new UserAlreadyExistsException("Email already in use"));

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isConflict());
    }

    // -------------------------------------------------------------------------
    // POST /auth/login
    // -------------------------------------------------------------------------

    @Test
    void login_validCredentials_returns200WithToken() throws Exception {
        LoginRequest req = LoginRequest.builder()
                    .email("user@example.com")
                    .password("Pass1234!")
                    .build();
        when(userService.authenticate(anyString(), anyString()))
            .thenReturn(LoginResponse.builder().token("jwt").expiresIn(3_600_000L).build());

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value("jwt"));
    }

    @Test
    void login_invalidCredentials_returns401() throws Exception {
        LoginRequest req = LoginRequest.builder()
                    .email("invalid@example.com")
                    .password("Pass1234!")
                    .build();
        when(userService.authenticate(anyString(), anyString()))
            .thenThrow(new InvalidCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    // -------------------------------------------------------------------------
    // GET /auth/me
    // -------------------------------------------------------------------------

    @Test
    void getCurrentUser_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/auth/me"))
            .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser(username = "1")
    void getCurrentUser_authenticated_returns200() throws Exception {
        when(userService.getUserById(anyLong()))
            .thenReturn(User.builder().id(1L).email("user@example.com").displayName("Alice").build());

        mockMvc.perform(get("/auth/me")
            .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, Collections.emptyList()))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("user@example.com"));
    }
}

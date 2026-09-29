package com.killeen.taskflow.components.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
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

import com.killeen.taskflow.components.refreshtoken.model.RefreshToken;
import com.killeen.taskflow.components.refreshtoken.service.RefreshTokenService;
import com.killeen.taskflow.components.user.controller.AuthController;
import com.killeen.taskflow.components.user.exception.InvalidCredentialsException;
import com.killeen.taskflow.components.refreshtoken.exception.InvalidRefreshTokenException;
import com.killeen.taskflow.components.refreshtoken.exception.RefreshTokenNotFoundException;
import com.killeen.taskflow.components.user.exception.UserAlreadyExistsException;
import com.killeen.taskflow.components.user.model.LoginRequest;
import com.killeen.taskflow.components.user.model.LoginResponse;
import com.killeen.taskflow.components.user.model.RefreshRequest;
import com.killeen.taskflow.components.user.model.RefreshResponse;
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
    @MockitoBean
    RefreshTokenService refreshTokenService;

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

    // -------------------------------------------------------------------------
    // POST /auth/refresh
    // -------------------------------------------------------------------------

    @Test
    @WithMockUser(username = "1")
    void refresh_validNonExpiredToken_returns200WithNewTokens() throws Exception {
    RefreshRequest req = RefreshRequest.builder()
        .refreshToken("sel.val")
        .build();

    RefreshToken stored = RefreshToken.builder()
        .userId(1L)
        .selector("sel")
        .token("hashed")
        .build();

    RefreshResponse response = RefreshResponse.builder()
        .token("new-jwt")
        .expiresIn(3_600_000L)
        .refreshToken("new.selector.validator")
        .build();

    when(refreshTokenService.findByToken(anyString())).thenReturn(stored);
    when(refreshTokenService.isTokenExpired(any())).thenReturn(false);
    when(userService.getUserById(anyLong())).thenReturn(User.builder().id(1L).email("user@example.com").displayName("Alice").build());
    when(jwtService.generateToken(any())).thenReturn("new-jwt");
    when(jwtService.getExpirationMs()).thenReturn(3_600_000L);
    when(refreshTokenService.createRefreshToken(anyLong())).thenReturn("new.selector.validator");
    when(userService.refresh(req)).thenReturn(response);

    mockMvc.perform(post("/auth/refresh")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("new-jwt"))
        .andExpect(jsonPath("$.refreshToken").value("new.selector.validator"));
    }

    @Test
    void refresh_invalidToken_returns401() throws Exception {
        RefreshRequest req = RefreshRequest.builder()
                .refreshToken("bad.token")
                .build();
        when(refreshTokenService.findByToken(anyString()))
                .thenThrow(new InvalidRefreshTokenException("Invalid token"));
        when(userService.refresh(req))
                .thenThrow(new InvalidRefreshTokenException("Invalid token"));

        mockMvc.perform(post("/auth/refresh")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or expired refresh token"));
    }

    @Test
    void refresh_tokenNotFound_returns401() throws Exception {
        RefreshRequest req = RefreshRequest.builder()
                .refreshToken("missing.token")
                .build();
        when(refreshTokenService.findByToken(anyString()))
                .thenThrow(new RefreshTokenNotFoundException("Not found"));
        when(userService.refresh(req))
                .thenThrow(new RefreshTokenNotFoundException("Not found"));

        mockMvc.perform(post("/auth/refresh")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid or expired refresh token"));
    }

    @Test
    void logout_validRefreshToken_returns200() throws Exception {
        RefreshRequest req = RefreshRequest.builder()
                .refreshToken("sel.val")
                .build();

        mockMvc.perform(post("/auth/logout")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void refresh_validTokenWithoutAccessToken_returns200() throws Exception {
        RefreshToken stored = RefreshToken.builder().userId(1L).build();
        when(refreshTokenService.findByToken("sel.val")).thenReturn(stored);
        when(refreshTokenService.isTokenExpired(stored)).thenReturn(false);
        when(userService.getUserById(1L))
            .thenReturn(User.builder().id(1L).email("user@example.com").displayName("Alice").build());
        when(jwtService.generateToken(any())).thenReturn("new-access");
        when(jwtService.getExpirationMs()).thenReturn(60_000L);
        when(refreshTokenService.createRefreshToken(1L)).thenReturn("new.refresh");

        mockMvc.perform(post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"refreshToken\":\"sel.val\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("new-access"))
            .andExpect(jsonPath("$.refreshToken").value("new.refresh"));
    }

    @Test
    void refresh_expiredToken_returns401AndDeletesToken() throws Exception {
    RefreshRequest req = RefreshRequest.builder()
        .refreshToken("sel.val")
        .build();

    RefreshToken stored = RefreshToken.builder()
        .userId(1L)
        .selector("sel")
        .token("hashed")
        .build();

    when(refreshTokenService.findByToken(anyString())).thenReturn(stored);
    when(refreshTokenService.isTokenExpired(any())).thenReturn(true);

    mockMvc.perform(post("/auth/refresh")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isUnauthorized());

    verify(refreshTokenService).deleteByToken("sel.val");
    }

    @Test
    @WithMockUser(username = "1")
    void refresh_blankRefreshToken_returns400() throws Exception {
    RefreshRequest req = RefreshRequest.builder()
        .refreshToken("")
        .build();

    mockMvc.perform(post("/auth/refresh")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // POST /auth/logout
    // -------------------------------------------------------------------------

    @Test
    @WithMockUser(username = "1")
    void logout_validToken_returns200() throws Exception {
    RefreshRequest req = RefreshRequest.builder()
        .refreshToken("sel.val")
        .build();

    mockMvc.perform(post("/auth/logout")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk());

    verify(refreshTokenService).deleteByToken("sel.val");
    }

    @Test
    @WithMockUser(username = "1")
    void logout_blankToken_returns400() throws Exception {
    RefreshRequest req = RefreshRequest.builder()
        .refreshToken("")
        .build();

    mockMvc.perform(post("/auth/logout")
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
    }

    // -------------------------------------------------------------------------
    // DELETE /auth/sessions
    // -------------------------------------------------------------------------

    @Test
    void revokeAllSessions_authenticated_returns200() throws Exception {
    mockMvc.perform(delete("/auth/sessions")
        .with(SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(1L, null, Collections.emptyList()))))
        .andExpect(status().isOk());

    verify(refreshTokenService).deleteAllForUser(anyLong());
    }

    @Test
    void revokeAllSessions_unauthenticated_returns401() throws Exception {
    mockMvc.perform(delete("/auth/sessions"))
        .andExpect(status().isUnauthorized());
    }
}

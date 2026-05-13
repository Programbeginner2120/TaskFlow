package com.killeen.taskflow.components.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.killeen.taskflow.components.user.model.ForgotPasswordRequest;
import com.killeen.taskflow.components.user.model.LoginRequest;
import com.killeen.taskflow.components.user.model.LoginResponse;
import com.killeen.taskflow.components.user.model.RegisterRequest;
import com.killeen.taskflow.components.user.model.ResendVerificationRequest;
import com.killeen.taskflow.components.user.model.ResetPasswordRequest;
import com.killeen.taskflow.components.user.model.User;
import com.killeen.taskflow.components.user.model.UserResponse;
import com.killeen.taskflow.components.user.service.UserService;
import com.killeen.taskflow.util.AuthUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import com.killeen.taskflow.components.user.model.RefreshRequest;
import com.killeen.taskflow.components.user.model.RefreshResponse;
import com.killeen.taskflow.components.refreshtoken.service.RefreshTokenService;
import com.killeen.taskflow.components.refreshtoken.model.RefreshToken;
import com.killeen.taskflow.config.JwtService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(
                request.getEmail(),
                request.getPassword(),
                request.getDisplayName()
        );
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = userService.authenticate(
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshToken stored = refreshTokenService.findByToken(request.getRefreshToken());
        if (refreshTokenService.isTokenExpired(stored)) {
            // remove expired token and reject
            refreshTokenService.deleteByToken(request.getRefreshToken());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // issue new JWT and rotate refresh token
        User user = userService.getUserById(stored.getUserId());
        String jwt = jwtService.generateToken(user);
        long expiresIn = jwtService.getExpirationMs();

        String newRefresh = refreshTokenService.createRefreshToken(user.getId());
        refreshTokenService.deleteByToken(request.getRefreshToken());

        RefreshResponse resp = RefreshResponse.builder()
            .token(jwt)
            .expiresIn(expiresIn)
            .refreshToken(newRefresh)
            .build();

        return ResponseEntity.ok(resp);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshRequest request) {
        String raw = request.getRefreshToken();
        if (raw == null || raw.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        refreshTokenService.deleteByToken(raw);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sessions")
    public ResponseEntity<?> revokeAllSessions() {
        Long userId = AuthUtils.getAuthenticatedUserId();
        refreshTokenService.deleteAllForUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        Long userId = AuthUtils.getAuthenticatedUserId();
        User user = userService.getUserById(userId);

        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<?> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        userService.resendVerification(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }
}

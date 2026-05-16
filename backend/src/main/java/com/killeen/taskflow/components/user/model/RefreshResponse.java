package com.killeen.taskflow.components.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshResponse {
    private String token;
    private long expiresIn;
    private String refreshToken;
}

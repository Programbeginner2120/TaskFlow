package com.killeen.taskflow.components.email.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailToken {
    private Long id;
    private Long userId;
    private String token;          // SHA-256 hex stored in DB
    private EmailTokenType tokenType;
    private OffsetDateTime createdAt;
    private OffsetDateTime expiresAt;
    private OffsetDateTime usedAt;
}
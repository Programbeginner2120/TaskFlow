package com.killeen.taskflow.components.user.model;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String email;
    private String passwordHash;
    private String displayName;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private boolean emailVerified;
}

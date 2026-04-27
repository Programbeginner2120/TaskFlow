# Backend Test Coverage Report — 2026-04-12.1

## Summary

| Metric | Value |
|--------|-------|
| Total production classes | 113 |
| Classes with meaningful test coverage | 0 |
| Classes with stub-only coverage | 1 (`TaskFlowApplicationTests` — `contextLoads()` only) |
| Classes with zero coverage | 112 |
| Coverage ratio | ~0% |

> **Note:** Generated MyBatis classes (`*Db`, `*DbExample`, `*DbMapper` — 24 classes total) are listed as Low in the gaps table and excluded from the Action Plan since they are auto-generated code.

---

## Coverage Map

### com.killeen (root)

| Class | Package | Test File | Status |
|-------|---------|-----------|--------|
| `TaskFlowApplication` | `com.killeen` | `TaskFlowApplicationTests` | ⚠️ Stub only |

### config

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `JwtService` | `@Service` | — | ❌ Missing |
| `JwtAuthenticationFilter` | `@Component` | — | ❌ Missing |
| `JwtProperties` | Config props | — | ❌ Missing |
| `EncryptionService` | `@Service` | — | ❌ Missing |
| `EncryptionProperties` | Config props | — | ❌ Missing |
| `EncryptionException` | Exception | — | ❌ Missing |
| `SecurityConfig` | `@Configuration` | — | ❌ Missing |
| `WebConfig` | `@Configuration` | — | ❌ Missing |
| `RetryConfig` | `@Configuration` | — | ❌ Missing |

### exception (global)

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `GlobalExceptionHandler` | `@RestControllerAdvice` | — | ❌ Missing |
| `ErrorResponse` | DTO | — | ❌ Missing |

### util

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `AuthUtils` | Utility | — | ❌ Missing |

### components.user

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `AuthController` | `@RestController` | — | ❌ Missing |
| `UserService` | `@Service` | — | ❌ Missing |
| `UserRepository` | `@Repository` | — | ❌ Missing |
| `UserConverter` | Converter | — | ❌ Missing |
| `User` | Domain model | — | ❌ Missing |
| `UserResponse` | DTO | — | ❌ Missing |
| `RegisterRequest` | DTO (with validation) | — | ❌ Missing |
| `LoginRequest` | DTO (with validation) | — | ❌ Missing |
| `LoginResponse` | DTO | — | ❌ Missing |
| `ForgotPasswordRequest` | DTO | — | ❌ Missing |
| `ResetPasswordRequest` | DTO | — | ❌ Missing |
| `ResendVerificationRequest` | DTO | — | ❌ Missing |
| `UserNotFoundException` | Exception | — | ❌ Missing |
| `UserAlreadyExistsException` | Exception | — | ❌ Missing |
| `InvalidCredentialsException` | Exception | — | ❌ Missing |

### components.task

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `TaskController` | `@RestController` | — | ❌ Missing |
| `TaskService` | `@Service` | — | ❌ Missing |
| `TaskEncryptionHelper` | `@Component` | — | ❌ Missing |
| `TaskRepository` | `@Repository` | — | ❌ Missing |
| `SubtaskRepository` | `@Repository` | — | ❌ Missing |
| `TaskConverter` | Converter | — | ❌ Missing |
| `SubtaskConverter` | Converter | — | ❌ Missing |
| `Task` | Domain model | — | ❌ Missing |
| `Subtask` | Domain model | — | ❌ Missing |
| `TaskResponse` | DTO | — | ❌ Missing |
| `SubtaskResponse` | DTO | — | ❌ Missing |
| `CreateTaskRequest` | DTO (with validation) | — | ❌ Missing |
| `UpdateTaskRequest` | DTO | — | ❌ Missing |
| `CreateSubtaskRequest` | DTO | — | ❌ Missing |
| `UpdateSubtaskRequest` | DTO | — | ❌ Missing |
| `TaskNotFoundException` | Exception | — | ❌ Missing |
| `SubtaskNotFoundException` | Exception | — | ❌ Missing |

### components.tasklist

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `TaskListController` | `@RestController` | — | ❌ Missing |
| `TaskListService` | `@Service` | — | ❌ Missing |
| `TaskListEncryptionHelper` | `@Component` | — | ❌ Missing |
| `TaskListRepository` | `@Repository` | — | ❌ Missing |
| `TaskListConverter` | Converter | — | ❌ Missing |
| `TaskList` | Domain model | — | ❌ Missing |
| `TaskListResponse` | DTO | — | ❌ Missing |
| `CreateTaskListRequest` | DTO | — | ❌ Missing |
| `UpdateTaskListRequest` | DTO | — | ❌ Missing |
| `TaskListNotFoundException` | Exception | — | ❌ Missing |

### components.tasklisttemplate

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `TaskListTemplateController` | `@RestController` | — | ❌ Missing |
| `TemplateGenerationController` | `@RestController` | — | ❌ Missing |
| `TaskListTemplateService` | `@Service` | — | ❌ Missing |
| `TemplateGeneratorService` | `@Service` | — | ❌ Missing |
| `RruleService` | `@Service` | — | ❌ Missing |
| `TaskListTemplateEncryptionHelper` | `@Component` | — | ❌ Missing |
| `TaskListTemplateRepository` | `@Repository` | — | ❌ Missing |
| `TaskTemplateRepository` | `@Repository` | — | ❌ Missing |
| `SubtaskTemplateRepository` | `@Repository` | — | ❌ Missing |
| `TaskListTemplateConverter` | Converter | — | ❌ Missing |
| `TaskTemplateConverter` | Converter | — | ❌ Missing |
| `SubtaskTemplateConverter` | Converter | — | ❌ Missing |
| `TaskListTemplate` | Domain model | — | ❌ Missing |
| `TaskTemplate` | Domain model | — | ❌ Missing |
| `SubtaskTemplate` | Domain model | — | ❌ Missing |
| `TaskListTemplateResponse` | DTO | — | ❌ Missing |
| `TaskTemplateResponse` | DTO | — | ❌ Missing |
| `SubtaskTemplateResponse` | DTO | — | ❌ Missing |
| `CreateTaskListTemplateRequest` | DTO | — | ❌ Missing |
| `CreateTaskTemplateRequest` | DTO | — | ❌ Missing |
| `CreateSubtaskTemplateRequest` | DTO | — | ❌ Missing |
| `UpdateTaskListTemplateRequest` | DTO | — | ❌ Missing |
| `InvalidRruleException` | Exception | — | ❌ Missing |
| `TaskListTemplateNotFoundException` | Exception | — | ❌ Missing |

### components.email

| Class | Type | Test File | Status |
|-------|------|-----------|--------|
| `EmailTokenService` | `@Service` | — | ❌ Missing |
| `EmailService` | `@Service` | — | ❌ Missing |
| `EmailTokenRepository` | `@Repository` | — | ❌ Missing |
| `EmailTokenConverter` | Converter | — | ❌ Missing |
| `EmailToken` | Domain model | — | ❌ Missing |
| `EmailTokenType` | Enum | — | ❌ Missing |
| `EmailNotVerifiedException` | Exception | — | ❌ Missing |
| `InvalidTokenException` | Exception | — | ❌ Missing |

### db (generated — auto-generated by MyBatis Generator)

| Class | Status |
|-------|--------|
| `UserDb`, `UserDbExample`, `UserDbMapper` | ❌ Missing (Low) |
| `TaskDb`, `TaskDbExample`, `TaskDbMapper` | ❌ Missing (Low) |
| `TaskListDb`, `TaskListDbExample`, `TaskListDbMapper` | ❌ Missing (Low) |
| `TaskTemplateDb`, `TaskTemplateDbExample`, `TaskTemplateDbMapper` | ❌ Missing (Low) |
| `SubtaskDb`, `SubtaskDbExample`, `SubtaskDbMapper` | ❌ Missing (Low) |
| `SubtaskTemplateDb`, `SubtaskTemplateDbExample`, `SubtaskTemplateDbMapper` | ❌ Missing (Low) |
| `EmailTokenDb`, `EmailTokenDbExample`, `EmailTokenDbMapper` | ❌ Missing (Low) |
| `TaskListTemplateDb`, `TaskListTemplateDbExample`, `TaskListTemplateDbMapper` | ❌ Missing (Low) |
| `TaskListTemplateDbCustomMapper` | ❌ Missing (Medium) |
| `PostgresEnumTypeHandler` | ❌ Missing (Medium) |

---

## Gaps by Criticality

### Critical

| Class | Reason |
|-------|--------|
| `AuthController` | Security-sensitive REST endpoints (register, login, verify-email, reset-password, forgot-password) — zero tests. Happy-path, validation error, and unauthenticated paths all untested. |
| `UserService` | Core auth logic with `@Transactional` methods — zero tests. `register`, `authenticate`, `verifyEmail`, `resetPassword` all have untested error paths. Password hash verification untested. |
| `JwtService` | JWT token generation and parsing — zero tests. Malformed/expired token handling fully untested. |
| `JwtAuthenticationFilter` | Security filter that gates every protected endpoint — zero tests. Behavior when no `Authorization` header, invalid token, or valid token each untested. |
| `EncryptionService` | Encrypts and decrypts all user task data at rest — zero tests. Null handling, encryption roundtrip, and corrupt-data exception paths all untested. |
| `EmailTokenService` | Hashes tokens with SHA-256 and validates them for email verification and password reset — zero tests. Expired, already-used, and wrong-type token paths all untested. |
| `TaskService` | Core task CRUD with `@Transactional` methods and ownership enforcement — zero tests. Cross-user access, subtask not-found, and listId validation paths all untested. |
| `TaskController` | Primary REST API surface for tasks and subtasks — zero tests. No happy-path, validation, or 404 coverage. |
| `TemplateGenerationController` | Uses a shared secret (`X-Cron-Secret`) to guard an internal endpoint — zero tests. An invalid secret returning 401 is completely untested. |

### High

| Class | Reason |
|-------|--------|
| `TaskListService` | CRUD service with `@Transactional` update — zero tests. Not-found and wrong-user paths untested. |
| `TaskListController` | REST API for task lists — zero tests. |
| `TaskListTemplateService` | Complex `@Transactional` create/update that also computes `nextGenerate` from an rrule — zero tests. Not-found, reschedule, and `generateDueTemplates` paths untested. |
| `TaskListTemplateController` | REST API for templates — zero tests. |
| `TemplateGeneratorService` | `@Transactional` method that creates a `TaskList` + `Task`s + `Subtask`s from a template — zero tests. "Already claimed" idempotency check completely untested. |
| `RruleService` | Third-party `ical4j` integration for date computation — zero tests. Invalid rrule, no-future-occurrence, and valid-rrule paths all untested. |
| `GlobalExceptionHandler` | `@RestControllerAdvice` mapping every domain exception to an HTTP status — zero tests. None of the 9 `@ExceptionHandler` methods are verified. |
| `TaskEncryptionHelper` | Encrypts/decrypts task and subtask titles and notes — zero tests. Null notes field handling untested. |
| `TaskListEncryptionHelper` | Encrypts/decrypts task list names — zero tests. |
| `TaskListTemplateEncryptionHelper` | Encrypts/decrypts template data — zero tests. |

### Medium

| Class | Reason |
|-------|--------|
| `UserRepository` | Custom MyBatis-backed repository with `findByEmail`, `setEmailVerified`, `updatePassword` — no query validation test. |
| `TaskRepository` | Custom repository with `findByIdAndUserId`, `findAllByUserId` ownership queries — no DB test. |
| `SubtaskRepository` | Custom repository with ownership-scoped `findByIdAndTaskId` — no DB test. |
| `TaskListRepository` | Custom repository with `findByIdAndUserId` — no DB test. |
| `TaskListTemplateRepository` | Custom repository including `findDueTemplates` and `claimIfStillDue` (concurrency logic) — no DB test. |
| `TaskTemplateRepository` | Custom repository with `deleteAllByListTemplateId` — no DB test. |
| `SubtaskTemplateRepository` | Custom repository — no DB test. |
| `EmailTokenRepository` | Custom repository with `findByToken` and `markUsed` — no DB test. |
| `TaskListTemplateDbCustomMapper` | Hand-written MyBatis mapper for custom queries — no DB test. |
| `PostgresEnumTypeHandler` | Custom MyBatis type handler for PostgreSQL enums — no unit test. |
| `TaskConverter` | Converts `TaskDb` ↔ `Task` domain model including subtask list — not tested. |
| `SubtaskConverter` | Converter — not tested. |
| `UserConverter` | Converter — not tested. |
| `TaskListConverter` | Converter — not tested. |
| `TaskListTemplateConverter` | Converter — not tested. |
| `TaskTemplateConverter` | Converter — not tested. |
| `SubtaskTemplateConverter` | Converter — not tested. |
| `EmailTokenConverter` | Converter — not tested. |
| `TaskFlowApplicationTests` | Stub only — `contextLoads()` with no assertions. |

### Low

| Class | Reason |
|-------|--------|
| `AuthUtils` | Trivial one-liner utility — untested but low risk. |
| `ErrorResponse` | Simple DTO — untested but no logic. |
| `RegisterRequest` / `LoginRequest` / other request DTOs | Bean Validation constraints not exercised in isolation, though covered indirectly by controller tests once those are written. |
| `*NotFound*Exception` / other exception classes | No logic — untested but trivial. |
| `SecurityConfig` / `WebConfig` / `RetryConfig` | Configuration classes — no logic to unit test; integration context load is sufficient. |
| `JwtProperties` / `EncryptionProperties` | `@ConfigurationProperties` holders — no logic. |
| Generated `*Db`, `*DbExample`, generated `*DbMapper` (24 classes) | Auto-generated by MyBatis Generator — not worth testing directly. |

---

## Action Plan

### `JwtService` — Critical

**Why it matters:** Every protected API call depends on `isTokenValid()` and `extractUserId()`. A token that has been tampered with, is expired, or uses the wrong key must be rejected; this is never asserted anywhere.

```java
package com.killeen.taskflow.config;

import com.killeen.taskflow.components.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties();
        props.setSecret("test-secret-key-that-is-at-least-32-bytes-long!");
        props.setExpiration(3_600_000L); // 1 hour
        jwtService = new JwtService(props);
        jwtService.init();
    }

    private User testUser() {
        return User.builder()
                .id(42L)
                .email("user@example.com")
                .displayName("Test User")
                .build();
    }

    @Test
    @DisplayName("generateToken_validUser_producesNonBlankJwt")
    void generateToken_validUser_producesNonBlankJwt() {
        String token = jwtService.generateToken(testUser());
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("extractUserId_afterGenerate_returnsSameId")
    void extractUserId_afterGenerate_returnsSameId() {
        String token = jwtService.generateToken(testUser());
        assertThat(jwtService.extractUserId(token)).isEqualTo(42L);
    }

    @Test
    @DisplayName("isTokenValid_freshToken_returnsTrue")
    void isTokenValid_freshToken_returnsTrue() {
        String token = jwtService.generateToken(testUser());
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    @DisplayName("isTokenValid_expiredToken_returnsFalse")
    void isTokenValid_expiredToken_returnsFalse() {
        JwtProperties shortProps = new JwtProperties();
        shortProps.setSecret("test-secret-key-that-is-at-least-32-bytes-long!");
        shortProps.setExpiration(-1L); // already expired
        JwtService shortJwtService = new JwtService(shortProps);
        shortJwtService.init();

        String token = shortJwtService.generateToken(testUser());
        assertThat(shortJwtService.isTokenValid(token)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid_garbageString_returnsFalse")
    void isTokenValid_garbageString_returnsFalse() {
        assertThat(jwtService.isTokenValid("not.a.jwt")).isFalse();
    }
}
```

---

### `EncryptionService` — Critical

**Why it matters:** All task titles, notes, and template names are encrypted at rest. If the encrypt/decrypt roundtrip silently fails, data is corrupted or readable in plaintext in the DB. Null handling must also be verified so service code doesn't NPE.

```java
package com.killeen.taskflow.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        EncryptionProperties props = new EncryptionProperties();
        props.setPassword("testpassword");
        props.setSalt("deadbeefdeadbeef"); // 16 hex chars
        encryptionService = new EncryptionService(props);
        encryptionService.init();
    }

    @Test
    @DisplayName("encrypt_thenDecrypt_returnsOriginalPlaintext")
    void encrypt_thenDecrypt_returnsOriginalPlaintext() {
        String plaintext = "My secret task title";
        String ciphertext = encryptionService.encrypt(plaintext);
        assertThat(encryptionService.decrypt(ciphertext)).isEqualTo(plaintext);
    }

    @Test
    @DisplayName("encrypt_nullInput_returnsNull")
    void encrypt_nullInput_returnsNull() {
        assertThat(encryptionService.encrypt(null)).isNull();
    }

    @Test
    @DisplayName("decrypt_nullInput_returnsNull")
    void decrypt_nullInput_returnsNull() {
        assertThat(encryptionService.decrypt(null)).isNull();
    }

    @Test
    @DisplayName("encrypt_differentCallsProduceDifferentCiphertext")
    void encrypt_differentCallsProduceDifferentCiphertext() {
        // Spring's Encryptors.text uses a random IV per call — same plaintext must not produce identical output
        String ct1 = encryptionService.encrypt("hello");
        String ct2 = encryptionService.encrypt("hello");
        assertThat(ct1).isNotEqualTo(ct2);
    }

    @Test
    @DisplayName("decrypt_corruptedCiphertext_throwsEncryptionException")
    void decrypt_corruptedCiphertext_throwsEncryptionException() {
        assertThatThrownBy(() -> encryptionService.decrypt("not-valid-ciphertext"))
                .isInstanceOf(EncryptionException.class);
    }
}
```

---

### `EmailTokenService` — Critical

**Why it matters:** This service controls email verification and password reset. Storing a raw token in the DB instead of the hash, or failing to reject expired/already-used tokens, is a security vulnerability.

```java
package com.killeen.taskflow.components.email.service;

import com.killeen.taskflow.components.email.exception.InvalidTokenException;
import com.killeen.taskflow.components.email.model.EmailToken;
import com.killeen.taskflow.components.email.model.EmailTokenType;
import com.killeen.taskflow.components.email.repository.EmailTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailTokenServiceTest {

    @Mock private EmailTokenRepository emailTokenRepository;
    @Mock private Environment env;

    private EmailTokenService emailTokenService;

    @BeforeEach
    void setUp() {
        emailTokenService = new EmailTokenService(emailTokenRepository, env);
        ReflectionTestUtils.setField(emailTokenService, "verificationTtlHours", 24);
        ReflectionTestUtils.setField(emailTokenService, "resetTtlHours", 1);
    }

    @Test
    @DisplayName("createToken_savesHashedToken_notRawUuid")
    void createToken_savesHashedToken_notRawUuid() {
        ArgumentCaptor<EmailToken> captor = ArgumentCaptor.forClass(EmailToken.class);

        String rawToken = emailTokenService.createToken(1L, EmailTokenType.VERIFY_EMAIL);

        verify(emailTokenRepository).save(captor.capture());
        EmailToken saved = captor.getValue();
        // The stored token must NOT be the raw UUID
        assertThat(saved.getToken()).isNotEqualTo(rawToken);
        // It should be a 64-char hex SHA-256 hash
        assertThat(saved.getToken()).matches("[a-f0-9]{64}");
    }

    @Test
    @DisplayName("validateAndConsume_validToken_returnsEmailToken")
    void validateAndConsume_validToken_returnsEmailToken() {
        String rawToken = "some-raw-token";
        EmailToken stored = EmailToken.builder()
                .userId(1L)
                .tokenType(EmailTokenType.VERIFY_EMAIL)
                .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1))
                .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(stored));

        EmailToken result = emailTokenService.validateAndConsume(rawToken, EmailTokenType.VERIFY_EMAIL);

        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("validateAndConsume_expiredToken_throwsInvalidTokenException")
    void validateAndConsume_expiredToken_throwsInvalidTokenException() {
        EmailToken expired = EmailToken.builder()
                .userId(1L)
                .tokenType(EmailTokenType.VERIFY_EMAIL)
                .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).minusHours(1))
                .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(expired));
        when(env.getProperty(any())).thenReturn("Token has expired");

        assertThatThrownBy(() ->
                emailTokenService.validateAndConsume("raw", EmailTokenType.VERIFY_EMAIL))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("validateAndConsume_alreadyUsedToken_throwsInvalidTokenException")
    void validateAndConsume_alreadyUsedToken_throwsInvalidTokenException() {
        EmailToken used = EmailToken.builder()
                .userId(1L)
                .tokenType(EmailTokenType.VERIFY_EMAIL)
                .usedAt(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(5))
                .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1))
                .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(used));
        when(env.getProperty(any())).thenReturn("Already used");

        assertThatThrownBy(() ->
                emailTokenService.validateAndConsume("raw", EmailTokenType.VERIFY_EMAIL))
                .isInstanceOf(InvalidTokenException.class);
    }

    @Test
    @DisplayName("validateAndConsume_wrongType_throwsInvalidTokenException")
    void validateAndConsume_wrongType_throwsInvalidTokenException() {
        EmailToken wrongType = EmailToken.builder()
                .userId(1L)
                .tokenType(EmailTokenType.RESET_PASSWORD)
                .expiresAt(OffsetDateTime.now(ZoneOffset.UTC).plusHours(1))
                .build();
        when(emailTokenRepository.findByToken(any())).thenReturn(Optional.of(wrongType));
        when(env.getProperty(any())).thenReturn("Wrong type");

        assertThatThrownBy(() ->
                emailTokenService.validateAndConsume("raw", EmailTokenType.VERIFY_EMAIL))
                .isInstanceOf(InvalidTokenException.class);
    }
}
```

---

### `UserService` — Critical

**Why it matters:** Orchestrates registration, login, email verification, and password reset — all security paths. `@Transactional` methods (`register`, `verifyEmail`, `resetPassword`) have no rollback coverage.

```java
package com.killeen.taskflow.components.user.service;

import com.killeen.taskflow.components.email.exception.EmailNotVerifiedException;
import com.killeen.taskflow.components.email.model.EmailTokenType;
import com.killeen.taskflow.components.email.service.EmailService;
import com.killeen.taskflow.components.email.service.EmailTokenService;
import com.killeen.taskflow.components.user.exception.InvalidCredentialsException;
import com.killeen.taskflow.components.user.exception.UserAlreadyExistsException;
import com.killeen.taskflow.components.user.exception.UserNotFoundException;
import com.killeen.taskflow.components.user.model.LoginResponse;
import com.killeen.taskflow.components.user.model.User;
import com.killeen.taskflow.components.user.repository.UserRepository;
import com.killeen.taskflow.config.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtService jwtService;
    @Mock private EmailTokenService emailTokenService;
    @Mock private EmailService emailService;
    @Mock private Environment env;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(
                userRepository, passwordEncoder, jwtService,
                emailTokenService, emailService, env);
    }

    // -------------------------------------------------------------------------
    // register
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("register_newEmail_savesUserAndSendsVerificationEmail")
    void register_newEmail_savesUserAndSendsVerificationEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        when(userRepository.save(any())).thenAnswer(inv -> {
            User u = inv.getArgument(0);
            u.setId(1L);
            return u;
        });
        when(emailTokenService.createToken(1L, EmailTokenType.VERIFY_EMAIL)).thenReturn("raw-token");

        User result = userService.register("user@example.com", "Pass1234!", "Alice");

        assertThat(result.getEmail()).isEqualTo("user@example.com");
        assertThat(result.isEmailVerified()).isFalse();
        verify(emailService).sendVerificationEmail("user@example.com", "raw-token");
    }

    @Test
    @DisplayName("register_duplicateEmail_throwsUserAlreadyExistsException")
    void register_duplicateEmail_throwsUserAlreadyExistsException() {
        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(User.builder().build()));

        assertThatThrownBy(() -> userService.register("dup@example.com", "Pass1!", "Bob"))
                .isInstanceOf(UserAlreadyExistsException.class);
        verify(userRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // authenticate
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("authenticate_validCredentials_returnsLoginResponse")
    void authenticate_validCredentials_returnsLoginResponse() {
        User user = User.builder()
                .id(1L).email("user@example.com")
                .passwordHash("hashed").emailVerified(true).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Pass1!", "hashed")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");
        when(jwtService.getExpirationMs()).thenReturn(3_600_000L);

        LoginResponse response = userService.authenticate("user@example.com", "Pass1!");

        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

    @Test
    @DisplayName("authenticate_wrongPassword_throwsInvalidCredentialsException")
    void authenticate_wrongPassword_throwsInvalidCredentialsException() {
        User user = User.builder()
                .id(1L).passwordHash("hashed").emailVerified(true).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> userService.authenticate("u@e.com", "wrong"))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    @DisplayName("authenticate_emailNotVerified_throwsEmailNotVerifiedException")
    void authenticate_emailNotVerified_throwsEmailNotVerifiedException() {
        User user = User.builder()
                .id(1L).passwordHash("hashed").emailVerified(false).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.authenticate("u@e.com", "Pass1!"))
                .isInstanceOf(EmailNotVerifiedException.class);
    }

    // -------------------------------------------------------------------------
    // getUserById
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getUserById_existingId_returnsUser")
    void getUserById_existingId_returnsUser() {
        User user = User.builder().id(5L).build();
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));

        assertThat(userService.getUserById(5L).getId()).isEqualTo(5L);
    }

    @Test
    @DisplayName("getUserById_missingId_throwsUserNotFoundException")
    void getUserById_missingId_throwsUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
```

---

### `AuthController` — Critical

**Why it matters:** The sole entry point for all authentication. Validation constraints (`@Valid`) on request bodies must be exercised to confirm 400 responses; security boundaries (e.g., `/auth/me` requiring auth) must be verified.

```java
package com.killeen.taskflow.components.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.killeen.taskflow.components.user.exception.InvalidCredentialsException;
import com.killeen.taskflow.components.user.exception.UserAlreadyExistsException;
import com.killeen.taskflow.components.user.model.*;
import com.killeen.taskflow.components.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockBean  UserService userService;

    // -------------------------------------------------------------------------
    // POST /auth/register
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("register_validRequest_returns201")
    void register_validRequest_returns201() throws Exception {
        RegisterRequest req = new RegisterRequest("user@example.com", "Pass1234!", "Alice");
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
    @DisplayName("register_blankEmail_returns400")
    void register_blankEmail_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest("", "Pass1234!", "Alice");

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("register_duplicateEmail_returns409")
    void register_duplicateEmail_returns409() throws Exception {
        RegisterRequest req = new RegisterRequest("dup@example.com", "Pass1234!", "Alice");
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
    @DisplayName("login_validCredentials_returns200WithToken")
    void login_validCredentials_returns200WithToken() throws Exception {
        LoginRequest req = new LoginRequest("user@example.com", "Pass1234!");
        when(userService.authenticate(anyString(), anyString()))
                .thenReturn(LoginResponse.builder().token("jwt").expiresIn(3600000L).build());

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt"));
    }

    @Test
    @DisplayName("login_invalidCredentials_returns401")
    void login_invalidCredentials_returns401() throws Exception {
        LoginRequest req = new LoginRequest("user@example.com", "wrong");
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
    @DisplayName("getCurrentUser_unauthenticated_returns401")
    void getCurrentUser_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "1") // principal = userId "1"
    @DisplayName("getCurrentUser_authenticated_returns200")
    void getCurrentUser_authenticated_returns200() throws Exception {
        when(userService.getUserById(anyLong()))
                .thenReturn(User.builder().id(1L).email("u@e.com").displayName("Alice").build());

        mockMvc.perform(get("/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("u@e.com"));
    }
}
```

---

### `TemplateGenerationController` — Critical

**Why it matters:** This internal endpoint triggers mass data generation. It relies on an `X-Cron-Secret` header for access control. An invalid or absent secret returning 200 would be a security hole — currently untested.

```java
package com.killeen.taskflow.components.tasklisttemplate.controller;

import com.killeen.taskflow.components.tasklisttemplate.service.TaskListTemplateService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TemplateGenerationController.class)
@TestPropertySource(properties = "app.cron.secret=test-secret")
class TemplateGenerationControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean  TaskListTemplateService taskListTemplateService;

    @Test
    @DisplayName("generateTemplates_validSecret_returns200AndTriggesGeneration")
    void generateTemplates_validSecret_returns200AndTriggersGeneration() throws Exception {
        mockMvc.perform(post("/internal/generate-templates")
                        .header("X-Cron-Secret", "test-secret"))
                .andExpect(status().isOk());

        verify(taskListTemplateService).generateDueTemplates();
    }

    @Test
    @DisplayName("generateTemplates_invalidSecret_returns401AndNoGeneration")
    void generateTemplates_invalidSecret_returns401AndNoGeneration() throws Exception {
        mockMvc.perform(post("/internal/generate-templates")
                        .header("X-Cron-Secret", "wrong-secret"))
                .andExpect(status().isUnauthorized());

        verify(taskListTemplateService, never()).generateDueTemplates();
    }

    @Test
    @DisplayName("generateTemplates_missingSecretHeader_returns401")
    void generateTemplates_missingSecretHeader_returns401() throws Exception {
        mockMvc.perform(post("/internal/generate-templates"))
                .andExpect(status().isUnauthorized());
    }
}
```

---

### `TaskService` — Critical

**Why it matters:** Core business logic for the app's main feature. Ownership enforcement (`findByIdAndUserId`) prevents cross-user data access — this is entirely untested.

```java
package com.killeen.taskflow.components.task.service;

import com.killeen.taskflow.components.task.TaskEncryptionHelper;
import com.killeen.taskflow.components.task.exception.TaskNotFoundException;
import com.killeen.taskflow.components.task.model.CreateTaskRequest;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.components.task.repository.SubtaskRepository;
import com.killeen.taskflow.components.task.repository.TaskRepository;
import com.killeen.taskflow.components.tasklist.repository.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock private TaskRepository taskRepository;
    @Mock private SubtaskRepository subtaskRepository;
    @Mock private TaskListRepository taskListRepository;
    @Mock private TaskEncryptionHelper encryptionHelper;
    @Mock private Environment env;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(
                taskRepository, subtaskRepository, taskListRepository, encryptionHelper, env);
    }

    // -------------------------------------------------------------------------
    // getAllTasks
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("getAllTasks_returnsDecryptedTasks")
    void getAllTasks_returnsDecryptedTasks() {
        Task encrypted = Task.builder().id(1L).title("enc").subtasks(List.of()).build();
        Task decrypted = Task.builder().id(1L).title("My Task").subtasks(List.of()).build();
        when(taskRepository.findAllByUserId(1L)).thenReturn(List.of(encrypted));
        when(encryptionHelper.decryptTask(encrypted)).thenReturn(decrypted);

        List<Task> result = taskService.getAllTasks(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("My Task");
    }

    // -------------------------------------------------------------------------
    // createTask
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("createTask_noListId_savesAndReturnsTask")
    void createTask_noListId_savesAndReturnsTask() {
        CreateTaskRequest req = new CreateTaskRequest();
        req.setTitle("Test Task");

        Task encrypted = Task.builder().title("enc").subtasks(List.of()).build();
        when(encryptionHelper.encryptTask(any())).thenReturn(encrypted);
        when(taskRepository.save(encrypted)).thenReturn(10L);

        Task result = taskService.createTask(1L, req);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getTitle()).isEqualTo("Test Task");
    }

    // -------------------------------------------------------------------------
    // deleteTask
    // -------------------------------------------------------------------------

    @Test
    @DisplayName("deleteTask_taskBelongsToOtherUser_throwsTaskNotFoundException")
    void deleteTask_taskBelongsToOtherUser_throwsTaskNotFoundException() {
        when(taskRepository.findByIdAndUserId(99L, 1L)).thenReturn(Optional.empty());
        when(env.getProperty(any())).thenReturn("Not found");

        assertThatThrownBy(() -> taskService.deleteTask(1L, 99L))
                .isInstanceOf(TaskNotFoundException.class);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("deleteTask_ownedTask_deletesSuccessfully")
    void deleteTask_ownedTask_deletesSuccessfully() {
        Task task = Task.builder().id(5L).subtasks(List.of()).build();
        when(taskRepository.findByIdAndUserId(5L, 1L)).thenReturn(Optional.of(task));

        taskService.deleteTask(1L, 5L);

        verify(taskRepository).deleteById(5L);
    }
}
```

---

### `RruleService` — High

**Why it matters:** Every template schedule depends on `computeNextGenerate()`. A malformed rrule or an rrule with no future occurrences must raise a specific exception — otherwise a template silently stops generating tasks.

```java
package com.killeen.taskflow.components.tasklisttemplate.service;

import com.killeen.taskflow.components.tasklisttemplate.exception.InvalidRruleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RruleServiceTest {

    @Mock private Environment env;

    private RruleService rruleService;

    @BeforeEach
    void setUp() {
        rruleService = new RruleService(env);
        when(env.getProperty(any())).thenReturn("Invalid rrule");
    }

    @Test
    @DisplayName("computeNextGenerate_weeklyRrule_returnsFutureDate")
    void computeNextGenerate_weeklyRrule_returnsFutureDate() {
        OffsetDateTime result = rruleService.computeNextGenerate("FREQ=WEEKLY", "UTC");
        assertThat(result).isAfter(OffsetDateTime.now());
    }

    @Test
    @DisplayName("computeNextGenerate_dailyRrule_returnsNextDay")
    void computeNextGenerate_dailyRrule_returnsFutureDate() {
        OffsetDateTime result = rruleService.computeNextGenerate("FREQ=DAILY", "America/New_York");
        assertThat(result).isAfter(OffsetDateTime.now());
    }

    @Test
    @DisplayName("computeNextGenerate_garbageRrule_throwsInvalidRruleException")
    void computeNextGenerate_garbageRrule_throwsInvalidRruleException() {
        assertThatThrownBy(() -> rruleService.computeNextGenerate("NOT_VALID", "UTC"))
                .isInstanceOf(InvalidRruleException.class);
    }
}
```

---

### `GlobalExceptionHandler` — High

**Why it matters:** All REST error responses flow through this class. If a mapping is missing or maps to the wrong status, clients get incorrect HTTP codes. Currently none of the 9 `@ExceptionHandler` methods are verified.

```java
package com.killeen.taskflow.exception;

import com.killeen.taskflow.components.task.exception.TaskNotFoundException;
import com.killeen.taskflow.components.user.exception.InvalidCredentialsException;
import com.killeen.taskflow.components.user.exception.UserAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;

// Can be tested as a plain unit class — no Spring context needed
class GlobalExceptionHandlerTest {

    private final Environment env = mock(Environment.class);
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler(env);

    @Test
    @DisplayName("handleUserAlreadyExists_returns409Conflict")
    void handleUserAlreadyExists_returns409Conflict() {
        ResponseEntity<ErrorResponse> response =
                handler.handleUserAlreadyExists(new UserAlreadyExistsException("exists"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    @DisplayName("handleInvalidCredentials_returns401Unauthorized")
    void handleInvalidCredentials_returns401Unauthorized() {
        ResponseEntity<ErrorResponse> response =
                handler.handleInvalidCredentials(new InvalidCredentialsException("bad"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    @DisplayName("handleTaskNotFound_returns404NotFound")
    void handleTaskNotFound_returns404NotFound() {
        ResponseEntity<ErrorResponse> response =
                handler.handleTaskNotFound(new TaskNotFoundException("missing"));
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
```

---

### `TemplateGeneratorService` — High

**Why it matters:** The `generateFromTemplate` method is the most complex `@Transactional` operation in the codebase — creating a `TaskList`, multiple `Task`s, and multiple `Subtask`s atomically. The idempotency guard (`claimIfStillDue`) that prevents double-generation on concurrent scheduler invocations is completely untested.

```java
package com.killeen.taskflow.components.tasklisttemplate.service;

import com.killeen.taskflow.components.task.TaskEncryptionHelper;
import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.components.task.repository.SubtaskRepository;
import com.killeen.taskflow.components.task.repository.TaskRepository;
import com.killeen.taskflow.components.tasklist.TaskListEncryptionHelper;
import com.killeen.taskflow.components.tasklist.repository.TaskListRepository;
import com.killeen.taskflow.components.tasklisttemplate.model.*;
import com.killeen.taskflow.components.tasklisttemplate.repository.TaskListTemplateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateGeneratorServiceTest {

    @Mock private TaskListRepository taskListRepository;
    @Mock private TaskRepository taskRepository;
    @Mock private SubtaskRepository subtaskRepository;
    @Mock private TaskListEncryptionHelper taskListEncryptionHelper;
    @Mock private TaskEncryptionHelper taskEncryptionHelper;
    @Mock private TaskListTemplateRepository templateRepository;
    @Mock private RruleService rruleService;

    private TemplateGeneratorService service;

    @BeforeEach
    void setUp() {
        service = new TemplateGeneratorService(
                taskListRepository, taskRepository, subtaskRepository,
                taskListEncryptionHelper, taskEncryptionHelper, templateRepository, rruleService);
    }

    private TaskListTemplate buildTemplate() {
        SubtaskTemplate subtask = SubtaskTemplate.builder().title("Sub 1").build();
        TaskTemplate task = TaskTemplate.builder()
                .title("Task 1").dueDateOffset(0)
                .subtaskTemplates(List.of(subtask))
                .build();
        return TaskListTemplate.builder()
                .id(1L).userId(10L).name("Weekly Work").color("#fff")
                .rrule("FREQ=WEEKLY").timezone("UTC")
                .nextGenerate(OffsetDateTime.now(ZoneOffset.UTC).minusMinutes(1))
                .taskTemplates(List.of(task))
                .build();
    }

    @Test
    @DisplayName("generateFromTemplate_claimed_createsTaskListAndTasks")
    void generateFromTemplate_claimed_createsTaskListAndTasks() {
        TaskListTemplate template = buildTemplate();
        when(templateRepository.claimIfStillDue(1L, template.getNextGenerate())).thenReturn(true);
        when(taskListEncryptionHelper.encrypt(any())).thenAnswer(inv -> inv.getArgument(0));
        when(taskEncryptionHelper.encryptTask(any())).thenAnswer(inv -> inv.getArgument(0));
        when(taskEncryptionHelper.encryptSubtask(any())).thenAnswer(inv -> inv.getArgument(0));
        when(taskListRepository.save(any())).thenReturn(100L);
        when(taskRepository.save(any())).thenReturn(200L);
        when(rruleService.computeNextGenerate(anyString(), anyString()))
                .thenReturn(OffsetDateTime.now(ZoneOffset.UTC).plusWeeks(1));

        service.generateFromTemplate(template);

        verify(taskListRepository).save(any());
        verify(taskRepository).save(any());
        verify(subtaskRepository).save(any());
        verify(templateRepository).updateSchedule(eq(1L), any(), any());
    }

    @Test
    @DisplayName("generateFromTemplate_alreadyClaimed_skipsGeneration")
    void generateFromTemplate_alreadyClaimed_skipsGeneration() {
        TaskListTemplate template = buildTemplate();
        when(templateRepository.claimIfStillDue(1L, template.getNextGenerate())).thenReturn(false);

        service.generateFromTemplate(template);

        verify(taskListRepository, never()).save(any());
        verify(taskRepository, never()).save(any());
    }
}
```

---

### `TaskEncryptionHelper` — High

**Why it matters:** Every task and subtask stored in the database passes through this helper. A bug here would mean either corrupted data or plaintext task content stored in the DB.

```java
package com.killeen.taskflow.components.task;

import com.killeen.taskflow.components.task.model.Subtask;
import com.killeen.taskflow.components.task.model.Task;
import com.killeen.taskflow.config.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskEncryptionHelperTest {

    @Mock private EncryptionService encryptionService;

    private TaskEncryptionHelper helper;

    @BeforeEach
    void setUp() {
        helper = new TaskEncryptionHelper(encryptionService);
    }

    @Test
    @DisplayName("encryptTask_encryptsTitleAndNotes")
    void encryptTask_encryptsTitleAndNotes() {
        when(encryptionService.encrypt("My Title")).thenReturn("ENC_TITLE");
        when(encryptionService.encrypt("My Notes")).thenReturn("ENC_NOTES");

        Task task = Task.builder().title("My Title").notes("My Notes").subtasks(List.of()).build();
        Task encrypted = helper.encryptTask(task);

        assertThat(encrypted.getTitle()).isEqualTo("ENC_TITLE");
        assertThat(encrypted.getNotes()).isEqualTo("ENC_NOTES");
    }

    @Test
    @DisplayName("decryptTask_decryptsTitleNotesAndSubtasks")
    void decryptTask_decryptsTitleNotesAndSubtasks() {
        when(encryptionService.decrypt("ENC_TITLE")).thenReturn("My Title");
        when(encryptionService.decrypt("ENC_NOTES")).thenReturn("My Notes");
        when(encryptionService.decrypt("ENC_SUB")).thenReturn("Sub Title");

        Subtask encSubtask = Subtask.builder().title("ENC_SUB").build();
        Task task = Task.builder()
                .title("ENC_TITLE").notes("ENC_NOTES")
                .subtasks(List.of(encSubtask)).build();

        Task decrypted = helper.decryptTask(task);

        assertThat(decrypted.getTitle()).isEqualTo("My Title");
        assertThat(decrypted.getNotes()).isEqualTo("My Notes");
        assertThat(decrypted.getSubtasks().get(0).getTitle()).isEqualTo("Sub Title");
    }

    @Test
    @DisplayName("encryptTask_nullNotes_propagatesNullToEncryptionService")
    void encryptTask_nullNotes_propagatesNullToEncryptionService() {
        when(encryptionService.encrypt("My Title")).thenReturn("ENC_TITLE");
        when(encryptionService.encrypt(null)).thenReturn(null);

        Task task = Task.builder().title("My Title").notes(null).subtasks(List.of()).build();
        Task encrypted = helper.encryptTask(task);

        assertThat(encrypted.getNotes()).isNull();
    }
}
```

---

## Checklist Compliance

The following items from `.github/skills/review-test-coverage/references/checklist.md` are **failing**:

### Controllers
- ❌ **Every `@RestController` class has a test using `@WebMvcTest` or `@SpringBootTest`**
  — Failing for: `AuthController`, `TaskController`, `TaskListController`, `TaskListTemplateController`, `TemplateGenerationController`
- ❌ **Happy path (valid input → expected response) tested** — All 5 controllers
- ❌ **Bad input path (invalid/missing fields → 400) tested** — All 5 controllers
- ❌ **Unauthorized path (if applicable → 401/403) tested** — `AuthController` (`/auth/me`), `TemplateGenerationController` (`X-Cron-Secret` check)
- ❌ **Not-found path (missing resource → 404) tested** — `TaskController`, `TaskListController`, `TaskListTemplateController`

### Services
- ❌ **Every `@Service` class has a unit test with `@ExtendWith(MockitoExtension.class)`**
  — Failing for: `UserService`, `TaskService`, `TaskListService`, `TaskListTemplateService`, `TemplateGeneratorService`, `RruleService`, `JwtService`, `EncryptionService`, `EmailTokenService`, `EmailService`
- ❌ **All public methods tested** — All services
- ❌ **`Optional.empty()` path tested for nullable lookups** — `UserService.getUserById`, `TaskService.deleteTask`, `TaskListService.updateTaskList`, `TaskListTemplateService.updateTemplate`
- ❌ **Exception paths tested** — All services
- ❌ **`@Transactional` rollback behavior tested where writes are involved** — `UserService.register`, `UserService.verifyEmail`, `UserService.resetPassword`, `TaskService.updateTask`, `TemplateGeneratorService.generateFromTemplate`, `TaskListTemplateService.createTemplate`, `TaskListTemplateService.updateTemplate`

### Repositories / Mappers
- ❌ **SQL queries validated against an in-memory or test-container DB** — All 8 repository classes
- ❌ **Filter/WHERE clause conditions produce correct results** — Ownership queries in `TaskRepository.findByIdAndUserId`, `TaskListRepository.findByIdAndUserId`, `TaskListTemplateRepository.findDueTemplates`
- ❌ **Empty result set handled correctly** — All repositories

### Coverage Quality
- ⚠️ **`TaskFlowApplicationTests`** — Only asserts `contextLoads()`. Violates: *"No test that only asserts `toBeTruthy()` on the component/class instance"*

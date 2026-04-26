package com.killeen.taskflow.components.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.killeen.taskflow.components.user.service.UserService;
import com.killeen.taskflow.config.JwtService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private EmailTokenService emailTokenService;
    @Mock
    private EmailService emailService;
    @Mock
    private Environment environment;

    @InjectMocks
    private UserService userService;

    // -------------------------------------------------------------------------
    // register
    // -------------------------------------------------------------------------

    @Test
    void register_newEmail_savesUserAndSendsVerificationEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashed");
        // This "thenAnswer" method is interesting!
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
    void register_duplicateEmail_throwsUserAlreadyExistsException() {
        when(userRepository.findByEmail(anyString()))
                    .thenReturn(Optional.of(User.builder().build()));
        when(environment.getProperty(anyString())).thenReturn("User already exists");
        
        assertThatThrownBy(() -> userService.register("dup@example.com", "Pass1!", "Bob"))
            .isInstanceOf(UserAlreadyExistsException.class)
            .hasMessage("User already exists");
        verify(userRepository, never()).save(any());
    }

    // -------------------------------------------------------------------------
    // authenticate
    // -------------------------------------------------------------------------

    @Test
    void authenticate_validCredentials_returnsLoginResponse() {
        User user = User.builder()
                .id(1L)
                .passwordHash("hashed").emailVerified(true).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("Pass1!", "hashed")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");
        when(jwtService.getExpirationMs()).thenReturn(3_600_000L);

        LoginResponse response = userService.authenticate("user@example.com", "Pass1!");

        assertThat(response.getToken()).isEqualTo("jwt-token");
    }

    @Test
    void authenticate_wrongPassword_throwsInvalidCredentialsException() {
        User user = User.builder()
                .id(1L).passwordHash("hashed").emailVerified(true).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(environment.getProperty(anyString())).thenReturn("Incorrect username or password");

        assertThatThrownBy(() -> userService.authenticate("u@e.com", "wrong"))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Incorrect username or password");
    }

    @Test
    @DisplayName("authenticate_emailNotVerified_throwsEmailNotVerifiedException")
    void authenticate_emailNotVerified_throwsEmailNotVerifiedException() {
        User user = User.builder()
                .id(1L).passwordHash("hashed").emailVerified(false).build();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(environment.getProperty(anyString())).thenReturn("Email not verified");

        assertThatThrownBy(() -> userService.authenticate("u@e.com", "Pass1!"))
                .isInstanceOf(EmailNotVerifiedException.class)
                .hasMessage("Email not verified");
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
        when(environment.getProperty(anyString())).thenReturn("User not found");

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }
    

}

package se.sofiekl.userorderservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import se.sofiekl.userorderservice.DTO.LoginRequest;
import se.sofiekl.userorderservice.DTO.RegisterRequest;
import se.sofiekl.userorderservice.DTO.AuthResponse;
import se.sofiekl.userorderservice.model.User;
import se.sofiekl.userorderservice.repository.UserRepository;
import se.sofiekl.userorderservice.security.JwtUtil;
import se.sofiekl.userorderservice.service.AuthService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("sofia");
        request.setEmail("sofia@example.com");
        request.setPassword("secret123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(request.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        String result = authService.register(request);

        assertEquals("User registered successfully", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throwsException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("sofia");
        request.setEmail("sofia@example.com");
        request.setPassword("secret123");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.register(request));

        assertEquals("Email already in use", ex.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_success() {
        LoginRequest request = new LoginRequest();
        request.setEmail("sofia@example.com");
        request.setPassword("secret123");

        User user = User.builder()
                .email("sofia@example.com")
                .username("sofia")
                .password("hashed")
                .role("ROLE_USER")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken(user.getEmail(), user.getRole())).thenReturn("mock.jwt.token");

        AuthResponse response = authService.login(request);

        assertEquals("mock.jwt.token", response.token());
        assertEquals("Bearer", response.type());
        assertEquals("sofia", response.username());
    }

    @Test
    void login_wrongPassword_throwsException() {
        LoginRequest request = new LoginRequest();
        request.setEmail("sofia@example.com");
        request.setPassword("wrongpassword");

        User user = User.builder()
                .email("sofia@example.com")
                .password("hashed")
                .build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.login(request));

        assertEquals("Invalid email or password", ex.getMessage());
    }
}
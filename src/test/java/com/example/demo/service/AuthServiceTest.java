package com.example.demo.service;

import com.example.demo.Util.JwtUtil;
import com.example.demo.dto.AuthRequest;
import com.example.demo.dto.AuthResponse;
import com.example.demo.models.Roles;
import com.example.demo.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void authenticateUser_Success() {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        user.setRoles(Set.of(Roles.USER));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("testuser");

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(userService.findByUsername("testuser")).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("jwt-token");

        ResponseEntity<?> response = authService.authenticateUser(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().get("Set-Cookie")).isNotNull();
        assertThat(response.getBody()).isInstanceOf(AuthResponse.class);

        AuthResponse body = (AuthResponse) response.getBody();
        if (body != null) {
            assertThat(body.getUsername()).isEqualTo("testuser");
        }
        if (body != null) {
            assertThat(body.getJwtToken()).isEqualTo("Successfully logged in");
        }

        // Verify interactions
        verify(authenticationManager).authenticate(any());
        verify(jwtUtil).generateToken(user);
        verify(userService).findByUsername("testuser");
    }

    @Test
    void authenticateUser_Failure_BadCredentials() {
        AuthRequest request = new AuthRequest();
        request.setUsername("wronguser");
        request.setPassword("wrongpass");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        ResponseEntity<?> response = authService.authenticateUser(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isEqualTo("Login failed!");

        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(jwtUtil);
        verifyNoInteractions(userService);
    }

    @Test
    void checkAuthentication_Success() {
        User user = new User();
        user.setUsername("authenticatedUser");

        // Use a real SecurityContext
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken("authenticatedUser", null, List.of());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        when(userService.findByUsername("authenticatedUser")).thenReturn(user);

        User result = authService.checkAuthentication();

        assertThat(result).isEqualTo(user);
    }

    @Test
    void checkAuthentication_NotAuthenticated_ThrowsException() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(false);
        when(securityContext.getAuthentication()).thenReturn(auth);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.checkAuthentication());
        assertThat(ex.getMessage()).isEqualTo("User not logged in");
    }

    @Test
    void checkAuthentication_UserNotFound_ThrowsException() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("unknownUser");
        when(securityContext.getAuthentication()).thenReturn(auth);

        when(userService.findByUsername("unknownUser")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> authService.checkAuthentication());
        assertThat(ex.getMessage()).isEqualTo("User not found");
    }
}
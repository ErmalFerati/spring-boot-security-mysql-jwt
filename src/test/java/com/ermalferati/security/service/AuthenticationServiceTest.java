package com.ermalferati.security.service;

import com.ermalferati.security.jwt.JwtTokenProvider;
import com.ermalferati.security.model.UserPrincipal;
import com.ermalferati.security.payload.JwtAuthenticationResponse;
import com.ermalferati.security.payload.LoginRequest;
import com.ermalferati.security.payload.UserTransport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.ermalferati.security.AuthenticationTestUtil.authenticateUser;
import static com.ermalferati.security.AuthenticationTestUtil.buildLoginRequest;
import static com.ermalferati.security.AuthenticationTestUtil.buildUserPrincipal;
import static com.ermalferati.security.AuthenticationTestUtil.validateUser;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class AuthenticationServiceTest {

    private AuthenticationService authenticationService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Before
    public void setup() {
        authenticationService = new AuthenticationService(tokenProvider, authenticationManager);
    }

    @Test(expected = NullPointerException.class)
    public void getCurrentUser_IsNotAuthenticated_ThrowsNullPointerException() {
        authenticationService.getCurrentUser();
    }

    @Test
    public void getCurrentUser_IsAuthenticated_ReturnsCurrentUser() {
        UserPrincipal userPrincipal = buildUserPrincipal();
        authenticateUser(userPrincipal);

        UserTransport userTransport = authenticationService.getCurrentUser();
        validateUser(userTransport, userPrincipal);
    }

    @Test
    public void authenticate_ValidAuthentication_ReturnsJWTToken() {
        LoginRequest loginRequest = buildLoginRequest();
        JwtAuthenticationResponse expectedJwtAuthenticationResponse = buildJwtAuthenticationResponse();

        mockAuthenticationGeneratedToken(expectedJwtAuthenticationResponse.getAccessToken());

        JwtAuthenticationResponse returnedJwtAuthenticationResponse = authenticationService.authenticate(loginRequest);
        validateReturnedAuthenticationResponse(returnedJwtAuthenticationResponse, expectedJwtAuthenticationResponse);
    }

    @Test(expected = AuthenticationException.class)
    public void authenticate_InvalidAuthentication_ThrowsAuthenticationException() {
        LoginRequest loginRequest = buildLoginRequest();

        mockAuthenticationFailure();

        authenticationService.authenticate(loginRequest);
    }

    private JwtAuthenticationResponse buildJwtAuthenticationResponse() {
        JwtAuthenticationResponse jwtAuthenticationResponse = new JwtAuthenticationResponse("Token");
        jwtAuthenticationResponse.setTokenType("Bearer");
        return jwtAuthenticationResponse;
    }

    private void mockAuthenticationGeneratedToken(String token) {
        when(tokenProvider.generateToken(any())).thenReturn(token);
    }

    private void mockAuthenticationFailure() {
        when(authenticationManager.authenticate(any())).thenThrow(new AuthenticationException("") {
        });
    }

    private void validateReturnedAuthenticationResponse(JwtAuthenticationResponse returnedJwtAuthenticationResponse,
                                                        JwtAuthenticationResponse expectedJwtAuthenticationResponse) {
        assertEquals(returnedJwtAuthenticationResponse.getTokenType(), expectedJwtAuthenticationResponse.getTokenType());
        assertEquals(returnedJwtAuthenticationResponse.getAccessToken(), expectedJwtAuthenticationResponse.getAccessToken());
    }
}

package com.ermalferati.security.service;

import com.ermalferati.security.jwt.JwtTokenProvider;
import com.ermalferati.security.model.UserPrincipal;
import com.ermalferati.security.payload.JwtAuthenticationResponse;
import com.ermalferati.security.payload.LoginRequest;
import com.ermalferati.security.payload.UserTransport;
import com.ermalferati.security.util.UserMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(JwtTokenProvider tokenProvider,
                                 AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public UserTransport getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return UserMapper.toTransport(userPrincipal);
    }

    public JwtAuthenticationResponse authenticate(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return new JwtAuthenticationResponse(jwt);
    }
}

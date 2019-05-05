package com.ermalferati.security.controller;

import com.ermalferati.security.payload.JwtAuthenticationResponse;
import com.ermalferati.security.payload.LoginRequest;
import com.ermalferati.security.payload.UserTransport;
import com.ermalferati.security.service.AuthenticationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public UserTransport getCurrentUser() {
        return authenticationService.getCurrentUser();
    }

    @PostMapping
    @ResponseBody
    public JwtAuthenticationResponse authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.authenticate(loginRequest);
    }
}

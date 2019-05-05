package com.ermalferati.security.controller;

import com.ermalferati.security.payload.CreateUserRequest;
import com.ermalferati.security.payload.CreateUserWithRolesRequest;
import com.ermalferati.security.payload.UserTransport;
import com.ermalferati.security.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserTransport create(@Valid @RequestBody CreateUserRequest createUserRequest) {
        return userService.create(createUserRequest);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserTransport createWithRoles(@Valid @RequestBody CreateUserWithRolesRequest createUserRequest) {
        return userService.createWithRoles(createUserRequest);
    }
}

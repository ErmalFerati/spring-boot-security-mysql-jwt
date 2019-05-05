package com.ermalferati.security.util;

import com.ermalferati.security.model.UserPrincipal;
import com.ermalferati.security.model.User;
import com.ermalferati.security.payload.CreateUserRequest;
import com.ermalferati.security.payload.CreateUserWithRolesRequest;
import com.ermalferati.security.payload.UserTransport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(CreateUserRequest createUserRequest) {
        User user = new User();
        user.setName(createUserRequest.getName());
        user.setUsername(createUserRequest.getUsername());
        user.setEmail(createUserRequest.getEmail());
        user.setPassword(createUserRequest.getPassword());
        return user;
    }

    public static User toEntity(CreateUserWithRolesRequest createUserRequest) {
        User user = toEntity((CreateUserRequest) createUserRequest);
        user.setRoles(createUserRequest.getRoles());
        return user;
    }

    public static UserTransport toTransport(User user) {
        UserTransport userTransport = new UserTransport();
        userTransport.setId(user.getId());
        userTransport.setUsername(user.getUsername());
        userTransport.setName(user.getName());
        userTransport.setEmail(user.getEmail());

        Set<String> userRoles = getUserRoles(user);
        userTransport.setRoles(userRoles);

        return userTransport;
    }

    public static UserTransport toTransport(UserPrincipal userPrincipal) {
        UserTransport userTransport = new UserTransport();
        userTransport.setId(userPrincipal.getId());
        userTransport.setUsername(userPrincipal.getUsername());
        userTransport.setName(userPrincipal.getName());
        userTransport.setEmail(userPrincipal.getEmail());

        Set<String> userRoles = getUserRoles(userPrincipal);
        userTransport.setRoles(userRoles);

        return userTransport;
    }

    public static UserPrincipal toPrincipal(User user) {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(user.getId());
        userPrincipal.setName(user.getName());
        userPrincipal.setUsername(user.getUsername());
        userPrincipal.setEmail(user.getEmail());
        userPrincipal.setPassword(user.getPassword());

        Set<GrantedAuthority> authorities = getAuthoritiesFromUser(user);
        userPrincipal.setAuthorities(authorities);

        return userPrincipal;
    }

    private static Set<GrantedAuthority> getAuthoritiesFromUser(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toSet());
    }

    private static Set<String> getUserRoles(UserPrincipal userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

    private static Set<String> getUserRoles(User user) {
        return user.getRoles().stream()
                .map(role -> role.getName().name())
                .collect(Collectors.toSet());
    }

}

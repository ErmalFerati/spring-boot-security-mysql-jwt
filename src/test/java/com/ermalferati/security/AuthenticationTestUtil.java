package com.ermalferati.security;

import com.ermalferati.security.model.UserPrincipal;
import com.ermalferati.security.payload.LoginRequest;
import com.ermalferati.security.payload.UserTransport;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;

public final class AuthenticationTestUtil {

    private AuthenticationTestUtil() {

    }

    public static void validateUser(UserTransport userTransport, UserPrincipal userPrincipal) {
        assertEquals(userTransport.getId(), userPrincipal.getId());
        assertEquals(userTransport.getName(), userPrincipal.getName());
        assertEquals(userTransport.getUsername(), userPrincipal.getUsername());
        assertEquals(userTransport.getEmail(), userPrincipal.getEmail());
        assertEquals(userTransport.getRoles().size(), userPrincipal.getAuthorities().size());

        validateUserRoles(userTransport, userPrincipal);
    }

    public static void validateUserRoles(UserTransport userTransport, UserPrincipal userPrincipal) {
        Iterator<String> userRolesIterator = userTransport.getRoles().iterator();
        Iterator<GrantedAuthority> userAuthoritiesIterator =
                (Iterator<GrantedAuthority>) userPrincipal.getAuthorities().iterator();

        while (userAuthoritiesIterator.hasNext()) {
            assertEquals(userRolesIterator.next(), userAuthoritiesIterator.next().getAuthority());
        }
    }

    public static UserPrincipal buildUserPrincipal() {
        UserPrincipal userPrincipal = new UserPrincipal();
        userPrincipal.setId(1234L);
        userPrincipal.setName("Ermal Ferati");
        userPrincipal.setUsername("Ermal");
        userPrincipal.setPassword("Ferati");
        userPrincipal.setEmail("ermalferati@gmail.com");

        List<GrantedAuthority> authorities = builtUserAuthorities();
        userPrincipal.setAuthorities(authorities);

        return userPrincipal;
    }

    public static void authenticateUser(UserPrincipal userPrincipal) {
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public static LoginRequest buildLoginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("Ermal");
        loginRequest.setPassword("Ferati");
        return loginRequest;
    }

    private static List<GrantedAuthority> builtUserAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}

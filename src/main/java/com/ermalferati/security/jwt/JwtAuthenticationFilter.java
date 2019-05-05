package com.ermalferati.security.jwt;

import com.ermalferati.security.service.CustomUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER_VALUE_PREFIX = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider,
                                   CustomUserDetailsService customUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        setRequestAuthentication(request);
        filterChain.doFilter(request, response);
    }

    private void setRequestAuthentication(HttpServletRequest request) {
        try {
            String jwtToken = getJwtTokenFromRequest(request);

            if (isJwtTokenValid(jwtToken)) {
                Long userId = tokenProvider.getUserIdFromJWT(jwtToken);
                setRequestSecurityContextHolder(userId, request);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String headerValue = request.getHeader("Authorization");
        if (isAuthorizationHeaderValueValid(headerValue)) {
            return headerValue.substring(7);
        }

        return null;
    }

    private boolean isAuthorizationHeaderValueValid(String headerValue) {
        return StringUtils.hasText(headerValue) && headerValue.startsWith(AUTHORIZATION_HEADER_VALUE_PREFIX);
    }

    private boolean isJwtTokenValid(String jwtToken) {
        return StringUtils.hasText(jwtToken) && tokenProvider.isTokenValid(jwtToken);
    }

    private void setRequestSecurityContextHolder(long userId, HttpServletRequest request) {
        Authentication authentication = getAuthenticationForUser(userId, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication getAuthenticationForUser(long userId, HttpServletRequest request) {
        UserDetails userDetails = customUserDetailsService.loadUserById(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        return authentication;
    }
}

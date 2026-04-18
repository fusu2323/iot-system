package com.example.iot.security;

import com.example.iot.entity.User;
import com.example.iot.mapper.UserMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * SecurityContext utility for extracting current user information.
 */
@Component
public class SecurityContextUtil {

    private final UserMapper userMapper;

    public SecurityContextUtil(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Get current authenticated user's ID from SecurityContext.
     * Uses DB lookup by username since UserDetails principal does not carry userId.
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found in SecurityContext");
        }

        Object principal = authentication.getPrincipal();
        String username;

        if (principal instanceof UserDetails userDetails) {
            username = userDetails.getUsername();
        } else if (principal instanceof String s) {
            username = s;
        } else {
            throw new IllegalStateException("Unexpected principal type: " + principal.getClass());
        }

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            throw new IllegalStateException("User not found: " + username);
        }
        return user.getId();
    }

    /**
     * Get current user's role from SecurityContext authorities.
     * Strips "ROLE_" prefix to return raw role name (ADMIN or USER).
     */
    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new IllegalStateException("No authenticated user found in SecurityContext");
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .filter(auth -> auth.startsWith("ROLE_"))
            .map(auth -> auth.substring(5))  // Strip "ROLE_" prefix
            .findFirst()
            .orElse("USER");
    }
}

package com.boersenparty.v_1_1.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class TokenUtils {

    // Gets the KeyCloak UserID ("preferred username") from the Token
    public static String getUserID() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            String name = jwt.getClaim("preferred_username");

            if (name != null) {
                return name;
            }
        }
        System.out.println("ERROR: claim (PREFFERED USERNAME) unaccesible, returning getName() instead");
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public static boolean hasAuthority(String authority) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
    }

    // Retrieve the "works_for" attribute from the token (Personal user associated with Veranstalter)
    public static String getWorksForFromToken() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            return jwt.getClaim("works_for");
        }

        return null;
    }
}


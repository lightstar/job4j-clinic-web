package ru.lightstar.clinic.security;

import org.apache.commons.codec.binary.Hex;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

/**
 * Utility methods related to spring security.
 *
 * @author LightStar
 * @since 0.0.1
 */
public class SecurityUtil {

    /**
     * Generate hashed password using SHA-256 algorithm.
     *
     * @param password open password.
     * @return hashed password.
     */
    public static String getHashedPassword(final String password) {
        if (password.isEmpty()) {
            return password;
        }

        try {
            return Hex.encodeHexString(MessageDigest.getInstance("SHA-256").digest(password.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get current user's authenticated name.
     *
     * @return user's authenticated name.
     */
    public static String getAuthName() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "";
        }
        return authentication.getName();
    }

    /**
     * Get current user's authenticated role.
     *
     * @return user's authenticated role.
     */
    public static String getAuthRole() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return "";
        }

        final Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (authorities.size() == 0) {
            return "";
        }

        final String authority = authorities.iterator().next().getAuthority();
        if (authority == null) {
            return "";
        }

        return authority;
    }
}

package ru.lightstar.clinic.security;

import org.apache.commons.codec.binary.Hex;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
}

package org.codecritique.thrifty.validators;

import org.codecritique.thrifty.exception.InvalidPasswordException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Passwords {
    private final static PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    public static String validateAndEncode(String password) {
        if (password == null || password.length() < 6 || password.length() > 80 || containsWhiteSpaceCharacters(password))
            throw new InvalidPasswordException();
        return encoder.encode(password);
    }

    private static boolean containsWhiteSpaceCharacters(String s) {
        return s != null && s.matches(".*\\s+.*");
    }
}

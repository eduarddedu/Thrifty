package org.codecritique.thrifty.validators;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


public class Passwords {
    private final static PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private static boolean isValid(String password) {
        return password != null &&
                password.length() >= 6 &&
                password.length() <= 80 &&
                !containsWhiteSpaceCharacters(password);
    }

    private static boolean containsWhiteSpaceCharacters(String s) {
        return s != null && s.matches(".*\\s+.*");
    }

    public static String validateAndEncode(String password) throws PasswordValidationException {
        if (isValid(password))
            return encoder.encode(password);
        throw new PasswordValidationException();
    }
}

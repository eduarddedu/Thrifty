package org.codecritique.thrifty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Locale;

@Service
public class MessageService {
    @Autowired
    private MessageSource messageSource;
    private String invalidPasswordMessage;
    private String registrationSuccessfulMessage;
    private String usernameExistsMessage;

    @PostConstruct
    void init() {
        invalidPasswordMessage = getMessage("invalid.password");
        registrationSuccessfulMessage = getMessage("registration.successful");
        usernameExistsMessage = getMessage("username.exists");
    }

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.ENGLISH);
    }

    public String invalidPassword() {
        return invalidPasswordMessage;
    }

    public String registrationSuccessful() {
        return registrationSuccessfulMessage;
    }

    public String usernameExists() {
        return usernameExistsMessage;
    }
}

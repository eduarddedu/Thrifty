package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.service.MessageService;
import org.codecritique.thrifty.service.UserService;
import org.codecritique.thrifty.validators.EmailValidationException;
import org.codecritique.thrifty.validators.PasswordValidationException;
import org.codecritique.thrifty.validators.UsernameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController extends BaseController {
    @Autowired
    private UserService users;

    @Autowired
    private MessageService messages;
    
    private final String USERNAME_PLACEHOLDER = "name@example.com";

    @PostMapping(path = "register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam String currency,
                             @RequestParam long accountId,
                             Model m) {
        try {
            if (accountId == 0)
                users.createUserAndAccount(username, password, currency);
            else
                users.createUserForAccount(username, password, accountId);
            m.addAttribute("hasCompletedRegistration", true);
            return "registerForm";
        } catch (PasswordValidationException passwordException) {
            m.addAttribute("usernamePlaceholder", username);
            m.addAttribute("passwordValidationError", messages.invalidPassword());
            m.addAttribute("accountId", 0);
            return "registerForm";
        } catch (EmailValidationException e) {
            m.addAttribute("usernamePlaceholder", username);
            m.addAttribute("usernameValidationError", e.getMessage());
            m.addAttribute("accountId", 0);
            return "registerForm";
        } catch (UsernameExistsException e) {
            m.addAttribute("usernamePlaceholder", USERNAME_PLACEHOLDER);
            m.addAttribute("usernameValidationError", messages.usernameExists());
            m.addAttribute("accountId", 0);
            return "registerForm";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @GetMapping(path = "register")
    public String getRegisterForm(Model m) {
        m.addAttribute("hasCompletedRegistration", false);
        m.addAttribute("usernamePlaceholder", USERNAME_PLACEHOLDER);
        m.addAttribute("accountId", 0);
        return "registerForm";
    }
}

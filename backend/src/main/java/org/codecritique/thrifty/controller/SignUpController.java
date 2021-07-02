package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.validators.PasswordValidationException;
import org.codecritique.thrifty.service.MessageService;
import org.codecritique.thrifty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
            m.addAttribute("emailPlaceholder", username);
            m.addAttribute("passwordValidationError", messages.invalidPassword());
            m.addAttribute("accountId", 0);
            return "registerForm";
        } catch (javax.validation.ConstraintViolationException e) {
            m.addAttribute("emailPlaceholder", username);
            m.addAttribute("emailValidationError", e.getConstraintViolations().iterator().next().getMessage());
            m.addAttribute("accountId", 0);
            return "registerForm";
        } catch (DataIntegrityViolationException ex) {
            m.addAttribute("emailPlaceholder", username);
            m.addAttribute("emailValidationError", messages.emailExists());
            m.addAttribute("accountId", 0);
            return "registerForm";
        }
    }

    @GetMapping(path = "register")
    public String getRegisterForm(Model m) {
        m.addAttribute("hasCompletedRegistration", false);
        m.addAttribute("emailPlaceholder", "name@example.com");
        m.addAttribute("accountId", 0);
        return "registerForm";
    }
}

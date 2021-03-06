package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.AccountDao;
import org.codecritique.thrifty.dao.UserDao;
import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.User;
import org.codecritique.thrifty.exception.InvalidPasswordException;
import org.codecritique.thrifty.validators.Passwords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
    @Autowired
    private UserDao userDao;
    @Autowired
    private AccountDao accountDao;

    @PostMapping(path = "register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createUser(@RequestParam String username,
                             @RequestParam String password,
                             @RequestParam long accountId,
                             Model m) {
        try {
            User user = new User(username, Passwords.validateAndEncode(password), accountId);
            if (user.getAccountId() == 0) {
                user.setAccountId(accountDao.save(new Account()).getId());
            }
            userDao.save(user);
            m.addAttribute("hasCompletedRegistration", true);
            return "registerForm";
        } catch (InvalidPasswordException passwordException) {
            m.addAttribute("emailPlaceholder", username);
            m.addAttribute("passwordValidationError", passwordException.getMessage());
            m.addAttribute("accountId", 0);
            return "registerForm";
        } catch (javax.validation.ConstraintViolationException e) {
            m.addAttribute("emailPlaceholder", username);
            m.addAttribute("emailValidationError", e.getConstraintViolations().iterator().next().getMessage());
            m.addAttribute("accountId", 0);
            return "registerForm";
        } catch (DataIntegrityViolationException ex) {
            m.addAttribute("emailPlaceholder", username);
            m.addAttribute("emailValidationError", "A user with this email already exists");
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

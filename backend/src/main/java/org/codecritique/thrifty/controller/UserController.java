package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.UserDao;
import org.codecritique.thrifty.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    private UserDao userDao;

    @GetMapping(path = "rest-api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        org.springframework.security.core.userdetails.User principal =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        org.codecritique.thrifty.entity.User user =
                userDao.findByUsernameIgnoreCase(principal.getUsername());
        return ResponseEntity.ok(user);
    }

}



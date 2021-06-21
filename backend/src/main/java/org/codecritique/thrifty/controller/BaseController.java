package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

public abstract class BaseController {
    @Autowired
    private UserDao userDao;

    protected URI toAbsoluteURI(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(path)
                .build(new HashMap<>());
    }

    protected org.codecritique.thrifty.entity.User findUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        User principal = (User) authentication.getPrincipal();
        return userDao.findByUsernameIgnoreCase(principal.getUsername());
    }
}

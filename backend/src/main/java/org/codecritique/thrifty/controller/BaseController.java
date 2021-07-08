package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.BaseEntity;
import org.codecritique.thrifty.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

public abstract class BaseController {

    protected URI toAbsoluteURI(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(path)
                .build(new HashMap<>());
    }

    protected boolean isAuthorizedToAccess(User user, BaseEntity entity) {
        if (user.getAccountId() == null || entity.getAccountId() == null)
            return false;
        return user.getAccountId().equals(entity.getAccountId());
    }
}

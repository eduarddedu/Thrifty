package org.codecritique.thrifty.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

public abstract class BaseController {

    protected URI toAbsoluteURI(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(path)
                .build(new HashMap<>());
    }
}

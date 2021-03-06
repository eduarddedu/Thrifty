package org.codecritique.thrifty.controller;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public abstract class BaseController {

    protected URI toAbsoluteUri(String path) {
        String apiBaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().toUriString();
        return URI.create(apiBaseUrl + path);
    }
}

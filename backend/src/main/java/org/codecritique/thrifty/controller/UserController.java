package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController extends BaseController {

    @GetMapping(path = "rest-api/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUser() {
        return ResponseEntity.ok(findUser());
    }

}



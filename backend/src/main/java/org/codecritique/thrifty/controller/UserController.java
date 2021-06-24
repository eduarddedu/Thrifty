package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.service.UserService;
import org.codecritique.thrifty.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController extends BaseController {
    @Autowired
    private UserService users;

    @GetMapping(path = "rest-api/self")
    public ResponseEntity<User> getLoggedInUser(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}



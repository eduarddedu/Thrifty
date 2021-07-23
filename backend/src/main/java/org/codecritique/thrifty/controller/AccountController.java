package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "rest-api/account")
public class AccountController extends BaseController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@AuthenticationPrincipal User user) {
        Account account = repository.findById(Account.class, user.getAccountId());
        return ResponseEntity.ok(account);
    }

    @DeleteMapping
    public ResponseEntity<Resource> deleteAccountData(@AuthenticationPrincipal User user) {
        repository.removeAccountData(user.getAccountId());
        return ResponseEntity.ok().build();
    }
}

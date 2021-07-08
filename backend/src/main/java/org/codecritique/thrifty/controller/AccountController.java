package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.AccountDao;
import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(path = "rest-api/account")
public class AccountController extends BaseController {
    @Autowired
    private AccountDao accountDao;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@AuthenticationPrincipal User user) {
        Account account = accountDao.findById(user.getAccountId());
        if (account == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(account);
    }
}

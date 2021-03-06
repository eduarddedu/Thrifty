package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.AccountDao;
import org.codecritique.thrifty.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    private AccountDao accountDao;

    @GetMapping(path = "/rest-api/account/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getAccount(@PathVariable long id) {
        Account account = accountDao.findById(id);
        if (account == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(account);
    }
}

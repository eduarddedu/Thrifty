package org.codecritique.thrifty.service;

import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.User;
import org.codecritique.thrifty.dao.AccountDao;
import org.codecritique.thrifty.dao.UserDao;
import org.codecritique.thrifty.validators.PasswordValidationException;
import org.codecritique.thrifty.validators.Passwords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AccountDao accountDao;

    private void internalCreateUser(String username, String encodedPassword, long accountId) {
        User user = new User(username, encodedPassword, accountId);
        userDao.save(user);
    }

    private Account internalCreateAccount(String accountCurrency) {
        Account account = new Account();
        account.setCurrency(accountCurrency);
        accountDao.save(account);
        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUsernameIgnoreCase(s);
        if (user == null)
            throw new UsernameNotFoundException(s);
        return user;
    }

    public void createUserForAccount(String username, String password, long accountId) throws PasswordValidationException {
        internalCreateUser(username, Passwords.validateAndEncode(password), accountId);
    }

    public void createUserAndAccount(String username, String password, String accountCurrency) throws PasswordValidationException {
        String encodedPassword = Passwords.validateAndEncode(password);
        Account account = internalCreateAccount(accountCurrency);
        internalCreateUser(username, encodedPassword, account.getId());
    }
}

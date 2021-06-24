package org.codecritique.thrifty.service;

import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.User;
import org.codecritique.thrifty.dao.AccountDao;
import org.codecritique.thrifty.dao.UserDao;
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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userDao.findByUsernameIgnoreCase(s);
        if (user == null)
            throw new UsernameNotFoundException(s);
        return user;
    }

    public void createUser(String username, String password, long accountId) {
        User user = new User(username, Passwords.validateAndEncode(password), accountId);
        if (user.getAccountId() == 0) {
            user.setAccountId(accountDao.save(new Account()).getId());
        }
        userDao.save(user);
    }
}

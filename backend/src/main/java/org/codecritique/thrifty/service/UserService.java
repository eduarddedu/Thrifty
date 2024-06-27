package org.codecritique.thrifty.service;

import org.codecritique.thrifty.repository.Repository;
import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.User;
import org.codecritique.thrifty.validators.EmailValidationException;
import org.codecritique.thrifty.validators.PasswordValidationException;
import org.codecritique.thrifty.validators.Passwords;
import org.codecritique.thrifty.validators.UsernameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private Repository repository;

    private void internalCreateUser(String username, String encodedPassword, long accountId) throws UsernameExistsException, EmailValidationException {
        try {
            User user = new User(username, encodedPassword, accountId);
            repository.save(user);
        }
        catch (DataIntegrityViolationException e) {
            if (e.getCause() instanceof org.hibernate.exception.ConstraintViolationException)
            throw new UsernameExistsException();
        }
        catch (javax.validation.ConstraintViolationException e) {
            String error = e.getConstraintViolations().iterator().next().getMessage();
            throw new EmailValidationException(error);
        }
    }

    private Account internalCreateAccount(String accountCurrency) {
        Account account = new Account();
        account.setCurrency(accountCurrency);
        repository.save(account);
        return account;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repository.findByUsernameIgnoreCase(s);
        if (user == null)
            throw new UsernameNotFoundException(s);
        return user;
    }

    public void createUserForAccount(String username, String password, long accountId) throws PasswordValidationException, UsernameExistsException, EmailValidationException {
        internalCreateUser(username, Passwords.validateAndEncode(password), accountId);
    }

    public void createUserAndAccount(String username, String password, String accountCurrency) throws PasswordValidationException, UsernameExistsException, EmailValidationException {
        String encodedPassword = Passwords.validateAndEncode(password);
        Account account = internalCreateAccount(accountCurrency);
        internalCreateUser(username, encodedPassword, account.getId());
    }
}

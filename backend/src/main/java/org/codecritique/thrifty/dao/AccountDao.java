package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Account;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Service;

@Service
public interface AccountDao extends Repository<Account, Long> {

    Account save(Account account);
    Account findById(long id);
}

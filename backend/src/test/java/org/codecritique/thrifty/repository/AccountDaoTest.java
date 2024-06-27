package org.codecritique.thrifty.repository;

import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountDaoTest extends BaseDaoTest {
    @Test
    void shouldGetAccount() {
        Account account = repository.findById(Account.class, ACCOUNT_ID);
        assertNotNull(account);
        assertEquals("Daily expenses", account.getName());
        Set<Category> categories = account.getCategories();
        assertNotNull(categories);
        assertEquals(2, categories.stream().map(Category::getName).filter(name -> name.matches("Groceries|Rent")).count());
    }

    @Test
    void shouldDeleteAccountData() {
        long accountId = 3;
        Account account = repository.findById(Account.class, accountId);
        assertNotNull(account);
        repository.removeAccountData(accountId);
        account = repository.findById(Account.class, accountId);
        assertTrue(account.getCategories().isEmpty());
        assertTrue(account.getLabels().isEmpty());
    }
}

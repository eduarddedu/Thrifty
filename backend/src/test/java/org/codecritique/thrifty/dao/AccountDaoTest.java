package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AccountDaoTest extends BaseDaoTest {
    @Autowired
    private AccountDao service;

    @Test
    void shouldGetAccount() {
        Account account  = service.findById(1);
        assertNotNull(account);
        assertEquals("Daily expenses", account.getName());
        assertEquals("English", account.getLanguage());
        Set<Category> categories = account.getCategories();
        assertNotNull(categories);
        assertEquals(2, categories.stream().map(Category::getName).filter(s -> s.matches("Groceries|Rent")).count());
        Set<Expense> expenses = account.getExpenses();
        assertNotNull(expenses);
    }
}

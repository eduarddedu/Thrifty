package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.ExpenseView;
import org.junit.jupiter.api.Test;

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
        assertEquals(2, categories.stream().map(Category::getName).filter(s -> s.matches("Groceries|Rent")).count());
        Set<ExpenseView> expenses = account.getExpenses();
        assertNotNull(expenses);
    }
}

package org.codecritique.thrifty.controller;


import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AccountControllerTest extends BaseControllerTest {

    @Test
    public void shouldGetTestAccount() throws Exception {
        Account account = (Account) getEntity(Resource.ACCOUNT, 1L);
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

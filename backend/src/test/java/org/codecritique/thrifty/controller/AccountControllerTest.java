package org.codecritique.thrifty.controller;


import org.codecritique.thrifty.entity.Account;
import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest extends BaseControllerTest {

    @Test
    public void shouldGetTestAccount() throws Exception {
        Account account = getEntity(Account.class, null);
        assertNotNull(account);
        assertEquals("Daily expenses", account.getName());
        Set<Category> categories = account.getCategories();
        assertNotNull(categories);
        assertEquals(2, categories.stream().map(Category::getName).filter(s -> s.matches("Groceries|Rent")).count());
    }

    @Test
    @WithUserDetails(value = "deleteme@example.com")
    public void shouldDeleteAccountData() throws Exception {
        String url = url(Account.class);
        mockMvc.perform(delete(url).with(csrf())).andExpect(status().isOk());
    }

}

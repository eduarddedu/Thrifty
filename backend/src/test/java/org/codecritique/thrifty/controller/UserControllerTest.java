package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest extends BaseControllerTest {

    @Test
    public void shouldFetchMockUser() throws Exception {
        User mockUser = (User) getEntity(Resource.USER, null);
        assertNotNull(mockUser);
        assertEquals("johndoe@example.com", mockUser.getUsername());
        assertNull(mockUser.getPassword());
    }
}

package org.codecritique.thrifty.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.junit.jupiter.api.Assertions.*;

public class SessionTimeoutControllerTest extends BaseControllerTest {

    @Test
    public void shouldGetSessionTimeoutDate() throws Exception {
        String timeout = mockMvc.perform(get("/session-timeout").with(csrf()))
                .andReturn().getResponse().getContentAsString();
        assertTrue(timeout.matches("\\d{13}"));
    }
}

package org.codecritique.thrifty.auth;

import org.codecritique.thrifty.MockMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class WebSecurityTest extends MockMvcTest {

    @Test
    public void shouldRedirectUnauthenticatedRequests() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    public void shouldLoginUser() throws Exception {
        mockMvc.perform(get("/"));
        String username = "johndoe@example.com";
        mockMvc.perform(post("/login")
                .param("username", username)
                .param("password", "password")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @WithMockUser
    public void shouldLogoutUser() throws Exception {
        mockMvc.perform(post("/logout").with(csrf()))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/login?logout"));
        shouldRedirectUnauthenticatedRequests();
    }
}

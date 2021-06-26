package org.codecritique.thrifty.e2e;

import org.codecritique.thrifty.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest extends BaseSecurityTest {

    @Test
    public void shouldReturnSelf() {
        login(mockUser);
        HttpEntity<String> entity = new HttpEntity<>(addXsrfHeader(new HttpHeaders()));
        ResponseEntity<User> selfResponse = customTemplate
                .exchange(baseUrl + "rest-api/self", HttpMethod.GET, entity, User.class);
        User user = selfResponse.getBody();
        assertNotNull(user);
        assertEquals(mockUser.getUsername(), user.getUsername());
        assertNull(user.getPassword());
        logout();
    }
}

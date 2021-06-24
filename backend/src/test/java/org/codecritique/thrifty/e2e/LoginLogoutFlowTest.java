package org.codecritique.thrifty.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginLogoutFlowTest extends BaseSecurityTest {

    @Test
    public void unauthenticatedRequestsShouldGetRedirectedToLoginUrl() {
        ResponseEntity<String> homepageResponse = customTemplate.getForEntity(baseUrl, String.class);
        assertThat(homepageResponse.getBody()).contains("<title>Sign in</title>");
    }

    @Test
    public void shouldLoginThenGetHomePageThenLogout() {
        login(mockUser);
        ResponseEntity<String> homePageResponse = customTemplate.getForEntity(baseUrl, String.class);
        assertThat(homePageResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(homePageResponse.getBody()).contains("<title>Thrifty</title>");
        logout();
    }

}

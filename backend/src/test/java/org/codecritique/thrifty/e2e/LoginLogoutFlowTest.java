package org.codecritique.thrifty.e2e;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginLogoutFlowTest extends BaseSecurityTest {

    @Test
    public void unauthenticatedRequestsShouldGetRedirectedToLoginUrl() {
        ResponseEntity<String> homepageResponse = customTemplate.getForEntity(webAppBaseUrl, String.class);
        assertThat(homepageResponse.getBody()).contains("<title>Sign in</title>");
    }

    @Test
    public void shouldLoginThenGetHomePageThenLogout() {
        login("johndoe@example.com", "password");
        ResponseEntity<String> homePageResponse = customTemplate.getForEntity(webAppBaseUrl, String.class);
        assertThat(homePageResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(homePageResponse.getBody()).contains("<title>Thrifty</title>");
        logout();
    }

}

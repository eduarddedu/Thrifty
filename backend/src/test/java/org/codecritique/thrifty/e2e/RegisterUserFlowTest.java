package org.codecritique.thrifty.e2e;

import org.codecritique.thrifty.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterUserFlowTest extends BaseSecurityTest {
    private final String registrationCompletedSuccessMessage = "Your account has been created";

    private ResponseEntity<String> register(User user) {
        String url = webAppBaseUrl + "register";
        ResponseEntity<String> registerPageResponse = customTemplate.getForEntity(url, String.class);
        assertThat(registerPageResponse.getBody()).contains("<title>Sign up</title>");

        HttpHeaders headers = new HttpHeaders();
        headers.add("X-XSRF-TOKEN", getXsrfToken());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", user.getUsername());
        map.add("password", user.getPassword());
        map.add("accountId", user.getAccountId().toString());

        HttpEntity<MultiValueMap<String, String>> signUpRequest = new HttpEntity<>(map, headers);
        return customTemplate.postForEntity(url, signUpRequest, String.class);
    }


    @Test
    public void shouldSignUpThenLoginThenGetHomePageThenLogout() {
        User jane = new User("jane@example.com", "secret", 0L);
        ResponseEntity<String> submitFormResponse = register(jane);
        assertThat(submitFormResponse.getBody()).contains(registrationCompletedSuccessMessage);

        login(jane.getUsername(), jane.getPassword());

        ResponseEntity<String> homePageResponse = customTemplate.getForEntity(webAppBaseUrl, String.class);
        assertThat(homePageResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(homePageResponse.getBody()).contains("<title>Thrifty</title>");

        logout();
    }

    @Test
    public void shouldFailRegistrationWhenInvalidEmail() {
        User wrong = new User("wrong_email_address", "password", 0L);
        ResponseEntity<String> submitFormResponse = register(wrong);
        assertThat(submitFormResponse.getBody()).contains("Invalid email");
    }

    @Test
    public void shouldFailRegistrationWhenInvalidPassword() {
        User wrong = new User("name@example.com", "pass", 0L);
        assertThat(register(wrong).getBody()).contains("Invalid password");
        wrong.setPassword("password with spaces");
        assertThat(register(wrong).getBody()).contains("Invalid password");

    }

    @Test
    public void shouldFailRegistrationWhenNotUniqueEmail() {
        String email = "bugs@bunny.com";
        User user = new User(email, "password", 0L);
        assertThat(register(user).getBody()).contains(registrationCompletedSuccessMessage);
        assertThat(register(user).getBody()).contains("A user with this email already exists");
    }
}

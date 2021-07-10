package org.codecritique.thrifty.e2e;

import org.codecritique.thrifty.entity.User;
import org.codecritique.thrifty.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

public class RegisterUserFlowTest extends BaseSecurityTest {
    @Autowired
    private MessageService messages;

    private ResponseEntity<String> register(User user) {
        String url = baseUrl + "register";
        ResponseEntity<String> registerPageResponse = customTemplate.getForEntity(url, String.class);
        assertThat(registerPageResponse.getBody()).contains("<title>Sign up</title>");

        HttpHeaders headers = addXsrfHeader(new HttpHeaders());

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", user.getUsername());
        map.add("password", user.getPassword());
        map.add("accountId", user.getAccountId().toString());
        map.add("currency", "EUR");

        HttpEntity<MultiValueMap<String, String>> signUpRequest = new HttpEntity<>(map, headers);
        return customTemplate.postForEntity(url, signUpRequest, String.class);
    }


    @Test
    public void shouldSignUpThenLoginThenGetHomePageThenLogout() {
        User jane = new User("jane@example.com", "secret", 0L);
        ResponseEntity<String> submitFormResponse = register(jane);
        assertThat(submitFormResponse.getBody()).contains(messages.registrationSuccessful());

        login(jane);

        ResponseEntity<String> homePageResponse = customTemplate.getForEntity(baseUrl, String.class);
        assertThat(homePageResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(homePageResponse.getBody()).contains("<title>Thrifty</title>");

        logout();
    }

    @Test
    public void shouldFailRegistrationWhenInvalidEmail() {
        User wrong = new User("wrong_email_address", "password", 0L);
        ResponseEntity<String> submitFormResponse = register(wrong);
        assertThat(submitFormResponse.getBody()).contains("Invalid email: &#39;wrong_email_address&#39;");
    }

    @Test
    public void shouldFailRegistrationWhenInvalidPassword() {
        User wrong = new User("name@example.com", "pass", 0L);
        String error = messages.invalidPassword();
        assertThat(register(wrong).getBody()).contains(error);
        wrong.setPassword("password with spaces");
        assertThat(register(wrong).getBody()).contains(error);

    }

    @Test
    public void shouldFailRegistrationWhenNotUniqueEmail() {
        String email = "bugs@bunny.com";
        User user = new User(email, "password", 0L);
        assertThat(register(user).getBody()).contains(messages.registrationSuccessful());
        assertThat(register(user).getBody()).contains(messages.usernameExists());
    }
}

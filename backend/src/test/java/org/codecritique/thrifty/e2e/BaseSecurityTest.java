package org.codecritique.thrifty.e2e;

import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.codecritique.thrifty.entity.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@TestPropertySource(locations = "classpath:/application-test.yml")
@SpringBootTest(classes = org.codecritique.thrifty.Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BaseSecurityTest {
    protected final String XSRF_HEADER_NAME = "X-XSRF-TOKEN";
    @Autowired
    protected Environment env;
    protected RestTemplate customTemplate;
    protected CookieStore cookieStore = new BasicCookieStore();
    @LocalServerPort
    protected int port;
    protected String baseUrl;
    protected User mockUser;

    protected void setBaseUrl() {
        this.baseUrl = "http://localhost:" + port + "/thrifty/";
    }

    @BeforeAll
    void init() {
        initCustomRestTemplate();
        setBaseUrl();
        setMockUser();
    }

    private void setMockUser() {
        mockUser = new User();
        mockUser.setUsername("johndoe@example.com");
        mockUser.setPassword("password");
        mockUser.setAccountId(1);
    }

    protected void initCustomRestTemplate() {
        if (customTemplate == null) {
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(cookieStore);

            if (Boolean.parseBoolean(env.getProperty("useBurpProxy")))
                builder.setProxy(new HttpHost("localhost", 8080, "http"));

            httpRequestFactory.setHttpClient(builder.build());
            customTemplate = new RestTemplate(httpRequestFactory);
        }
    }

    protected String getXsrfTokenValue() {
        Cookie xsrfCookie = cookieStore.getCookies().stream()
                .filter(c -> c.getName().equalsIgnoreCase("xsrf-token")).findAny()
                .orElseThrow(() -> new RuntimeException("Missing xsrf token"));
        return xsrfCookie.getValue();
    }

    protected HttpHeaders addXsrfHeader(HttpHeaders headers) {
        headers.add(XSRF_HEADER_NAME, getXsrfTokenValue());
        return headers;
    }

    protected void login(User user) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", user.getUsername());
        map.add("password", user.getPassword());

        customTemplate.getForEntity(baseUrl + "login", String.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-XSRF-TOKEN", getXsrfTokenValue());

        HttpEntity<MultiValueMap<String, String>> submitFormRequest = new HttpEntity<>(map, headers);

        customTemplate.postForEntity(baseUrl + "login", submitFormRequest, String.class);
    }

    protected void logout() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-XSRF-TOKEN", getXsrfTokenValue());
        HttpEntity<MultiValueMap<String, String>> signOutRequest = new HttpEntity<>(null, headers);
        customTemplate.postForEntity(URI.create(baseUrl + "logout"), signOutRequest, String.class);
    }
}

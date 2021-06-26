package org.codecritique.thrifty.e2e;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.codecritique.thrifty.Generator.categorySupplier;

public class CategoriesAccessTest extends BaseSecurityTest {

    @BeforeAll
    public void loginMockUser() {
        login(mockUser);
    }

    @AfterAll
    public void logoutMockUser() {
        logout();
    }

    @Test
    public void shouldCreateCategory() {
        Category category = categorySupplier.get();
        assertThat(category.getAccountId()).isEqualByComparingTo(mockUser.getAccountId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(XSRF_HEADER_NAME, Collections.singletonList(getXsrfTokenValue()));
        URI uri = URI.create(baseUrl + "rest-api/categories");
        RequestEntity<Category> createCategoryRequest = new RequestEntity<>(category, headers, HttpMethod.POST, uri);
        ResponseEntity<String> createCategoryResponse = customTemplate.exchange(createCategoryRequest, String.class);

        assertThat(createCategoryResponse.getStatusCode()).isEqualByComparingTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldForbidCreateCategory() {
        Category category = categorySupplier.get();
        category.setAccountId(100);
        assertThat(category.getAccountId()).isNotEqualTo(mockUser.getAccountId());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.put(XSRF_HEADER_NAME, Collections.singletonList(getXsrfTokenValue()));
        URI uri = URI.create(baseUrl + "rest-api/categories");
        RequestEntity<Category> createCategoryRequest = new RequestEntity<>(category, headers, HttpMethod.POST, uri);
        assertThatExceptionOfType(HttpClientErrorException.Forbidden.class)
                .isThrownBy(() -> customTemplate.exchange(createCategoryRequest, String.class));
    }
}

package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.codecritique.thrifty.Generator.categorySupplier;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseServiceBeanTest {
    @Autowired
    CategoryService categoryService;

    Category createCategory() {
        Category category = categorySupplier.get();
        categoryService.store(category);
        return category;
    }

}

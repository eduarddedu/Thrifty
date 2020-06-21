package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Iterator;
import java.util.Random;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceBeanTest {
    @Autowired
    CategoryServiceBean service;

    @Test
    void testAddCategory() {
        Category category = Category.getInstance("A", "Description");
        service.addCategory(category);
        assertEquals(category, service.getCategory(category.getId()));
    }

    @Test
    void testGetCategoriesSortedByName() {
        Supplier<String> randomNameGenerator = () -> {
            Random random = new Random();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append((char) (65 + random.nextInt(24)));
            }
            return sb.toString();
        };

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++) {
            String name = randomNameGenerator.get();
            Category category = Category.getInstance(name, name);
            service.addCategory(category);
        }

        assertTrue(service.getCategories().size() >= numEntities);

        Iterator<Category> iterator = service.getCategories().iterator();
        while (true) {
            String name = iterator.next().getName();
            if (iterator.hasNext()) {
                String name2 = iterator.next().getName();
                assertTrue(name.compareTo(name2) <= 0);
            } else {
                break;
            }

        }
    }

}
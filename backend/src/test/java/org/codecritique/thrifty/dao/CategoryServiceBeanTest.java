package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceBeanTest extends BaseServiceBeanTest {
    @Autowired
    CategoryServiceBean service;

    @Test
    void testAddCategory() {
        Category category = getCategory();
        service.addCategory(category);
        assertEquals(category, service.getCategory(category.getId()));
    }

    @Test
    void testGetCategoriesSortedByName() {

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            service.addCategory(getCategory());

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

    private Category getCategory() {
        return Category.getInstance(rNameGen.get(), rNameGen.get());
    }

}
package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;
import static org.codecritique.thrifty.TestUtils.*;

class CategoryServiceBeanTest extends BaseServiceBeanTest {

    @Autowired
    private CategoryService categoryService;


    @Test
    void testStoreCategory() {
        Category category = categorySupplier.get();
        categoryService.store(category);
        assertEquals(category, categoryService.get(category.getId()));
    }

    @Test
    void testGetCategoriesSortedByName() {

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            categoryService.store(categorySupplier.get());

        Iterator<String> it = categoryService.getCategories()
                .stream().map(Category::getName).iterator();

        while (it.hasNext()) {
            String name = it.next();
            if (it.hasNext()) {
                assertTrue(name.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void testUpdateCategory() {
        //setup
        Category category = categorySupplier.get();
        categoryService.store(category);

        //exercise
        category.setName(randomName.get());
        category.setDescription(randomName.get());

        //verify
        assertEquals(category, categoryService.get(category.getId()));
    }

    @Test
    void testRemoveCategory() {
        //setup
        Category category = categorySupplier.get();
        categoryService.store(category);

        //exercise
        categoryService.remove(category.getId());

        //verify
        assertNull(categoryService.get(category.getId()));
    }
}
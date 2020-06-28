package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CategoryServiceBeanTest extends BaseServiceBeanTest {

    @Test
    void testAddCategory() {
        Category category = categorySupplier.get();
        categoryService.store(category);
        assertEquals(category, categoryService.get(category.getId()));
    }

    @Test
    void testGetCategoriesSortedByName() {

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            categoryService.store(categorySupplier.get());

        Iterator<String> it = categoryService.getCategoriesSortedByName()
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
        Category cat = categorySupplier.get();
        categoryService.store(cat);

        //exercise
        cat.setName(randomName.get());
        cat.setDescription(randomName.get());

        //verify
        assertEquals(cat, categoryService.get(cat.getId()));
    }

}
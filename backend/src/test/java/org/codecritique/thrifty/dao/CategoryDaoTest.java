package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.codecritique.thrifty.Generator.*;
import static org.junit.jupiter.api.Assertions.*;

class CategoryDaoTest extends BaseDaoTest {

    @Test
    void shouldStoreCategory() {
        Category category = categorySupplier.get();
        repository.save(category);
        assertEquals(category, repository.findById(Category.class, category.getId()));
    }

    @Test
    void shouldGetCategoriesSortedByName() {
        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            repository.save(categorySupplier.get());

        Iterator<String> it = repository.findCategories(ACCOUNT_ID)
                .stream().map(Category::getName).iterator();

        while (it.hasNext()) {
            String name = it.next();
            if (it.hasNext()) {
                assertTrue(name.compareTo(it.next()) <= 0);
            }
        }
    }

    @Test
    void shouldUpdateCategory() {
        //setup
        Category category = categorySupplier.get();
        repository.save(category);
        Expense expense = expenseSupplier.get();
        expense.setCategory(category);
        repository.save(expense);

        //exercise
        category.setName(stringSupplier.get());
        category.setDescription(stringSupplier.get());
        repository.updateEntity(category);

        //verify
        assertEquals(category, repository.findById(Category.class, category.getId()));
        assertEquals(category, expense.getCategory());
    }

    @Test
    void shouldRemoveCategory() {
        //setup
        Category category = categorySupplier.get();
        repository.save(category);

        //exercise
        repository.removeCategory(category.getId());

        //verify
        assertNull(repository.findById(Category.class, category.getId()));
    }

}
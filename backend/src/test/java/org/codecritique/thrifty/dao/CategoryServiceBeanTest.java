package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceBeanTest extends BaseServiceBeanTest {

    @Autowired
    CategoryService service;

    @Autowired
    protected ExpenseServiceBean expenseService;

    @Test
    void testAddCategory() {
        Category category = categorySupplier.get();
        service.store(category);
        assertEquals(category, service.get(category.getId()));
    }

    @Test
    void testGetCategoriesSortedByName() {

        int numEntities = 10;

        for (int i = 0; i < numEntities; i++)
            service.store(categorySupplier.get());

        Iterator<String> it = service.getCategories()
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
        Category c = categorySupplier.get();
        service.store(c);

        //exercise
        c.setName(randomName.get());
        c.setDescription(randomName.get());

        //verify
        assertEquals(c, service.get(c.getId()));
    }

    @Test
    void testRemoveCategory() {
        //setup
        Category c = categorySupplier.get();
        service.store(c);

        //exercise
        service.remove(c.getId());

        //verify
        assertNull(service.get(c.getId()));
    }

    @Test
    void testExpenseHavingNoCategory() {
        Expense expense = new Expense();
        expense.setCreatedOn(randomDate.get());
        expense.setDescription(randomName.get());
        expense.setAmount(0d);
        expenseService.store(expense);
        assertNull(expense.getCategory());
    }

    @Test
    void testSetCategory() {

        Category c = categorySupplier.get();
        Expense e = expenseSupplier.get();
        e.setCategory(c);

        expenseService.store(e);

        //verify
        assertEquals(c, e.getCategory());
        assertEquals(c, service.get(c.getId()));
    }

    @Test
    void testSetCategory_doesNotStoreCategory() {

        Category c = categorySupplier.get();
        Expense e = expenseSupplier.get();

        expenseService.store(e);
        e.setCategory(c);

        //verify
        assertNull(c.getId());
    }

    @Test
    void testRemoveCategoryFromExpense() {
        /*
        Both entities must be stored in order for the delete operation on Category to succeed.
         */
        //setup
        Expense e = expenseSupplier.get();
        Category c = categorySupplier.get();

        expenseService.store(e);
        service.store(c); // comment out to fail the test

        e.setCategory(c);

        //exercise
        service.remove(c.getId()); // get NullPointer here

        //verify
        assertNull(e.getCategory());
    }

}
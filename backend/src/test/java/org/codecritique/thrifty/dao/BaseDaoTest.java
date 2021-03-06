package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.MockMvcTest;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.springframework.beans.factory.annotation.Autowired;

import static org.codecritique.thrifty.Generator.*;

public abstract class BaseDaoTest extends MockMvcTest {
    @Autowired
    protected ExpenseDao expenseDao;
    @Autowired
    protected CategoryDao categoryDao;
    @Autowired
    protected LabelDao labelDao;

    protected Expense createAndStoreExpense() {
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndStoreCategory());
        expenseDao.store(expense);
        return expense;
    }

    protected Category createAndStoreCategory() {
        Category category = categorySupplier.get();
        categoryDao.store(category);
        return category;
    }

    protected Label createAndStoreLabel() {
        Label label = labelSupplier.get();
        labelDao.store(label);
        return label;
    }

}

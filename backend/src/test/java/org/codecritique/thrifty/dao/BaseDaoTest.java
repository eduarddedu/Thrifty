package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.MockMvcTest;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.springframework.beans.factory.annotation.Autowired;

import static org.codecritique.thrifty.Generator.*;

abstract class BaseDaoTest extends MockMvcTest {
    @Autowired
    protected Repository repository;
    protected final long ACCOUNT_ID = 1;

    protected Expense createAndGetExpense() {
        Expense expense = expenseSupplier.get();
        expense.setCategory(createAndGetCategory());
        repository.save(expense);
        return expense;
    }

    protected Category createAndGetCategory() {
        Category category = categorySupplier.get();
        repository.save(category);
        return category;
    }

    protected Label createAndGetLabel() {
        Label label = labelSupplier.get();
        repository.save(label);
        return label;
    }

}

package org.codecritique.thrifty.repository;

import org.codecritique.thrifty.MockMvcTest;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.springframework.beans.factory.annotation.Autowired;

import static org.codecritique.thrifty.Suppliers.*;

abstract class BaseDaoTest extends MockMvcTest {
    @Autowired
    protected Repository repository;
    protected final long ACCOUNT_ID = 1;

    protected Expense createAndGetExpense() {
        Expense expense = expenses.get();
        expense.setCategory(createAndGetCategory());
        repository.save(expense);
        return expense;
    }

    protected Category createAndGetCategory() {
        Category category = categories.get();
        repository.save(category);
        return category;
    }

    protected Label createAndGetLabel() {
        Label label = labels.get();
        repository.save(label);
        return label;
    }

}

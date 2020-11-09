package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.codecritique.thrifty.Generator.*;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseServiceBeanTest {
    @Autowired
    protected ExpenseService expenseService;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected LabelService labelService;


    protected Expense createExpense() {
        Expense expense = expenseSupplier.get();
        expense.setCategory(createCategory());
        expenseService.store(expense);
        return expense;
    }


    protected Category createCategory() {
        Category category = categorySupplier.get();
        categoryService.store(category);
        return category;
    }

    protected Label createLabel() {
        Label label = labelSupplier.get();
        labelService.store(label);
        return label;
    }

}

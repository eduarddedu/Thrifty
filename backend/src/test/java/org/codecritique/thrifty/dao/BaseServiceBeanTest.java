package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Random;
import java.util.function.Supplier;

@SpringBootTest(classes = org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseServiceBeanTest {
    @Autowired
    protected ExpenseServiceBean expenseService;
    @Autowired
    protected LabelServiceBean labelService;
    @Autowired
    protected CategoryServiceBean categoryService;

    protected Supplier<String> randomName = () -> {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        final int size = 8;
        for (int i = 0; i < size; i++) {
            sb.append((char) (65 + random.nextInt(26)));
        }
        return sb.toString();
    };

    protected Supplier<LocalDate> randomDate = () -> {
        Random r = new Random();
        return LocalDate.of(r.nextInt(2000), 1 + r.nextInt(11), 1 + r.nextInt(27));
    };

    protected Supplier<Label> labelSupplier = () -> {
        Label label = new Label();
        label.setName(randomName.get());
        return label;
    };

    protected Supplier<Category> categorySupplier = () -> {
        Category category = new Category();
        category.setName(randomName.get());
        category.setDescription(randomName.get());
        return category;
    };

    protected Supplier<Expense> expenseSupplier = () -> {
        Expense expense = new Expense();
        expense.setCreatedOn(randomDate.get());
        expense.setAmount(0d);
        expense.setCategory(categorySupplier.get());
        expense.addExpenseLabel(labelSupplier.get());
        expense.addExpenseLabel(labelSupplier.get());
        expense.setDescription(randomName.get());
        return expense;
    };
}

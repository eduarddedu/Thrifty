package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.function.Supplier;

public abstract class BaseServiceBeanTest {
    protected static Supplier<String> rNameGen = () -> {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append((char) (65 + random.nextInt(24)));
        }
        return sb.toString();
    };

    protected static Supplier <Label> labelSupplier = () -> {
        Label label = new Label();
        label.setName(rNameGen.get());
        return label;
    };

    protected static Supplier<Category> categorySupplier = () -> {
        Category category = new Category();
        category.setName(rNameGen.get());
        category.setDescription(rNameGen.get());
        return category;
    };

    protected static Supplier<Expense> expenseSupplier = () -> {
        Expense expense = new Expense();
        expense.setCreatedOn(LocalDate.MIN);
        expense.setAmount(0d);
        expense.setCategory(categorySupplier.get());
        expense.setLabels(new HashSet<>(Arrays.asList(labelSupplier.get(), labelSupplier.get())));
        expense.setDescription(rNameGen.get());
        return expense;
    };
}

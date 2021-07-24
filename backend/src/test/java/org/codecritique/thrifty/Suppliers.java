package org.codecritique.thrifty;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.function.Supplier;

public class Suppliers {
    private final static Random r = new Random();
    private final static long ACCOUNT_ID = 1;

    public static Supplier<String> strings = () -> {
        StringBuilder sb = new StringBuilder();
        final int size = 8;
        for (int i = 0; i < size; i++) {
            sb.append((char) (65 + r.nextInt(26)));
        }
        return sb.toString();
    };

    public static Supplier<LocalDate> dates = () ->
            LocalDate.of(2000 + r.nextInt(100), 1 + r.nextInt(12), 1 + r.nextInt(28));

    public static Supplier<Label> labels = () -> {
        Label label = new Label();
        label.setAccountId(ACCOUNT_ID);
        label.setName(strings.get());
        label.setDescription(strings.get());
        return label;
    };

    public static Supplier<Category> categories = () -> {
        Category category = new Category();
        category.setAccountId(ACCOUNT_ID);
        category.setName(strings.get());
        category.setDescription(strings.get());
        return category;
    };

    public static Supplier<BigDecimal> amounts = () -> {
        BigDecimal cents = new BigDecimal(String.format("%.2f", r.nextDouble()));
        return cents.add(new BigDecimal(r.nextInt(100000)));
    };

    public static Supplier<Expense> expenses = () -> {
        Expense expense = new Expense();
        expense.setAccountId(ACCOUNT_ID);
        expense.setCreatedOn(dates.get());
        expense.setAmount(amounts.get());
        expense.setDescription(strings.get());
        return expense;
    };
}

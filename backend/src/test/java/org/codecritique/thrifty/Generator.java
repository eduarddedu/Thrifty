package org.codecritique.thrifty;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.function.Supplier;

public class Generator {
    private final static Random r = new Random();
    private final static long accountId = 1;

    public static Supplier<String> stringSupplier = () -> {
        StringBuilder sb = new StringBuilder();
        final int size = 8;
        for (int i = 0; i < size; i++) {
            sb.append((char) (65 + r.nextInt(26)));
        }
        return sb.toString();
    };

    public static Supplier<LocalDate> dateSupplier = () ->
            LocalDate.of(2000 + r.nextInt(100), 1 + r.nextInt(12), 1 + r.nextInt(28));

    public static Supplier<Label> labelSupplier = () -> {
        Label label = new Label();
        label.setAccountId(accountId);
        label.setName(stringSupplier.get());
        return label;
    };

    public static Supplier<Category> categorySupplier = () -> {
        Category category = new Category();
        category.setAccountId(accountId);
        category.setName(stringSupplier.get());
        category.setDescription(stringSupplier.get());
        return category;
    };

    public static Supplier<BigDecimal> expenseAmountSupplier = () -> {
        BigDecimal cents = new BigDecimal(String.format("%.2f", r.nextDouble()));
        return cents.add(new BigDecimal(r.nextInt(100000)));
    };

    public static Supplier<Expense> expenseSupplier = () -> {
        Expense expense = new Expense();
        expense.setAccountId(accountId);
        expense.setCreatedOn(dateSupplier.get());
        expense.setAmount(expenseAmountSupplier.get());
        expense.setDescription(stringSupplier.get());
        return expense;
    };
}

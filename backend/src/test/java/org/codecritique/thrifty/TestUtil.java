package org.codecritique.thrifty;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;

import java.time.LocalDate;
import java.util.Random;
import java.util.function.Supplier;

public class TestUtil {
    private static Random r = new Random();

    public static Supplier<String> nameSupplier = () -> {
        StringBuilder sb = new StringBuilder();
        final int size = 8;
        for (int i = 0; i < size; i++) {
            sb.append((char) (65 + r.nextInt(26)));
        }
        return sb.toString();
    };

    public static Supplier<LocalDate> dateSupplier = () ->
            LocalDate.of(2000 + r.nextInt(100), 1 + r.nextInt(12), 1 + r.nextInt(28));

    public static Supplier<Label> labelSupplier = () -> new Label(nameSupplier.get());

    public static Supplier<Category> categorySupplier = () -> new Category(nameSupplier.get(), nameSupplier.get());

    public static Supplier<Expense> expenseSupplier = () -> {
        Expense expense = new Expense();
        expense.setCreatedOn(dateSupplier.get());
        expense.setAmount(0d);
        expense.setDescription(nameSupplier.get());
        return expense;
    };
}

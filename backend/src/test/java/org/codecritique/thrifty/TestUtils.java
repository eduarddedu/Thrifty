package org.codecritique.thrifty;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.Expense;
import org.codecritique.thrifty.entity.Label;

import java.time.LocalDate;
import java.util.Random;
import java.util.function.Supplier;

public class TestUtils {
    public static Supplier<String> randomName = () -> {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        final int size = 8;
        for (int i = 0; i < size; i++) {
            sb.append((char) (65 + random.nextInt(26)));
        }
        return sb.toString();
    };

    public static Supplier<LocalDate> randomDate = () -> {
        Random r = new Random();
        return LocalDate.of(r.nextInt(2000), 1 + r.nextInt(12), 1 + r.nextInt(28));
    };

    public static Supplier<Label> labelSupplier = () -> new Label(randomName.get());

    public static Supplier<Category> categorySupplier = () -> new Category(randomName.get(), randomName.get());

    public static Supplier<Expense> expenseSupplier = () -> {
        Expense expense = new Expense();
        expense.setCreatedOn(randomDate.get());
        expense.setAmount(0d);
        expense.setDescription(randomName.get());
        return expense;
    };
}

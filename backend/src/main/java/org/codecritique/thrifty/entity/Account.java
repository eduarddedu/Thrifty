package org.codecritique.thrifty.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "Account")
public class Account extends BaseEntity {
    @NotNull
    private String name = "My account";

    @NotNull
    private String language = "English";

    @NotNull
    private String currency = "EUR";

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Set<Category> categories;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Set<Label> labels;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Set<Expense> expenses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<Label> getLabels() {
        return labels;
    }

    public Set<Expense> getExpenses() {
        return expenses;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addLabel(Label label) {
        labels.add(label);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void removeLabel(Label label) {
        labels.remove(label);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    public void removeExpense(Expense expense) {
        expenses.remove(expense);
    }

}

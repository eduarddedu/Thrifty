package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Eduard Dedu
 */

@Entity
@Table(name = "Category", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Category extends BaseEntity {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @OneToMany(mappedBy = "category")
    private Set<Expense> expenses = new HashSet<>();

    public Category() {
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @JsonIgnore
    public Set<Expense> getExpenses() {
        return new HashSet<>(expenses);
    }

    public void addExpense(Expense expense) {
        this.expenses.add(expense);
    }

    public void removeExpense(Expense expense) {
        this.expenses.remove(expense);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Category))
            return false;
        Category other = (Category) o;
        return Objects.equals(name, other.name) && Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description);
    }


    @Override
    public String toString() {
        return "Category[" + id + "]";
    }

}
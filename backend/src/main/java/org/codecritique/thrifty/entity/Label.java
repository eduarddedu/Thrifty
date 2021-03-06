package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
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
@Table(name = "Label", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Label extends BaseEntity {

    @NotNull
    private String name;

    @ManyToMany(mappedBy = "labels")
    private Set<Expense> expenses = new HashSet<>();

    public Label() {
    }

    public Label(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Label))
            return false;
        return Objects.equals(name, ((Label) o).name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Label[" + name + "]";
    }

}
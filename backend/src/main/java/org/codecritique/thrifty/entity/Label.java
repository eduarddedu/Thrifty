package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Label", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "account_id"}))
@Getter
@Setter
@NoArgsConstructor
public class Label extends BaseEntity {

    @NotNull
    @Column(name = "account_id")
    private Long accountId;

    @NotNull
    @Size(min = 1, max = 25)
    private String name;

    @Size(max = 100)
    private String description = "";

    @ManyToMany(mappedBy = "labels")
    private final Set<Expense> expenses = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Label))
            return false;
        Label other = (Label) o;
        return Objects.equals(id, other.id) &&
                Objects.equals(accountId, other.accountId) &&
                Objects.equals(name, other.name) &&
                Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, name, description);
    }

    @Override
    public String toString() {
        return "Label [" + name + "]";
    }

}
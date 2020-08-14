package org.codecritique.thrifty.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Eduard Dedu
 */


@Entity
@Table(name = "Expense")
public class Expense extends BaseEntity {
    @NotNull
    private LocalDate createdOn;
    @NotNull
    private String description;
    @NotNull
    private Double amount;

    @ManyToOne(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "Expense_Category",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @NotNull
    private Category category;

    @ManyToMany(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "Expense_Label",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private Set<Label> labels = new HashSet<>();

    public Expense() {
    }

    public Expense(LocalDate createdOn, String description, double amount, Category category, Collection<Label> labels) {
        this.createdOn = createdOn;
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.labels.addAll(labels);
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null || category.equals(this.category))
            return;
        if (this.category != null)
            this.category.getExpenses().remove(this);
        category.getExpenses().add(this);
        this.category = category;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDate createdOn) {
        this.createdOn = createdOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setLabels(Collection<Label> labels) {
        this.labels.clear();
        this.labels.addAll(labels);
    }

    public Set<Label> getLabels() {
        return new HashSet<>(labels);
    }

    public void addLabel(Label label) {
        labels.add(label);
        label.getExpenses().add(this);
    }

    public void removeLabel(Label label) {
        label.getExpenses().remove(this);
        labels.remove(label);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Expense))
            return false;
        Expense other = (Expense) o;

        return Objects.equals(id, other.id)
                && Objects.equals(amount, other.amount)
                && Objects.equals(createdOn, other.createdOn)
                && Objects.equals(description, other.description)
                && Objects.equals(category, other.category)
                && Objects.equals(labels, other.labels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount, createdOn, description, category, labels);
    }

    @Override
    public String toString() {
        return "Expense[" + id + "]";
    }


}
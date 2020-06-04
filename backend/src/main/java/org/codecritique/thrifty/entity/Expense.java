package org.codecritique.thrifty.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "Expense")
public class Expense extends BaseEntity {

    @NotNull
    private LocalDate createdOn;

    @NotNull
    private String description;

    @NotNull
    private Double amount;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "Expense_Label",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private Set<Label> labels;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "Expense_Category",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    public Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
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

    public Set<Label> getLabels() {
        return labels;
    }

    public void setLabels(Set<Label> labels) {
        this.labels = labels;
    }

    public void removeLabel(Label label) {
        labels.remove(label);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Expense))
            return false;
        return Objects.equals(id, ((Expense) o).id);
    }

    @Override
    public int hashCode() {
        return 17;
    }

    @Override
    public String toString() {
        return "Expense[" + id + "]";
    }


}
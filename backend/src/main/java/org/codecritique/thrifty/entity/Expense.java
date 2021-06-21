package org.codecritique.thrifty.entity;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "Expense")
public class Expense extends BaseEntity {
    @NotNull
    @Column(name = "account_id")
    private Long accountId;

    @NotNull
    private LocalDate createdOn;

    @NotNull
    @Size(max = 255)
    private String description;

    @NotNull
    @Digits(integer = 7, fraction = 2, message = "Has maximum 7 integer and 2 fractional digits")
    private BigDecimal amount;

    @ManyToOne(cascade = {
            CascadeType.MERGE
    })
    @JoinTable(name = "Expense_Category",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    @NotNull
    private Category category;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "Expense_Label",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private final Set<Label> labels = new HashSet<>();

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        if (category == null || category.equals(this.category))
            return;
        if (this.category != null)
            this.category.removeExpense(this);
        this.category = category;
        this.category.addExpense(this);
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setLabels(Collection<Label> labels) {
        this.labels.clear();
        for (Label label : labels) {
            addLabel(label);
        }
    }

    public Set<Label> getLabels() {
        return new HashSet<>(labels);
    }

    public void addLabel(Label label) {
        labels.add(label);
        label.addExpense(this);
    }

    public void removeLabel(Label label) {
        label.removeExpense(this);
        labels.remove(label);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        else if (!(o instanceof Expense))
            return false;
        Expense other = (Expense) o;
        return Objects.equals(id, other.id) &&
                Objects.equals(accountId, other.accountId) &&
                Objects.equals(amount, other.amount) &&
                Objects.equals(createdOn, other.createdOn) &&
                Objects.equals(description, other.description) &&
                Objects.equals(category, other.category) &&
                isEqual(labels, other.labels);
    }

    /*
     * Compare sets based on logical equality between elements.
     * See: https://bugs.java.com/bugdatabase/view_bug.do?bug_id=6579200
     */
    private boolean isEqual(Set<?> set1, Set<?> set2) {
        if (set1 == null && set2 == null)
            return true;
        else if (set1 == null || set2 == null)
            return false;
        if (set1.size() != set2.size())
            return false;
        for (Object obj : set1) {
            if (!set2.contains(obj))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, accountId, amount, createdOn, description, category, labels);
    }

    @Override
    public String toString() {
        return "Expense [" + id + "]";
    }


}
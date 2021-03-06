package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
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
public class ExpenseView extends BaseEntity {
    @NotNull
    @Column(name = "account_id")
    private Long accountId;

    @NotNull
    private LocalDate createdOn;

    @NotNull
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
    private CategoryView category;

    @ManyToMany(cascade = {CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "Expense_Label",
            joinColumns = @JoinColumn(name = "expense_id"),
            inverseJoinColumns = @JoinColumn(name = "label_id"))
    private Set<LabelView> labels = new HashSet<>();

    @JsonIgnore
    public Long getAccountId() {
        return accountId;
    }

    public LocalDate getCreatedOn() {
        return createdOn;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CategoryView getCategory() {
        return category;
    }

    public Set<LabelView> getLabels() { return new HashSet<>(labels); }

}
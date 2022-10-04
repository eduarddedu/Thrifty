package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Expense")
@Getter
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
    private final Set<LabelView> labels = new HashSet<>();

    @JsonIgnore
    public Long getAccountId() {
        return accountId;
    }

}
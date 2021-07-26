package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "Account")
public class Account extends BaseEntity {
    @NotNull
    @Size(min = 1, max = 30)
    private String name = "My account";

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
    private Set<ExpenseView> expenses;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Set<ExpenseView> getExpenses() {
        return expenses;
    }

    @JsonIgnore
    @Override
    public Long getAccountId() { return id; }

}

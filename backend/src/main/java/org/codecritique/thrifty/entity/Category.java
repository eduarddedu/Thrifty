package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

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

    @JsonIgnore
    public Set<Expense> getExpenses() {
        return expenses;
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
        return Objects.hashCode(name);
    }


    @Override
    public String toString() {
        return "Category[" + id + "]";
    }

}
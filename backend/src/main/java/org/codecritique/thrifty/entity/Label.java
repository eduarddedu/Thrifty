package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
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
    private Set<Expense> expenses;

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
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "Label[" + name + "]";
    }

}
package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * View over the database id of the Category entity.
 */


@Entity
@Table(name = "Category")
public class CategoryView extends BaseEntity {

    @Override
    @JsonIgnore
    public Long getAccountId() { return null; }
}

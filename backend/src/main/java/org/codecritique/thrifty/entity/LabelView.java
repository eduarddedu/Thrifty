package org.codecritique.thrifty.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * View over the database id of the Label entity.
 */

@Entity
@Table(name = "Label")
public class LabelView extends BaseEntity {
    @Override
    @JsonIgnore
    public Long getAccountId() { return null; }
}

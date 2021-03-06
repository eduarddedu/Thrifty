package org.codecritique.thrifty.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
  This view exposes only the id field of the entity.
 */

@Entity
@Table(name = "Category")
public class CategoryView extends BaseEntity { }

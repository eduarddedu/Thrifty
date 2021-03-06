package org.codecritique.thrifty.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 This view exposes only the id field of the entity.
 */

@Entity
@Table(name = "Label")
public class LabelView extends BaseEntity { }

package org.codecritique.thrifty.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 This view exposes only the id field of the entity.
 */

@Entity
@Table(name = "Account")
public class AccountView extends BaseEntity { }

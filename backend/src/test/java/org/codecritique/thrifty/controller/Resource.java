package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.*;

public enum Resource {
    EXPENSE("http://localhost:8080/rest-api/expenses/", Expense.class),
    CATEGORY("http://localhost:8080/rest-api/categories/", Category.class),
    LABEL("http://localhost:8080/rest-api/labels/", Label.class),
    USER("http://localhost:8080/rest-api/user/", User.class),
    ACCOUNT("http://localhost:8080/rest-api/account/", Account.class);

    Resource(String url, Class<? extends BaseEntity> klass) {
        this.url = url;
        this.klass = klass;
    }

    String url;
    Class<? extends BaseEntity> klass;

    static Resource resolveEntityClass(Class<? extends BaseEntity> klass) {
        for (Resource resource : Resource.values()) {
            if (resource.klass.equals(klass))
                return resource;
        }
        throw new IllegalArgumentException("Entity class not mapped: " + klass);
    }
}

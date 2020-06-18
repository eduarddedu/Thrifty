package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Category;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes=org.codecritique.thrifty.Application.class)
@ActiveProfiles("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CategoryServiceBeanTest {
    @Autowired
    CategoryServiceBean service;

    @Test
    void testAddGetCategory() {
        Category o = new Category();
        o.setName("A");
        o.setDescription("B");
        service.addCategory(o);
        assertEquals(o, service.getCategory(o.getId()));
    }

    @Test
    void testGetCategoriesSortedByName() {
        List<Category> list = new ArrayList<>();
        String [] arr = {"A", "B", "C"};
        for(int i = 0; i < 3; i++) {
            Category o = new Category();
            o.setName(arr[i]);
            o.setDescription(arr[i]);
            list.add(o);
            service.addCategory(o);
        }
        assertEquals(list.size(), service.getCategoriesSortedByName().size());
        assertEquals(list, service.getCategoriesSortedByName());
    }

    @AfterAll
    void removeAll() {
        service.getCategoriesSortedByName().forEach(category -> service.removeCategory(category.getId()));
    }
}
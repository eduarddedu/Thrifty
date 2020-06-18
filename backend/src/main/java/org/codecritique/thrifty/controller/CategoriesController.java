package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.CategoryServiceBean;
import org.codecritique.thrifty.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/rest-api/categories")
public class CategoriesController extends BaseController {

    @Autowired
    CategoryServiceBean categoryServiceBean;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategories() {
        return categoryServiceBean.getCategories();
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Category getCategoryById(@PathVariable int id) {
        return categoryServiceBean.getCategory(id);
    }
}

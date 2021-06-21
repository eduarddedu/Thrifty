package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.CategoryDao;
import org.codecritique.thrifty.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rest-api/categories")
public class CategoriesController extends BaseController {
    @Autowired
    private CategoryDao dao;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createCategory(@RequestBody Category category) {
        dao.store(category);
        URI location = toAbsoluteURI("/rest-api/categories/" + category.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateCategory(@RequestBody Category category) {
        dao.updateCategory(category);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        Category category = dao.getCategory(id);
        if (category == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(category);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesSortedByName() {
        return dao.getCategories();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeCategory(@PathVariable long id) {
        Category category = dao.getCategory(id);
        if (category == null)
            return ResponseEntity.notFound().build();
        dao.removeCategory(id);
        return ResponseEntity.ok().build();
    }
}

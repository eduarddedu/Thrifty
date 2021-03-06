package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.CategoryService;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.exception.WebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * @author Eduard Dedu
 */


@RestController
@RequestMapping("/rest-api/categories")
public class CategoriesController extends BaseController {
    @Autowired
    private CategoryService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createCategory(@RequestBody Category category) {
        try {
            service.store(category);
            URI uri = toAbsoluteUri("/rest-api/categories/" + category.getId());
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            if (isConstraintViolationException(e))
                return ResponseEntity.badRequest().build();
            throw new WebException(e);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateCategory(@RequestBody Category category) {
        try {
            service.updateCategory(category);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            if (isConstraintViolationException(e))
                return ResponseEntity.badRequest().build();
            throw new WebException(e);
        }
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        try {
            Category category = service.getCategory(id);
            if (category == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(category);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesSortedByName() {
        try {
            return service.getCategories();
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeCategory(@PathVariable long id) {
        try {
            Category category = service.getCategory(id);
            if (category == null)
                return ResponseEntity.notFound().build();
            service.removeCategory(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new WebException(e);
        }
    }
}

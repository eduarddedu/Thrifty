package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.CategoryServiceBean;
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
    private CategoryServiceBean service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createCategory(@RequestBody Category category) {
        try {
            service.store(category);
            URI uri = toAbsoluteUri("/rest-api/categories/" + category.getId());
            return ResponseEntity.created(uri).build();
        } catch (Throwable th) {
            if (isConstraintViolationException(th))
                return ResponseEntity.badRequest().build();
            throw new WebException(th);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateCategory(@RequestBody Category category) {
        try {
            service.update(category);
            return ResponseEntity.ok().build();
        } catch (Throwable th) {
            if (isConstraintViolationException(th))
                return ResponseEntity.badRequest().build();
            throw new WebException(th);
        }
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        try {
            Category category = service.get(id);
            if (category == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(category);
        } catch (Throwable th) {
            throw new WebException(th);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesSortedByName() {
        try {
            return service.getCategories();
        } catch (Throwable th) {
            throw new WebException(th);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeCategory(@PathVariable long id) {
        try {
            Category category = service.get(id);
            if (category == null)
                return ResponseEntity.notFound().build();
            service.remove(id);
            return ResponseEntity.ok().build();
        } catch (Throwable th) {
            throw new WebException(th);
        }
    }
}

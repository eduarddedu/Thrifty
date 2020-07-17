package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.CategoryServiceBean;
import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.exception.WebException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Eduard Dedu
 */


@RestController
@RequestMapping("/rest-api/categories")
public class CategoriesController extends BaseController {

    @Autowired
    CategoryServiceBean service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createCategory(@RequestBody Category category) {
        try {
            service.store(category);
            return ResponseEntity.created(toAbsoluteUri("/rest-api/categories/" + category.getId())).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateCategory(@RequestBody Category category) {
        try {
            service.update(category);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeCategory(@PathVariable long id) {
        try {
            Category category = service.get(id);
            if (category != null) {
                service.remove(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        try {
            Category category = service.get(id);
            if (category != null)
                return ResponseEntity.ok(category);
            return ResponseEntity.notFound().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesSortedByName() {
        try {
            return service.getCategories();
        } catch (Throwable e) {
            throw new WebException(e.getMessage());
        }
    }
}

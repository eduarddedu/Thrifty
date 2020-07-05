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

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesSortedByName() {
        return service.getCategories();
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Category getCategory(@PathVariable long id) {
        return service.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> storeCategory(@RequestBody Category category) {
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
}

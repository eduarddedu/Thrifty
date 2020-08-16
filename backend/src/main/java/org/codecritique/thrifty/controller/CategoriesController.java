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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createCategory(@RequestBody Category category) {
        try {
            CategoryServiceBean service = new CategoryServiceBean();
            service.store(category);
            return ResponseEntity.created(toAbsoluteUri("/rest-api/categories/" + category.getId())).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateCategory(@RequestBody Category category) {
        try {
            CategoryServiceBean service = new CategoryServiceBean();
            service.update(category);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeCategory(@PathVariable long id) {
        try {
            CategoryServiceBean service = new CategoryServiceBean();
            Category category = service.get(id);
            if (category != null) {
                service.remove(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable long id) {
        try {
            CategoryServiceBean service = new CategoryServiceBean();
            Category category = service.get(id);
            if (category != null)
                return ResponseEntity.ok(category);
            return ResponseEntity.notFound().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesSortedByName() {
        try {
            CategoryServiceBean service = new CategoryServiceBean();
            return service.getCategories();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }
}

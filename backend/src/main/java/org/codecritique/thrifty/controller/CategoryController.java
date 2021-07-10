package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Category;
import org.codecritique.thrifty.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rest-api/categories")
public class CategoryController extends BaseController {

    @PreAuthorize("hasAuthority(#category.accountId)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createCategory(@RequestBody Category category) {
        repository.save(category);
        URI location = toAbsoluteURI("/rest-api/categories/" + category.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority(#category.accountId)")
    public ResponseEntity<Resource> updateCategory(@RequestBody Category category) {
        repository.updateEntity(category);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Category> getCategory(@PathVariable long id, @AuthenticationPrincipal User user) {
        Category category = repository.findById(Category.class, id);
        if (category == null)
            return ResponseEntity.notFound().build();
        if (isAuthorizedToAccess(user, category))
            return ResponseEntity.ok(category);
        throw new AccessDeniedException("Access is denied");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Category> getCategoriesSortedByName(@AuthenticationPrincipal User user) {
        return repository.findCategories(user.getAccountId());
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeCategory(@PathVariable long id, @AuthenticationPrincipal User user) {
        Category category = repository.findById(Category.class, id);
        if (category == null)
            return ResponseEntity.notFound().build();
        if (isAuthorizedToAccess(user, category) && category.getExpenses().isEmpty()) {
            repository.removeCategory(id);
            return ResponseEntity.ok().build();
        }
        throw new AccessDeniedException("Access is denied");
    }
}

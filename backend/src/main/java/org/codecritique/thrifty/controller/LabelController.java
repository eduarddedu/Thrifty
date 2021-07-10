package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Label;
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
@RequestMapping("/rest-api/labels")
public class LabelController extends BaseController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority(#label.accountId)")
    public ResponseEntity<Resource> createLabel(@RequestBody Label label) {
        repository.save(label);
        URI location = toAbsoluteURI("/rest-api/labels/" + label.getId());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAuthority(#label.accountId)")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateLabel(@RequestBody Label label) {
        repository.updateEntity(label);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeLabel(@PathVariable long id, @AuthenticationPrincipal User user) {
        Label label = repository.findById(Label.class,id);
        if (label == null)
            return ResponseEntity.notFound().build();
        if (isAuthorizedToAccess(user, label)) {
            repository.removeLabel(id);
            return ResponseEntity.ok().build();
        }
        throw new AccessDeniedException("Access is denied");
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Label> getLabel(@PathVariable long id, @AuthenticationPrincipal User user) {
        Label label = repository.findById(Label.class,id);
        if (label == null)
            return ResponseEntity.notFound().build();
        if (isAuthorizedToAccess(user, label))
            return ResponseEntity.ok(label);
        throw new AccessDeniedException("Access is denied");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Label> getLabelsSortedByName(@AuthenticationPrincipal User user) {
        return repository.findLabels(user.getAccountId());
    }

}


package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.LabelDao;
import org.codecritique.thrifty.entity.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rest-api/labels")
public class LabelsController extends BaseController {
    @Autowired
    private LabelDao dao;

    @PreAuthorize("hasAuthority(#label.accountId)")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createLabel(@RequestBody Label label) {
        dao.store(label);
        URI location = toAbsoluteURI("/rest-api/labels/" + label.getId());
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasAuthority(#label.accountId)")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateLabel(@RequestBody Label label) {
        dao.updateLabel(label);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeLabel(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Label label = dao.getLabel(id);
        if (label == null)
            return ResponseEntity.notFound().build();
        if (hasAuthority(userDetails, label)) {
            dao.removeLabel(id);
            return ResponseEntity.ok().build();
        }
        throw new AccessDeniedException("Access is denied");
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Label> getLabel(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        Label label = dao.getLabel(id);
        if (label == null)
            return ResponseEntity.notFound().build();
        if (hasAuthority(userDetails, label))
            return ResponseEntity.ok(label);
        throw new AccessDeniedException("Access is denied");
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Label> getLabelsSortedByName(@AuthenticationPrincipal UserDetails userDetails) {
        long accountId = Long.parseLong(userDetails.getAuthorities().iterator().next().getAuthority());
        return dao.getLabels(accountId);
    }

}


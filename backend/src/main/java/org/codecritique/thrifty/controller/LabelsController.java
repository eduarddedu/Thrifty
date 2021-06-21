package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.LabelDao;
import org.codecritique.thrifty.entity.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/rest-api/labels")
public class LabelsController extends BaseController {
    @Autowired
    private LabelDao dao;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createLabel(@RequestBody Label label) {
        dao.store(label);
        URI location = toAbsoluteURI("/rest-api/labels/" + label.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateLabel(@RequestBody Label label) {
        dao.updateLabel(label);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeLabel(@PathVariable long id) {
        Label label = dao.getLabel(id);
        if (label == null)
            return ResponseEntity.notFound().build();
        dao.removeLabel(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Label> getLabel(@PathVariable long id) {
        Label label = dao.getLabel(id);
        if (label == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(label);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Label> getLabelsSortedByName() {
        return dao.getLabels();
    }

}


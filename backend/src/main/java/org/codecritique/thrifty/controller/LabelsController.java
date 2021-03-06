package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.LabelService;
import org.codecritique.thrifty.entity.Label;
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
@RequestMapping("/rest-api/labels")
public class LabelsController extends BaseController {
    @Autowired
    private LabelService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createLabel(@RequestBody Label label) {
        try {
            service.store(label);
            URI uri = toAbsoluteUri("/rest-api/labels/" + label.getId());
            return ResponseEntity.created(uri).build();
        } catch (Exception e) {
            if (isConstraintViolationException(e))
                return ResponseEntity.badRequest().build();
            throw new WebException(e);
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateLabel(@RequestBody Label label) {
        try {
            service.updateLabel(label);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            if (isConstraintViolationException(e))
                return ResponseEntity.badRequest().build();
            throw new WebException(e);
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeLabel(@PathVariable long id) {

        try {
            Label label = service.getLabel(id);
            if (label == null)
                return ResponseEntity.notFound().build();
            service.removeLabel(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            if (isConstraintViolationException(e))
                return ResponseEntity.badRequest().build();
            throw new WebException(e);
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Label> getLabel(@PathVariable long id) {

        try {
            Label label = service.getLabel(id);
            if (label == null)
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok(label);
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Label> getLabelsSortedByName() {
        try {
            return service.getLabels();
        } catch (Exception e) {
            throw new WebException(e);
        }
    }

}


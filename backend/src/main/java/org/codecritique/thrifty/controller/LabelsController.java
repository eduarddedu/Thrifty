package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.LabelServiceBean;
import org.codecritique.thrifty.entity.Label;
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
@RequestMapping("/rest-api/labels")
public class LabelsController extends BaseController {
    @Autowired
    private LabelServiceBean service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createLabel(@RequestBody Label label) {
        try {
            service.store(label);
            return ResponseEntity.created(toAbsoluteUri("/rest-api/labels/" + label.getId())).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateLabel(@RequestBody Label label) {
        try {
            service.update(label);
            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Resource> removeLabel(@PathVariable long id) {
        try {
            Label label = service.get(id);
            if (label != null) {
                service.remove(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.notFound().build();
        } catch (Throwable ex) {
            throw new WebException(ex.getMessage());
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Label> getLabel(@PathVariable long id) {
        Label label = service.get(id);
        if (label != null)
            return ResponseEntity.ok(label);
        return ResponseEntity.notFound().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Label> getLabelsSortedByName() {
        return service.getLabels();
    }

}


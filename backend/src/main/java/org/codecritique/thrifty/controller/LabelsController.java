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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> createLabel(@RequestBody Label label) {
        LabelServiceBean service = new LabelServiceBean();
        try {
            service.store(label);
            return ResponseEntity.created(toAbsoluteUri("/rest-api/labels/" + label.getId())).build();
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource> updateLabel(@RequestBody Label label) {
        LabelServiceBean service = new LabelServiceBean();
        try {
            service.update(label);
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
    public ResponseEntity<Resource> removeLabel(@PathVariable long id) {
        LabelServiceBean service = new LabelServiceBean();
        try {
            Label label = service.get(id);
            if (label != null) {
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

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Label> getLabel(@PathVariable long id) {
        LabelServiceBean service = new LabelServiceBean();
        try {
            Label label = service.get(id);
            if (label != null)
                return ResponseEntity.ok(label);
            return ResponseEntity.notFound().build();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Label> getLabelsSortedByName() {
        LabelServiceBean service = new LabelServiceBean();
        try {
            return service.getLabels();
        } catch (Throwable th) {
            WebException ex = new WebException(th);
            ex.printStackTrace();;
            throw ex;
        }
    }

}


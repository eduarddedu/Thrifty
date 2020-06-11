package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.entity.Label;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/rest-api/labels")
public class LabelsController extends BaseController {

    @GetMapping
    public List<Label> getAllLabels() {
        return em.createQuery("select l from Label l order by l.name", Label.class).getResultList();
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Label getLabelById(@PathVariable int id) {
        return em.find(Label.class, id);
    }
}

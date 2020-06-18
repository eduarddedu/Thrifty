package org.codecritique.thrifty.controller;

import org.codecritique.thrifty.dao.LabelServiceBean;
import org.codecritique.thrifty.entity.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/rest-api/labels")
public class LabelsController extends BaseController {
    @Autowired
    LabelServiceBean labelServiceBean;

    @GetMapping
    public List<Label> getLabels() {
        return labelServiceBean.getLabels();
    }

    @GetMapping(path = "{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Label getLabelById(@PathVariable int id) {
        return labelServiceBean.getLabel(id);
    }
}

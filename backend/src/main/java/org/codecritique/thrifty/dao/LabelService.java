package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;

import java.util.List;

public interface LabelService {

    void addEntity(Label label);
    Label getLabel(int id);
    List<Label> getLabelsSortedByName();
    void removeLabel(int id);
}

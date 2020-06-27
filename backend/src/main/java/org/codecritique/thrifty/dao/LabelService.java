package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface LabelService {

    void addLabel(Label label);

    Label getLabel(int id);

    List<Label> getLabels();

    void removeLabel(int id);

    void updateLabel(Label label);
}

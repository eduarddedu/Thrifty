package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface LabelService {

    void store(Label label);

    Label getLabel(long id);

    List<Label> getLabels();

    void removeLabel(long id);

    void updateLabel(Label label);
}

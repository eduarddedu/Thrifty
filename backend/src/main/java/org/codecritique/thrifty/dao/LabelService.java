package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface LabelService {

    void store(Label label);

    Label get(long id);

    List<Label> getLabels();

    void remove(long id);

    void update(Label label);
}

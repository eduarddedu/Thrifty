package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;

import java.util.List;

/**
 * @author Eduard Dedu
 */


public interface LabelService {

    void store(Label label);

    Label get(int id);

    List<Label> getLabelsSortedByName();

    void remove(int id);

    void update(Label label);
}

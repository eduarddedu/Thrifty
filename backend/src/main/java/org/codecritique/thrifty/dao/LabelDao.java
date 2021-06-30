package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;

import java.util.List;

public interface LabelDao {

    void store(Label label);

    Label getLabel(long id);

    List<Label> getLabels(long accountId);

    void removeLabel(long id);

    void updateLabel(Label label);
}

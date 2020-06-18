package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LabelServiceBean extends BaseEntityService implements LabelService {

    @Override
    public void addEntity(Label label) {

        super.addEntity(label);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Label> getLabelsSortedByName() {

        return (List<Label>) super.getEntitiesSortedByName(Label.class);
    }

    @Override
    public Label getLabel(int id) {
        return this.getEntity(Label.class, id);
    }

    @Override
    public void removeLabel(int id) {
        super.removeEntity(Label.class, id);
    }
}

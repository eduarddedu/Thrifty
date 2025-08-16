package org.codecritique.thrifty.repository;

import org.codecritique.thrifty.entity.Label;
import org.springframework.data.repository.Repository;

import java.util.List;


interface LabelRepository extends Repository<Label, Long> {
    List<Label> findByAccountId(long accountId);
}

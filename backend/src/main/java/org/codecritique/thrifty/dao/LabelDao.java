package org.codecritique.thrifty.dao;

import org.codecritique.thrifty.entity.Label;
import org.springframework.data.repository.Repository;

import java.util.List;


interface LabelDao extends Repository<Label, Long> {
    List<Label> findByAccountId(long accountId);
}

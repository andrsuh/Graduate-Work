package ru.sukhoa.DAO.Postgres;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sukhoa.domain.GraphLink;

@Repository
public interface GraphLinkRepositoryPostgres extends CrudRepository<GraphLink, Integer> {
    GraphLink findByLeftNodePkAndRightNodePk(String leftNodePk, String rightNodePk);
}
package ru.sukhoa.DAO.Postgres;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sukhoa.domain.GraphLink;

@Repository
public interface GraphLinkRepositoryPostgres extends CrudRepository<GraphLink, Integer> {

    @Query(value = "SELECT * FROM graph_link WHERE left_node=? AND right_node=? LIMIT 1;", nativeQuery = true)
    GraphLink findLink(String leftNodePk, String rightNodePk);
}
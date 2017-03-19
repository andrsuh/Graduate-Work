package ru.sukhoa.DAO.Postgres;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sukhoa.domain.Node;

import java.util.List;

@Repository
public interface NodeRepositoryPostgres extends CrudRepository<Node, String> {
    @Query(value = "WITH RECURSIVE q AS\n" +
            "(SELECT *, 1 AS level FROM node n WHERE n.id = ? \n" +
            "    UNION ALL\n" +
            "  SELECT n.*, 1 + level AS level FROM node n\n" +
            "  JOIN q\n" +
            "  ON n.part_of = q.id\n" +
            ")\n" +
            "SELECT * FROM q;", nativeQuery = true)
    List<Node> getSubtreeInRootOf(String id);
}
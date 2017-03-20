package ru.sukhoa.DAO.Postgres;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sukhoa.domain.Node;

import java.util.List;

@Repository
public interface NodeRepositoryPostgres extends CrudRepository<Node, String> {
    @Query(value = "WITH RECURSIVE q AS\n" +
            "(SELECT *, 1 AS level FROM node n WHERE n.id = ?\n" +
            "    UNION ALL\n" +
            "  SELECT n.*, 1 + level AS level FROM graph_link l\n" +
            "  JOIN q ON l.right_node = q.id\n" +
            "  JOIN node n ON n.id = l.left_node\n" +
            ")\n" +
            "SELECT * FROM q;", nativeQuery = true)
    List<Node> getSubtreeInRootOf(String id);

    @Query(value = "WITH RECURSIVE q AS (\n" +
            "  SELECT n.* FROM node n\n" +
            "  JOIN graph_link l ON n.id = l.right_node\n" +
            "  WHERE l.left_node = ?\n" +
            "    UNION ALL\n" +
            "  SELECT n.* FROM graph_link l\n" +
            "  JOIN q ON q.id = l.left_node\n" +
            "  JOIN node n ON n.id = l.right_node\n" +
            ")\n" +
            "SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END\n" +
            "FROM q\n" +
            "WHERE q.id = ?;\n", nativeQuery = true)
    boolean isNodeDescendantOfAnother(String descendantPk, String ancestorPk);
}
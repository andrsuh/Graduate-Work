package ru.sukhoa.DAO.Neo4j;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sukhoa.domain.Node;

import java.util.List;

@Repository
public interface NodeRepositoryNeo4j extends CrudRepository<Node, Long> {
    Node findOneByPk(String pk);

    @Query(value = "MATCH (n:node {pk:{0}})-[:PART_OF*1..2]-(children) RETURN *;")
    List<Node> getSubtreeInRootOf(String id);

    @Query("MATCH (n:node)-[r:PART_OF]->(an:node {pk:{0)}) RETURN n;")
    List<Node> getChildOfNode(String id);

    @Query("MATCH (n:node {pk:{0}})-[:PART_OF*]->(a:node {pk:{1}}) " +
            "RETURN " +
            "   CASE count(a) " +
            "   WHEN 0 THEN FALSE " +
            "   ELSE TRUE " +
            "END;")
    boolean isNodeDescendantOfAnother(String descendantPk, String ancestorPk);
}


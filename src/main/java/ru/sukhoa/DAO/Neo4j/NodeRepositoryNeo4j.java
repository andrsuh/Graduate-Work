package ru.sukhoa.DAO.Neo4j;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.sukhoa.DAO.NodeRepository;
import ru.sukhoa.domain.Node;

import java.util.List;

@Transactional
@Repository(value = "NodeRepositoryNeo4j")
public interface NodeRepositoryNeo4j extends CrudRepository<Node, Long>, NodeRepository {

    String DATASOURCE_NAME = "NEO";

    default String getDatasourceName() {
        return DATASOURCE_NAME;
    }

    Node findOneByPk(String pk);

    @Query("MATCH (n:node {pk:{0}}) " +
            "RETURN " +
            "   CASE count(n) " +
            "   WHEN 0 THEN FALSE " +
            "   ELSE TRUE " +
            "END;")
    boolean exists(String pk);

    @Query(value = "MATCH (n:node {pk:{0}})<-[:PART_OF*]-(children) RETURN *;")
    List<Node> getSubtreeInRootOf(String pk);

    @Query("MATCH (n:node)-[r:PART_OF]->(an:node {pk:{0}}) RETURN n;")
    List<Node> getChildrenOfNode(String pk);

    @Query("MATCH (n:node {pk:{0}})-[:PART_OF*]->(a:node {pk:{1}}) " +
            "RETURN " +
            "   CASE count(a) " +
            "   WHEN 0 THEN FALSE " +
            "   ELSE TRUE " +
            "END;")
    boolean isNodeDescendantOfAnother(String descendantPk, String ancestorPk);
}


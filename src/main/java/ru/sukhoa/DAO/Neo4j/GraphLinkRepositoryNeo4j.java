package ru.sukhoa.DAO.Neo4j;


import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;
import ru.sukhoa.domain.GraphLink;

public interface GraphLinkRepositoryNeo4j extends CrudRepository<GraphLink, Long> {

    @Query("MATCH (n:node {pk:{0}})-[r:PART_OF]-(another:node {pk:{1}}) RETURN r;")
    GraphLink findRelationshipByNodesPk(String childNodePk, String parentNodePk);
}

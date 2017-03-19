package ru.sukhoa.service;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.DAO.Neo4j.NodeRepositoryNeo4j;
import ru.sukhoa.DAO.Postgres.GraphLinkRepositoryPostgres;
import ru.sukhoa.DAO.Postgres.NodeRepositoryPostgres;
import ru.sukhoa.domain.GraphLink;
import ru.sukhoa.domain.Node;

import javax.validation.constraints.Null;

@Service
public class GraphLinkService {

    private NodeRepositoryPostgres psRepository;

    private GraphLinkRepositoryPostgres graphLinkRepository;

    private NodeRepositoryNeo4j neoRepository;

    @Autowired
    public void setPsRepository(NodeRepositoryPostgres psRepository) {
        this.psRepository = psRepository;
    }

    @Autowired
    public void setGraphLinkRepository(GraphLinkRepositoryPostgres graphLinkRepository) {
        this.graphLinkRepository = graphLinkRepository;
    }

    @Autowired
    public void setNeoRepository(NodeRepositoryNeo4j neoRepository) {
        this.neoRepository = neoRepository;
    }

    public void linkNodes(@Nullable final String childPk, @Nullable final String parentPk) {
        if (childPk == null || parentPk == null) {
            throw new IllegalArgumentException("Can not create graph link: null has been passed");
        }

        createNeo4jGraphLink(childPk, parentPk);
        createPostgresGraphLink(childPk, parentPk);
    }

    public void createNeo4jGraphLink(@NotNull final String childPk, @NotNull final String parentPk) {
        Node child = neoRepository.findOneByPk(childPk);
        Node parent = neoRepository.findOneByPk(parentPk);
        if (child == null) {
            throw new IllegalArgumentException("Can not create graph link: child does not exist");
        }
        if (parent == null) {
            throw new IllegalArgumentException("Can not create graph link: parent does not exist");
        }

        child.setPartOf(parent);
        neoRepository.save(child);
    }

    public void createPostgresGraphLink(@NotNull final String childPk, @NotNull final String parentPk) {
        if (!psRepository.exists(childPk)) {
            throw new IllegalArgumentException("Can not create graph link: child does not exist");
        }
        if (!psRepository.exists(parentPk)) {
            throw new IllegalArgumentException("Can not create graph link: parent does not exist");
        }

        if (graphLinkRepository.findByLeftNodePkAndRightNodePk(childPk, parentPk) == null) {
            graphLinkRepository.save(new GraphLink(childPk, parentPk));
        }
    }
}

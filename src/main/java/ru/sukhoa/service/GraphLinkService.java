package ru.sukhoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sukhoa.DAO.Neo4j.GraphLinkRepositoryNeo4j;
import ru.sukhoa.DAO.Neo4j.NodeRepositoryNeo4j;
import ru.sukhoa.DAO.Postgres.GraphLinkRepositoryPostgres;
import ru.sukhoa.DAO.Postgres.NodeRepositoryPostgres;
import ru.sukhoa.domain.GraphLink;
import ru.sukhoa.domain.Node;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.UUID;

@Service
public class GraphLinkService {

    private NodeRepositoryPostgres psRepository;

    private GraphLinkRepositoryNeo4j graphLinkRepositoryNeo4j;

    private GraphLinkRepositoryPostgres graphLinkRepositoryPostgres;

    private NodeRepositoryNeo4j neoRepository;

    private MeasureService measureService;

    @Autowired
    public void setPsRepository(NodeRepositoryPostgres psRepository) {
        this.psRepository = psRepository;
    }

    @Autowired
    public void setGraphLinkRepositoryPostgres(GraphLinkRepositoryPostgres graphLinkRepositoryPostgres) {
        this.graphLinkRepositoryPostgres = graphLinkRepositoryPostgres;
    }

    @Autowired
    public void setGraphLinkRepositoryNeo4j(GraphLinkRepositoryNeo4j graphLinkRepositoryNeo4j) {
        this.graphLinkRepositoryNeo4j = graphLinkRepositoryNeo4j;
    }

    @Autowired
    public void setNeoRepository(NodeRepositoryNeo4j neoRepository) {
        this.neoRepository = neoRepository;
    }

    @Autowired
    public void setMeasureService(MeasureService measureService) {
        this.measureService = measureService;
    }

    public void linkNodes(@Nullable final String childPk, @Nullable final String parentPk) {
        if (childPk == null || parentPk == null) {
            throw new IllegalArgumentException("Can not create graph link: null has been passed");
        }

//        createNeo4jGraphLink(childPk, parentPk);
        createPostgresGraphLink(childPk, parentPk);
    }

    @Transactional
    public void createNeo4jGraphLink(@Nonnull final String childPk, @Nonnull final String parentPk) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.NEO_PERSIST);

        Node child = neoRepository.findOneByPk(childPk);
        Node parent = neoRepository.findOneByPk(parentPk);

        checkIfLinkingAllowed(child, parent);

        if (graphLinkRepositoryNeo4j.findRelationshipByNodesPk(childPk, parentPk) == null) {
            if (child.getPartOf() == null) {
                child.setPartOf(new HashSet<>());
            }
            child.getPartOf().add(parent);
            neoRepository.save(child);
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.NEO_PERSIST, measureId);
    }

    @Transactional
    public void createPostgresGraphLink(@Nonnull String childPk, @Nonnull String parentPk) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST);

        Node child = psRepository.findOne(childPk);
        Node parent = psRepository.findOne(parentPk);

        checkIfLinkingAllowed(child, parent);

        if (graphLinkRepositoryPostgres.findLink(childPk, parentPk) == null) {
            graphLinkRepositoryPostgres.save(new GraphLink(child, parent));
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST, measureId);
    }

    public void checkIfLinkingAllowed(@Nullable Node child, @Nullable Node parent) {
        if (child == null) {
            throw new IllegalArgumentException("Can not create graph link: child does not exist");
        }
        if (parent == null) {
            throw new IllegalArgumentException("Can not create graph link: parent does not exist");
        }
        if (child.getType().equals("GROUP") && child.getPartOf() != null && child.getPartOf().size() > 0) {
            throw new IllegalArgumentException("Group already linked to another node");
        }
        if (!parent.getType().equals("GROUP")) {
            throw new IllegalArgumentException("Nodes can not be linked to non-group node");
        }
    }
}

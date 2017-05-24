package ru.sukhoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sukhoa.DAO.Neo4j.NodeRepositoryNeo4j;
import ru.sukhoa.DAO.Postgres.NodeRepositoryPostgres;
import ru.sukhoa.domain.Node;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class NodeService {

    private NodeRepositoryPostgres psRepository;

    private NodeRepositoryNeo4j neoRepository;

    private MeasureService measureService;

    private GraphLinkService graphLinkService;

    @Autowired
    public void setGraphLinkService(GraphLinkService graphLinkService) {
        this.graphLinkService = graphLinkService;
    }

    @Autowired
    public void setNeoRepository(NodeRepositoryNeo4j neoRepository) {
        this.neoRepository = neoRepository;
    }

    @Autowired
    public void setPsRepository(NodeRepositoryPostgres psRepository) {
        this.psRepository = psRepository;
    }

    @Autowired
    public void setMeasureService(MeasureService measureService) {
        this.measureService = measureService;
    }

    public void createNodes(List<Node> nodes) {
        nodes.forEach(node -> {
            createNodeNeo4j(node);
            createNodePostgres(node);
        });
    }

    public void updateNode(Node updatingNode) {
        if (updatingNode.getPk() == null) {
            throw new IllegalArgumentException("Passed node has pk = null");
        }

        if (updatingNode.getName() == null) {
            throw new IllegalArgumentException("Passed node with null name for update");
        }

        updateNodePostgres(updatingNode);
        updateNodeNeo4j(updatingNode);
    }

    // todo remove redundant code
    public void updateNodePostgres(Node updatingNode) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.POSTGRES_UPDATE);

        Node existingNode = psRepository.findOneByPk(updatingNode.getPk());
        if (existingNode == null) {
            throw new IllegalArgumentException("Node with passed pk does not exist");
        }

        if (updatingNode.getPartOf() != null) {
            existingNode.getPartOf().retainAll(updatingNode.getPartOf()); // remove all links that does not exist already
            updatingNode.getPartOf().removeAll(existingNode.getPartOf()); // get all newly created links
            updatingNode.getPartOf().forEach(parent -> {
                graphLinkService.checkIfLinkingAllowed(existingNode, parent);
                existingNode.getPartOf().add(parent);
            });
        }
        existingNode.setName(updatingNode.getName());

        psRepository.save(existingNode);

        measureService.fixMeasure(MeasureService.MeasureEvent.POSTGRES_UPDATE, measureId);
    }

    public void updateNodeNeo4j(Node updatingNode) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.NEO_UPDATE);

        Node existingNode = neoRepository.findOneByPk(updatingNode.getPk());
        if (existingNode == null) {
            throw new IllegalArgumentException("Node with passed pk does not exist");
        }
        if (updatingNode.getPartOf() != null) {
            if (existingNode.getPartOf() == null || existingNode.getPartOf().isEmpty()) {
                existingNode.setPartOf(new HashSet<>());
            } else {
                existingNode.getPartOf().retainAll(updatingNode.getPartOf()); // remove all links that does not exist already
                updatingNode.getPartOf().removeAll(existingNode.getPartOf()); // get all newly created links
            }
            updatingNode.getPartOf().forEach(parent -> {
                graphLinkService.checkIfLinkingAllowed(existingNode, parent);
                existingNode.getPartOf().add(parent);
            });
        }
        existingNode.setName(updatingNode.getName());
        neoRepository.save(existingNode);

        measureService.fixMeasure(MeasureService.MeasureEvent.NEO_UPDATE, measureId);
    }

    @Transactional(transactionManager = "postgresTransactionalManager")
    public void createNodePostgres(Node node) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST);

        if (!psRepository.exists(node.getPk())) {
            psRepository.save(node);
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST, measureId);
    }

    @Transactional(transactionManager = "neoTransactionalManager")
    public void createNodeNeo4j(Node node) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.NEO_PERSIST);

        if (!neoRepository.exists(node.getPk())) {
            neoRepository.save(node);
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.NEO_PERSIST, measureId);
    }

    public void removeAllNodes() {
        psRepository.deleteAll();
        neoRepository.deleteAll();
    }
}

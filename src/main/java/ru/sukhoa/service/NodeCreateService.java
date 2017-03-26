package ru.sukhoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.DAO.Neo4j.NodeRepositoryNeo4j;
import ru.sukhoa.DAO.Postgres.NodeRepositoryPostgres;
import ru.sukhoa.domain.Node;

import java.util.List;
import java.util.UUID;

@Service
public class NodeCreateService {

    private NodeRepositoryPostgres psRepository;

    private NodeRepositoryNeo4j neoRepository;

    private MeasureService measureService;

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

    public void createNodes(final List<Node> nodes) {
        nodes.forEach(node -> {
            createNodePostgres(node);
            createNodeNeo4j(node);
        });
    }

    @org.springframework.transaction.annotation.Transactional(transactionManager = "postgresTransactionalManager")
    private void createNodePostgres(Node node) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST);

        if (!psRepository.exists(node.getPk())) {
            psRepository.save(node);
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST, measureId);
    }

    private void createNodeNeo4j(Node node) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.NEO_PERSIST);

        if (!neoRepository.exists(node.getPk())) {
            neoRepository.save(node);
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.NEO_PERSIST, measureId);
    }

    public List<Node> getSubtreeInRootOf(String id) {
        return neoRepository.getSubtreeInRootOf(id);
    }
}

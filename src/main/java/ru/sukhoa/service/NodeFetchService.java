package ru.sukhoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sukhoa.DAO.Neo4j.NodeRepositoryNeo4j;
import ru.sukhoa.DAO.Postgres.NodeRepositoryPostgres;
import ru.sukhoa.domain.Node;

import java.util.List;
import java.util.UUID;

@Service
@org.springframework.transaction.annotation.Transactional(transactionManager = "postgresTransactionalManager")
public class NodeFetchService {

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


    public List<Node> getPostgresSubtreeInRootOf(String id) {
        UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.POSTGRES_SUBTREE_FETCH);
        List<Node> subtree = psRepository.getSubtreeInRootOf(id);
        measureService.fixMeasure(MeasureService.MeasureEvent.POSTGRES_SUBTREE_FETCH, measureId);

        return subtree;
    }

    public List<Node> getNeoSubtreeInRootOf(String id) {
        UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.NEO_SUBTREE_FETCH);
        List<Node> subtree = neoRepository.getSubtreeInRootOf(id);
        measureService.fixMeasure(MeasureService.MeasureEvent.NEO_SUBTREE_FETCH, measureId);

        return subtree;
    }
}
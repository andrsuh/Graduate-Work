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
public class NodeCreateService {

    private GraphLinkService graphLinkService;

    private NodeRepositoryPostgres psRepository;

    private NodeRepositoryNeo4j neoRepository;

    private MeasureService measureService;

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

    public void createNodes(final List<Node> nodes) {
        nodes.forEach(node -> {
            createNodePostgres(node);
            createNodeNeo4j(node);
        });
    }

    private void createNodePostgres(Node node) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST);

        if (!psRepository.exists(node.getPk())) {
//            if (node.getPartOf() != null) {
//                // if node is not a group, it can has a several parents, so we will store it's parents with graph link
//                if (!"GROUP".equals(node.getType())) {
//                    graphLinkService.createPostgresGraphLink(node.getPk(), node.getPartOf().getPk());
//                    node.setPartOf(null);
//                } else {
//                    if (!psRepository.exists(node.getPartOf().getPk())) {
//                        throw new IllegalArgumentException("Parent node which has been specified does not exists yet");
//                    }
//                }
//            }
            psRepository.save(node);
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.POSTGRES_PERSIST, measureId);
    }

    private void createNodeNeo4j(Node node) {
        final UUID measureId = measureService.startMeasure(MeasureService.MeasureEvent.NEO_PERSIST);

        if (neoRepository.findOneByPk(node.getPk()) == null) {
//            Node parentNode = node.getPartOf();
//            if (parentNode != null) {
//                Node existedParentNode = neoRepository.findOneByPk(parentNode.getPk());
//                if (existedParentNode == null) {
//                    throw new IllegalArgumentException("Parent node which has benn specified does not exists yet");
//                }
//                node.setPartOf(existedParentNode);
//            }
            neoRepository.save(node);
        }

        measureService.fixMeasure(MeasureService.MeasureEvent.NEO_PERSIST, measureId);
    }

    public List<Node> getSubtreeInRootOf(String id) {
        return neoRepository.getSubtreeInRootOf(id);
    }
}

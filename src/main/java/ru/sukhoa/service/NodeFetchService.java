package ru.sukhoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.sukhoa.DAO.NodeRepository;
import ru.sukhoa.domain.Node;
import ru.sukhoa.service.MeasureService.MeasureEvent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.UUID;

@Service
public class NodeFetchService {

    private static final String FIND_NODE = "_FIND_NODE";
    private static final String SUBTREE_FETCH = "_SUBTREE_FETCH";
    private static final String CHECK_DESCENDANT = "_CHECK_DESCENDANT";
    private static final String GET_CHILDREN = "_GET_CHILDREN";

    private NodeRepository psRepository;

    private NodeRepository neoRepository;

    private MeasureService measureService;

    @Autowired
    public void setNeoRepository(@Qualifier("NodeRepositoryNeo4j") NodeRepository neoRepository) {
        this.neoRepository = neoRepository;
    }

    @Autowired
    public void setPsRepository(@Qualifier("NodeRepositoryPostgres") NodeRepository psRepository) {
        this.psRepository = psRepository;
    }

    @Autowired
    public void setMeasureService(MeasureService measureService) {
        this.measureService = measureService;
    }


    public List<Node> getPostgresSubtreeInRootOf(String id) {
        return getSubtreeInRootOf(psRepository, id);
    }

    public List<Node> getNeoSubtreeInRootOf(String id) {
        return getSubtreeInRootOf(neoRepository, id);
    }

    public Boolean checkPostgresIsNodeADescendantOFAnother(String childId, String parentId) {
        return checkIsNodeADescendantOFAnother(psRepository, childId, parentId);
    }

    public Boolean checkNeoIsNodeADescendantOFAnother(String childId, String parentId) {
        return checkIsNodeADescendantOFAnother(neoRepository, childId, parentId);
    }

    public List<Node> getNeoChildrenOfNode(String id) {
        return getChildrenOfNode(neoRepository, id);
    }

    public List<Node> getPostgresChildrenOfNode(String id) {
        return getChildrenOfNode(psRepository, id);
    }

    public Node findNeoNodeById(String nodeId) {
        return findNodeById(neoRepository, nodeId);
    }

    public Node findPostgresNodeById(String nodeId) {
        return findNodeById(psRepository, nodeId);
    }

    private List<Node> getChildrenOfNode(@Nonnull NodeRepository repository, String id) {
        MeasureEvent event = MeasureEvent.valueOf(repository.getDatasourceName() + GET_CHILDREN);
        UUID measureId = measureService.startMeasure(event);
        List<Node> subtree = repository.getChildrenOfNode(id);
        subtree.forEach(node -> node.setPartOf(null));
        measureService.fixMeasure(event, measureId);

        return subtree;
    }

    private List<Node> getSubtreeInRootOf(@Nonnull NodeRepository repository, String id) {
        MeasureEvent event = MeasureEvent.valueOf(repository.getDatasourceName() + SUBTREE_FETCH);
        UUID measureId = measureService.startMeasure(event);
        List<Node> subtree = repository.getSubtreeInRootOf(id);
        subtree.forEach(node -> node.setPartOf(null));
        measureService.fixMeasure(event, measureId);

        return subtree;
    }

    private Boolean checkIsNodeADescendantOFAnother(@Nonnull NodeRepository repository, String childId, String parentId) {
        MeasureEvent event = MeasureEvent.valueOf(repository.getDatasourceName() + CHECK_DESCENDANT);
        UUID measureId = measureService.startMeasure(event);
        Boolean result = repository.isNodeDescendantOfAnother(childId, parentId);
        measureService.fixMeasure(event, measureId);

        return result;
    }

    private Node findNodeById(@Nonnull NodeRepository repository, String nodeId) {
        MeasureEvent event = MeasureEvent.valueOf(repository.getDatasourceName() + FIND_NODE);
        UUID measureId = measureService.startMeasure(event);
        Node result = repository.findOneByPk(nodeId);
        result.setPartOf(null);
        measureService.fixMeasure(event, measureId);

        return result;
    }
}
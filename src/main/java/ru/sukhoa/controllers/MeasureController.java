package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.domain.MeasureEntity;
import ru.sukhoa.service.GraphLinkService;
import ru.sukhoa.service.MeasureService;
import ru.sukhoa.service.NodeCreateService;
import ru.sukhoa.service.NodeFetchService;

@RestController()
@RequestMapping("/measure")
public class MeasureController {
    private NodeCreateService nodeCreateService;

    private NodeFetchService nodeFetchService;

    private GraphLinkService graphLinkService;

    private MeasureService measureService;

    @Autowired
    public void setNodeCreateService(NodeCreateService nodeCreateService) {
        this.nodeCreateService = nodeCreateService;
    }

    @Autowired
    public void setNodeFetchService(NodeFetchService nodeFetchService) {
        this.nodeFetchService = nodeFetchService;
    }

    @Autowired
    public void setGraphLinkService(GraphLinkService graphLinkService) {
        this.graphLinkService = graphLinkService;
    }

    @Autowired
    public void setMeasureService(MeasureService measureService) {
        this.measureService = measureService;
    }

    @RequestMapping(value = "/subtree/postgres", method = RequestMethod.GET)
    public MeasureEntity measurePostgresSubtreeFetching() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.POSTGRES_SUBTREE_FETCH);
    }

    @RequestMapping(value = "/subtree/neo", method = RequestMethod.GET)
    public MeasureEntity measureNeoSubtreeFetching() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.NEO_SUBTREE_FETCH);
    }

    @RequestMapping(value = "/persist/postgres", method = RequestMethod.GET)
    public MeasureEntity measurePostgresPersist() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.POSTGRES_SUBTREE_FETCH);
    }

    @RequestMapping(value = "/persist/neo", method = RequestMethod.GET)
    public MeasureEntity measureNeoPersist() {
        return measureService.getMeasureEntityByEvent(MeasureService.MeasureEvent.NEO_SUBTREE_FETCH);
    }
}

package ru.sukhoa.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sukhoa.domain.Node;
import ru.sukhoa.service.GraphLinkService;
import ru.sukhoa.service.MeasureService;
import ru.sukhoa.service.NodeCreateService;
import ru.sukhoa.service.NodeFetchService;

import java.util.List;

@RestController
public class NodeController {
    final static Logger logger = Logger.getLogger(NodeController.class);

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

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/node", method = RequestMethod.POST)
    public void createNodes(@RequestBody(required = true) final List<Node> nodes) {
        nodeCreateService.createNodes(nodes);
    }

    @RequestMapping(value = "/postgres/subtree", method = RequestMethod.GET)
    public List<Node> getPostgresSubtreeInRootOf(@RequestParam(name = "id", required = true) final String id) {
        return nodeFetchService.getPostgresSubtreeInRootOf(id);
    }

    @RequestMapping(value = "/neo/subtree", method = RequestMethod.GET)
    public List<Node> getNeoSubtreeInRootOf(@RequestParam(name = "id", required = true) final String id) {
        return nodeFetchService.getNeoSubtreeInRootOf(id);
    }

//    @RequestMapping(value = "/node/{child_pk}/linked_to/{parent_pk}", method = RequestMethod.POST)
//    public void linkNodes(@PathParam("child_pk") String childPk, @PathParam("parent_pk") String parentPk) {
//        graphLinkService.createPostgresGraphLink(childPk, parentPk);
//    }

}

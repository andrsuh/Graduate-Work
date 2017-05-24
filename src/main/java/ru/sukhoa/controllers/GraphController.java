package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.service.GraphLinkService;
import ru.sukhoa.service.NodeService;

@RestController
@RequestMapping("/graph")
public class GraphController {
    private NodeService nodeService;

    private GraphLinkService linkService;

    @Autowired
    public GraphController(NodeService nodeService, GraphLinkService linkService) {
        this.nodeService = nodeService;
        this.linkService = linkService;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void removeGraph() {
        linkService.removeAllLinks();
        nodeService.removeAllNodes();
    }
}
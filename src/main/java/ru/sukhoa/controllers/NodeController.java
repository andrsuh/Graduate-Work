package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sukhoa.domain.Node;
import ru.sukhoa.service.NodeService;

import java.util.List;

@RestController
@RequestMapping("/nodes")
public class NodeController {
    private NodeService nodeService;

    @Autowired
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST)
    public void createNodes(@RequestBody final List<Node> nodes) {
        nodeService.createNodes(nodes);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "postgres", method = RequestMethod.POST)
    public void createNodesPostgres(@RequestBody final List<Node> nodes) {
        nodes.forEach(nodeService::createNodePostgres);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "neo", method = RequestMethod.POST)
    public void createNodesNeo(@RequestBody final List<Node> nodes) {
        nodes.forEach(nodeService::createNodeNeo4j);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT)
    public void updateNode(@RequestBody Node updatingNode) {
        nodeService.updateNode(updatingNode);
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "postgres", method = RequestMethod.PUT)
    public void updateNodePostgres(@RequestBody Node updatingNode) {
        nodeService.updateNodePostgres(updatingNode);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "neo", method = RequestMethod.PUT)
    public void updateNodeNeo(@RequestBody Node updatingNode) {
        nodeService.updateNodeNeo4j(updatingNode);
    }
}

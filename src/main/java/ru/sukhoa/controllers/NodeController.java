package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(method = RequestMethod.POST)
    public void createNodes(@RequestBody List<Node> nodes) {
        nodeService.createNodes(nodes);
    }

    @RequestMapping(value = "postgres", method = RequestMethod.POST)
    public void createNodesPostgres(@RequestBody List<Node> nodes) {
        nodes.forEach(nodeService::createNodePostgres);
    }

    @RequestMapping(value = "neo", method = RequestMethod.POST)
    public void createNodesNeo(@RequestBody List<Node> nodes) {
        nodes.forEach(nodeService::createNodeNeo4j);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateNode(@RequestBody Node updatingNode) {
        nodeService.updateNode(updatingNode);
    }

    @RequestMapping(value = "postgres", method = RequestMethod.PUT)
    public void updateNodePostgres(@RequestBody Node updatingNode) {
        nodeService.updateNodePostgres(updatingNode);
    }

    @RequestMapping(value = "neo", method = RequestMethod.PUT)
    public void updateNodeNeo(@RequestBody Node updatingNode) {
        nodeService.updateNodeNeo4j(updatingNode);
    }
}

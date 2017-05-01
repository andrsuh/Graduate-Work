package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sukhoa.domain.Node;
import ru.sukhoa.service.NodeService;

import java.util.Arrays;
import java.util.List;

@RestController
public class NodeController {
    private NodeService nodeService;

    @Autowired
    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/create_nodes", method = RequestMethod.POST)
    public void createNodes(@RequestBody final List<Node> nodes) {
        nodeService.createNodes(nodes);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/create_node", method = RequestMethod.POST)
    public void createNodes(@RequestBody Node node) {
        nodeService.createNodes(Arrays.asList(node));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/update_node", method = RequestMethod.PUT)
    public void updateNode(@RequestBody Node updatingNode) {
        nodeService.updateNode(updatingNode);
    }
}

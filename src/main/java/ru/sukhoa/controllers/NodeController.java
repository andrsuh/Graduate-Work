package ru.sukhoa.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sukhoa.domain.Node;
import ru.sukhoa.service.NodeCreateService;

import java.util.List;

@RestController
public class NodeController {
    final static Logger logger = Logger.getLogger(NodeController.class);

    private NodeCreateService nodeCreateService;

    @Autowired
    public void setNodeCreateService(NodeCreateService nodeCreateService) {
        this.nodeCreateService = nodeCreateService;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/node", method = RequestMethod.POST)
    public void createNodes(@RequestBody(required = true) final List<Node> nodes) {
        nodeCreateService.createNodes(nodes);
    }
}

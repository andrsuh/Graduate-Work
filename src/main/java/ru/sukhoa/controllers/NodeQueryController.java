package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.sukhoa.domain.Node;
import ru.sukhoa.service.NodeFetchService;

import java.util.List;

@RestController
@RequestMapping("/query")
public class NodeQueryController {
    private NodeFetchService nodeFetchService;

    @Autowired
    public void setNodeFetchService(NodeFetchService nodeFetchService) {
        this.nodeFetchService = nodeFetchService;
    }

    @RequestMapping(value = "/postgres/{childId}/isDescendantOf/{parentId}", method = RequestMethod.GET)
    public Boolean checkPostgresIsNodeADescendantOFAnother(
            @PathVariable final String childId, @PathVariable final String parentId) {
        return nodeFetchService.checkPostgresIsNodeADescendantOFAnother(childId, parentId);
    }

    @RequestMapping(value = "/neo/{childId}/isDescendantOf/{parentId}", method = RequestMethod.GET)
    public Boolean checkNeoIsNodeADescendantOFAnother(
            @PathVariable final String childId, @PathVariable final String parentId) {
        return nodeFetchService.checkNeoIsNodeADescendantOFAnother(childId, parentId);
    }

    @RequestMapping(value = "/neo/{nodeId}", method = RequestMethod.GET)
    public Node findNeoNodeBuId(@PathVariable final String nodeId) {
        return nodeFetchService.findNeoNodeById(nodeId);
    }

    @RequestMapping(value = "/postgres/{nodeId}", method = RequestMethod.GET)
    public Node findPostgresNodeBuId(@PathVariable final String nodeId) {
        return nodeFetchService.findPostgresNodeById(nodeId);
    }

    @RequestMapping(value = "/postgres/subtree", method = RequestMethod.GET)
    public List<Node> getPostgresSubtreeInRootOf(@RequestParam(name = "id", required = true) final String id) {
        return nodeFetchService.getPostgresSubtreeInRootOf(id);
    }

    @RequestMapping(value = "/neo/subtree", method = RequestMethod.GET)
    public List<Node> getNeoSubtreeInRootOf(@RequestParam(name = "id", required = true) final String id) {
        return nodeFetchService.getNeoSubtreeInRootOf(id);
    }

    @RequestMapping(value = "/neo/children", method = RequestMethod.GET)
    public List<Node> getNeoChildrenOfNode(@RequestParam(name = "id", required = true) final String id) {
        return nodeFetchService.getNeoChildrenOfNode(id);
    }

    @RequestMapping(value = "/postgres/children", method = RequestMethod.GET)
    public List<Node> getPostgresChildrenOfNode(@RequestParam(name = "id", required = true) final String id) {
        return nodeFetchService.getPostgresChildrenOfNode(id);
    }
}

package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    @RequestMapping(value = "/postgres/if_descendant", method = RequestMethod.GET)
    public Boolean checkPostgresIsNodeADescendantOFAnother(
            @RequestParam("child_id") final String childId, @RequestParam("parent_id") final String parentId) {
        return nodeFetchService.checkPostgresIsNodeADescendantOFAnother(childId, parentId);
    }

    @RequestMapping(value = "/neo/if_descendant", method = RequestMethod.GET)
    public Boolean checkNeoIsNodeADescendantOFAnother(
            @RequestParam("child_id") final String childId, @RequestParam("parent_id") final String parentId) {
        return nodeFetchService.checkNeoIsNodeADescendantOFAnother(childId, parentId);
    }

    @RequestMapping(value = "/neo", method = RequestMethod.GET)
    public Node findNeoNodeBuId(@RequestParam("id") final String nodeId) {
        return nodeFetchService.findNeoNodeById(nodeId);
    }

    @RequestMapping(value = "/postgres", method = RequestMethod.GET)
    public Node findPostgresNodeBuId(@RequestParam("id") final String nodeId) {
        return nodeFetchService.findPostgresNodeById(nodeId);
    }

    @RequestMapping(value = "/postgres/subtree", method = RequestMethod.GET)
    public List<Node> getPostgresSubtreeInRootOf(@RequestParam(name = "id") final String id) {
        return nodeFetchService.getPostgresSubtreeInRootOf(id);
    }

    @RequestMapping(value = "/neo/subtree", method = RequestMethod.GET)
    public List<Node> getNeoSubtreeInRootOf(@RequestParam(name = "id") final String id) {
        return nodeFetchService.getNeoSubtreeInRootOf(id);
    }

    @RequestMapping(value = "/neo/children", method = RequestMethod.GET)
    public List<Node> getNeoChildrenOfNode(@RequestParam(name = "id") final String id) {
        return nodeFetchService.getNeoChildrenOfNode(id);
    }

    @RequestMapping(value = "/postgres/children", method = RequestMethod.GET)
    public List<Node> getPostgresChildrenOfNode(@RequestParam(name = "id") final String id) {
        return nodeFetchService.getPostgresChildrenOfNode(id);
    }
}

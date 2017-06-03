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
            @RequestParam("child_id") final String childId, @RequestParam("parent_id") final String parentId,
            @RequestParam(value = "times", defaultValue = "1") int times) {
        if (times < 1) {
            throw new IllegalArgumentException("Passed times value is incorrect: " + times);
        }
        Boolean result = null;
        for (int i = 0; i < times; ++i) {
            result = nodeFetchService.checkPostgresIsNodeADescendantOFAnother(childId, parentId);
        }
        return result;
    }

    @RequestMapping(value = "/neo/if_descendant", method = RequestMethod.GET)
    public Boolean checkNeoIsNodeADescendantOFAnother(
            @RequestParam("child_id") final String childId, @RequestParam("parent_id") final String parentId,
            @RequestParam(value = "times", defaultValue = "1") int times) {
        if (times < 1) {
            throw new IllegalArgumentException("Passed times value is incorrect: " + times);
        }
        Boolean result = null;
        for (int i = 0; i < times; ++i) {
            result = nodeFetchService.checkNeoIsNodeADescendantOFAnother(childId, parentId);
        }
        return result;
    }

    @RequestMapping(value = "/neo", method = RequestMethod.GET)
    public Node findNeoNodeById(@RequestParam("id") final String nodeId) {
        return nodeFetchService.findNeoNodeById(nodeId);
    }

    @RequestMapping(value = "/postgres", method = RequestMethod.GET)
    public Node findPostgresNodeById(@RequestParam("id") final String nodeId) {
        return nodeFetchService.findPostgresNodeById(nodeId);
    }

    @RequestMapping(value = "/neo/get_by_name", method = RequestMethod.GET)
    public Node findNeoNodeByName(@RequestParam("name") final String name) {
        return nodeFetchService.findNeoNodeByName(name);
    }

    @RequestMapping(value = "/postgres/get_by_name", method = RequestMethod.GET)
    public Node findPostgresNodeByName(@RequestParam("name") final String name) {
        return nodeFetchService.findNeoNodeByName(name);
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

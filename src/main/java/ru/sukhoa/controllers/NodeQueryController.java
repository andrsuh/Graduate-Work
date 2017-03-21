package ru.sukhoa.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.sukhoa.service.NodeFetchService;

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
}
